package com.example.bean;

import com.example.util.IndexClientRecordUtil;
import com.example.util.RoomClientRecordUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import net.sf.json.JSONObject;

public class RoomHandler extends AbstractWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(RoomHandler.class);
    RoomClientRecordUtil roomClientRecordUtil = RoomClientRecordUtil.getInstance();
    IndexClientRecordUtil indexClientRecordUtil = IndexClientRecordUtil.getInstance();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("👉Connection established.");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info("👉Connecton closed. status: " + status);
        int roomId = roomClientRecordUtil.getSessionIdHashRoomId().get(session.getId());
        roomClientRecordUtil.removeClient(session, roomId);
        // 推送在自习室的所有客户端：当前自习室人数，推送主页的所有客户端：所有自习室人数
        roomClientRecordUtil.pushRoomPeopleCntToAllClients(roomId);
        indexClientRecordUtil.pushAllRoomsPeopleCntToAllClients();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        // 接收自习室Id，根据信息得出是进入还是退出，然后通知该自习室的所有客户端
        String msg = message.getPayload();
        logger.info("📤Received message: " + msg);

        JSONObject jsonObject = JSONObject.fromObject(msg);
        RoomState roomState = (RoomState)JSONObject.toBean(jsonObject, RoomState.class);
        logger.info("👉json转为pojo: " + roomState);

        int roomId = roomState.getRoomId();
        if (roomState.getAction().equals("enter")) { // big error: 判断字符串相等用了==
            roomClientRecordUtil.addClient(session, roomId);
        }
        // 改为afterConnectionClosed处理，避免WebSocket过时不会发送消息给服务端，也就消除不了记录了
        // else if (roomState.getAction().equals("leave")) {
        // roomClientRecordUtil.removeClient(session, roomId);
        // }
        else {
            throw new Exception("💥error status!");
        }
        roomClientRecordUtil.pushRoomPeopleCntToAllClients(roomId);
        // 每当有人进出自习室，还要给主页的所有客户端推送消息
        indexClientRecordUtil.pushAllRoomsPeopleCntToAllClients();
    }
}

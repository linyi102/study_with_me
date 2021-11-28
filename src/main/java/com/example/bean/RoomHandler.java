package com.example.bean;

import com.example.util.PeopleRecordUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import net.sf.json.JSONObject;

public class RoomHandler extends AbstractWebSocketHandler {
    PeopleRecordUtil peopleRecordUtil = new PeopleRecordUtil();
    private static final Logger logger = LoggerFactory.getLogger(RoomHandler.class);

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info("👉Connecton closed. status: " + status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("👉Connection established.");
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
            peopleRecordUtil.addPeople(session, roomId);
        } else if (roomState.getAction().equals("leave")) {
            peopleRecordUtil.deletePeople(session, roomId);
        }
        peopleRecordUtil.pushClient(roomId);
    }
}

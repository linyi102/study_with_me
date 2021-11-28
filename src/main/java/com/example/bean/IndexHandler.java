package com.example.bean;

import com.example.util.IndexClientRecordUtil;
import com.example.util.RoomClientRecordUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class IndexHandler extends AbstractWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(IndexHandler.class);
    RoomClientRecordUtil roomClientRecordUtil = RoomClientRecordUtil.getInstance();
    IndexClientRecordUtil indexClientRecordUtil = IndexClientRecordUtil.getInstance();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("👉Connection established.");
        // 为啥之前会提示NullException，现在却没有了？
        indexClientRecordUtil.addClient(session);
        indexClientRecordUtil.pushAllRoomsPeopleCntToClient(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info("👉Connecton closed. status: " + status);
        indexClientRecordUtil.removeClient(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        // String msg = message.getPayload();
        // logger.info("📤Received message: " + msg);
        // if (msg.equals("enter index page")) {
        //     indexClientRecordUtil.addClient(session);
        // } else if (msg.equals("leave index page")) {
        //     indexClientRecordUtil.removeClient(session);
        // } else {
        //     throw new Exception("💥error status!");
        // }
        // // 尽管自习室人数没有影响，但主页需要获取到所有自习室的人数，因此仅需要对进主页的推送消息
        // indexClientRecordUtil.pushAllRoomsPeopleCntToClient(session);
    }
}

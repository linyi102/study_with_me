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
    IndexClientRecordUtil indexClientRecordUtil = IndexClientRecordUtil.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(IndexHandler.class);
    RoomClientRecordUtil peopleRecordUtil = RoomClientRecordUtil.getInstance();

    // 进入主页，客户端与服务端建立，服务端向客户端发送数据：所有自习室的各自人数
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("👉Connection established.");
        // 会提示NullException
        // indexClientRecordUtil.addClient(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info("👉Connecton closed. status: " + status);
        // indexClientRecordUtil.removeClient(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String msg = message.getPayload();
        logger.info("📤Received message: " + msg);
        if (msg.equals("enter index page")) {
            indexClientRecordUtil.addClient(session);
        } else if (msg.equals("leave index page")) {
            indexClientRecordUtil.removeClient(session);
        } else {
            throw new Exception("💥error status!");
        }
        peopleRecordUtil.pushAllRoomPeopleCntToClient();
    }
}

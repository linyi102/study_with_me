package com.example.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

// 单例模式，目的是为了IndexHandler和RoomClientRecordUtil都可以处理主页客户端
public class IndexClientRecordUtil {
    private static final Logger logger = LoggerFactory.getLogger(IndexClientRecordUtil.class);
    private HashSet<WebSocketSession> clientsOfIndex = new HashSet<>();
    private static IndexClientRecordUtil indexClientRecordUtil;

    private IndexClientRecordUtil() {}

    public static IndexClientRecordUtil getInstance() {
        if (indexClientRecordUtil == null) {
            synchronized (IndexClientRecordUtil.class) {
                if (indexClientRecordUtil == null) {
                    indexClientRecordUtil = new IndexClientRecordUtil();
                }
            }
        }
        return indexClientRecordUtil;
    }

    public HashSet<WebSocketSession> getClientsOfIndex() {
        return clientsOfIndex;
    }

    public void addClient(WebSocketSession session) {
        logger.info("⏩进入主页：sessionId=" + session.getId());
        clientsOfIndex.add(session);
    }

    public void removeClient(WebSocketSession session) {
        logger.info("⏩离开主页：sessionId=" + session.getId());
        clientsOfIndex.remove(session);
    }

    // 把新人数推送给该自习室中的所有客户端
    public void pushAllRoomsPeopleCntToAllClients() throws IOException {
        logger.info("📢推送给主页中的所有客户端：");
        String msg = generateMsg();
        HashSet<WebSocketSession> clientsOfIndex = IndexClientRecordUtil.getInstance().getClientsOfIndex();
        for (WebSocketSession session : clientsOfIndex) {
            logger.info("sessionId=" + session.getId());
            session.sendMessage(new TextMessage(msg));
        }
        logger.info("📤推送消息：" + msg);
    }

    // 仅推送给刚进主页的客户端
    public void pushAllRoomsPeopleCntToClient(WebSocketSession session) throws IOException {
        logger.info("📢推送给主页中的某个客户端：");
        String msg = generateMsg();
        logger.info("sessionId=" + session.getId());
        session.sendMessage(new TextMessage(msg));
        logger.info("📤推送消息：" + msg);
    }

    public String generateMsg() {
        StringBuffer msg = new StringBuffer();
        // 获取所有自习室的情况
        ArrayList<HashMap<String, WebSocketSession>> clientsOfRoom =
            RoomClientRecordUtil.getInstance().getClientsOfRoom();
        for (int i = 0; i < clientsOfRoom.size(); ++i) { // 把索引0也加上，方便JS处理
            msg.append(clientsOfRoom.get(i).size() + " ");
        }
        return msg.toString();
    }
}

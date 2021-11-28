package com.example.util;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

}

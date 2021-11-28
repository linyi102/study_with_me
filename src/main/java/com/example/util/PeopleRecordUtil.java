package com.example.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class PeopleRecordUtil {
    private final int roomCnt = 10;
    private ArrayList<HashMap<String, WebSocketSession>> clientsOfRoom = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(PeopleRecordUtil.class);

    // 必须要为每个元素指定一个空间
    public PeopleRecordUtil() {
        for (int i = 0; i < roomCnt; ++i) {
            clientsOfRoom.add(new HashMap<String, WebSocketSession>());
        }
    }

    public void addPeople(WebSocketSession session, int roomId) {
        logger.info("⏩进入自习室" + roomId + "：sessionId=" + session.getId());
        clientsOfRoom.get(roomId).put(session.getId(), session);
    }

    public void deletePeople(WebSocketSession session, int roomId) {
        logger.info("⏪退出自习室" + roomId + "：session=" + session.getId());
        clientsOfRoom.get(roomId).remove(session.getId());
    }

    // 把新人数推送给该自习室中的所有客户端
    public void pushClient(int roomId) throws Exception {
        logger.info("📢推送目标：");
        HashMap<String, WebSocketSession> hashMap = clientsOfRoom.get(roomId);
        int roomPeopleCnt = clientsOfRoom.get(roomId).size();
        for (Map.Entry<String, WebSocketSession> entry : hashMap.entrySet()) {
            logger.info("sessionId=" + entry.getKey());
            WebSocketSession session = entry.getValue();
            session.sendMessage(new TextMessage(Integer.toString(roomPeopleCnt)));
        }
    }
}

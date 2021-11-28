package com.example.util;

import java.util.ArrayList;

import com.example.bean.RoomHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class PeopleRecordUtil {
    private final int roomCnt = 10;
    private ArrayList<ArrayList<WebSocketSession>> clientsOfRoom = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(RoomHandler.class);

    // 必须要为每个元素指定一个空间
    public PeopleRecordUtil() {
        for (int i = 0; i < roomCnt; ++i) {
            ArrayList<WebSocketSession> elem = new ArrayList<>();
            clientsOfRoom.add(elem);
        }
    }

    public void addPeople(WebSocketSession session, int roomId) {
        logger.info("⏩进入自习室" + roomId + "：session=" + session);
        clientsOfRoom.get(roomId).add(session);
    }

    public void deletePeople(WebSocketSession session, int roomId) {
        logger.info("⏪退出自习室" + roomId + "：session=" + session);
        clientsOfRoom.get(roomId).remove(session);
    }

    // 把新人数推送给该自习室中的所有客户端
    public void pushClient(int roomId) throws Exception {
        logger.info("📢推送目标：");
        ArrayList<WebSocketSession> arrayList = clientsOfRoom.get(roomId);

        for (WebSocketSession session : arrayList) {
            logger.info("session=" + session);
            session.sendMessage(new TextMessage("" + clientsOfRoom.get(roomId).size()));
        }
    }
}

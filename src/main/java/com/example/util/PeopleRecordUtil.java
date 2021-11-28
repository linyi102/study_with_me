package com.example.util;

import java.util.ArrayList;

import com.example.bean.RoomHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class PeopleRecordUtil {
    private final int roomCnt = 10;
    private ArrayList<ArrayList<SessionEntity>> clientsOfRoom = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(RoomHandler.class);

    // 必须要为每个元素指定一个空间
    public PeopleRecordUtil() {
        for (int i = 0; i < roomCnt; ++i) {
            ArrayList<SessionEntity> elem = new ArrayList<>();
            clientsOfRoom.add(elem);
        }
    }

    public void addPeople(WebSocketSession session, int roomId) {
        logger.info("⏩进入自习室" + roomId + "：sessionId=" + session.getId());
        SessionEntity sessionEntity = new SessionEntity(session, session.getId());
        clientsOfRoom.get(roomId).add(sessionEntity);
    }

    public void deletePeople(WebSocketSession session, int roomId) {
        logger.info("⏪退出自习室" + roomId + "：session=" + session.getId());
        // clientsOfRoom.get(roomId).remove(sessionEntity);
        for (int i = 0; i < clientsOfRoom.get(roomId).size(); ++i) {
            SessionEntity sessionEntity = clientsOfRoom.get(roomId).get(i);
            if (sessionEntity.getSessionId().equals(session.getId())) { // 只判断id
                clientsOfRoom.get(roomId).remove(i); // 直接根据索引去除该元素
                break;
            }
        }
    }

    // 把新人数推送给该自习室中的所有客户端
    public void pushClient(int roomId) throws Exception {
        logger.info("📢推送目标：");
        ArrayList<SessionEntity> arrayList = clientsOfRoom.get(roomId);

        for (SessionEntity sessionEntity : arrayList) {
            String sessionId = sessionEntity.getSessionId();
            logger.info("sessionId=" + sessionId);
            WebSocketSession session = sessionEntity.getSession();
            // 因为还需要向session发送消息，所以不能简单把session全部换成sessionId
            session.sendMessage(new TextMessage("" + clientsOfRoom.get(roomId).size()));
        }
    }
}

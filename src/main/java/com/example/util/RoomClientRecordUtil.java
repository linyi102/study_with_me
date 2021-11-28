package com.example.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class RoomClientRecordUtil {
    private final int roomCnt = 10;
    private ArrayList<HashMap<String, WebSocketSession>> clientsOfRoom = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(RoomClientRecordUtil.class);
    private static RoomClientRecordUtil roomClientRecordUtil;

    // 必须要为每个元素指定一个空间
    private RoomClientRecordUtil() {
        for (int i = 0; i < roomCnt; ++i) {
            clientsOfRoom.add(new HashMap<String, WebSocketSession>());
        }
    }

    public static RoomClientRecordUtil getInstance() {
        if (roomClientRecordUtil == null) {
            synchronized (RoomClientRecordUtil.class) {
                if (roomClientRecordUtil == null) {
                    roomClientRecordUtil = new RoomClientRecordUtil();
                }
            }

        }
        return roomClientRecordUtil;
    }

    public void addClient(WebSocketSession session, int roomId) {
        logger.info("⏩进入自习室" + roomId + "：sessionId=" + session.getId());
        clientsOfRoom.get(roomId).put(session.getId(), session);
    }

    public void removeClient(WebSocketSession session, int roomId) {
        logger.info("⏪退出自习室" + roomId + "：sessionId=" + session.getId());
        clientsOfRoom.get(roomId).remove(session.getId());
    }

    // 把新人数推送给该自习室中的所有客户端
    public void pushRoomPeopleCntToClient(int roomId) throws Exception {
        logger.info("📢推送给该自习室中的所有客户端：");
        HashMap<String, WebSocketSession> hashMap = clientsOfRoom.get(roomId);
        int roomPeopleCnt = clientsOfRoom.get(roomId).size();
        for (Map.Entry<String, WebSocketSession> entry : hashMap.entrySet()) {
            logger.info("sessionId=" + entry.getKey());
            WebSocketSession session = entry.getValue();
            session.sendMessage(new TextMessage(Integer.toString(roomPeopleCnt)));
        }
    }

    public void pushAllRoomPeopleCntToClient() throws IOException {
        logger.info("📢推送给主页中的所有客户端：");
        IndexClientRecordUtil indexClientRecordUtil = IndexClientRecordUtil.getInstance();
        HashSet<WebSocketSession> clientsOfIndex = indexClientRecordUtil.getClientsOfIndex();

        StringBuffer msg = new StringBuffer();
        for (int i = 0; i < roomCnt; ++i) { // 把索引0也加上，方便JS处理
            msg.append(clientsOfRoom.get(i).size() + " ");
        }
        logger.info("📤推送信息：" + msg);

        for (WebSocketSession session : clientsOfIndex) {
            logger.info("sessionId=" + session.getId());
            session.sendMessage(new TextMessage(msg.toString()));
        }
    }
}

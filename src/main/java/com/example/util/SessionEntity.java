package com.example.util;

import org.springframework.web.socket.WebSocketSession;

public class SessionEntity {
    private WebSocketSession session;
    private String sessionId;

    public SessionEntity(WebSocketSession session, String sessionId) {
        this.session = session;
        this.sessionId = sessionId;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}

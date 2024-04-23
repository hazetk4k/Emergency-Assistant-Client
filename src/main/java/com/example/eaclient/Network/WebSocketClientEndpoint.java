package com.example.eaclient.Network;


import jakarta.websocket.*;

@ClientEndpoint
public class WebSocketClientEndpoint {
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to WebSocket.");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message from server: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        this.session = null;
        System.out.println("WebSocket connection closed: " + closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Throwable t) {
        System.err.println("WebSocket error occurred: " + t.getMessage());
    }
}
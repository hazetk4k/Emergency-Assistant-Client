package com.example.eaclient.Network.WebSocket;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocketClient {
    private static WebSocketClient instance;
    private final WebSocketContainer container;
    private Session session;
    private ExecutorService executor;
    private Timer pingTimer;

    private WebSocketClient() {
        container = ContainerProvider.getWebSocketContainer();
    }

    public static WebSocketClient getInstance() {
        if (instance == null) {
            instance = new WebSocketClient();
        }
        return instance;
    }

    private void startPingTimer() {
        pingTimer = new Timer();
        pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (session != null && session.isOpen()) {
                    try {
                        session.getBasicRemote().sendPing(ByteBuffer.wrap(new byte[0]));
                    } catch (Exception e) {
                        System.out.println("Error sending ping: " + e.getMessage());
                    }
                }
            }
        }, 300000, 300000);
    }

    private void stopPingTimer() {
        if (pingTimer != null) {
            pingTimer.cancel();
        }
    }

    public void connect() {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                session = container.connectToServer(new WebSocketClientEndpoint(), new URI("ws://localhost:8085/"));
                startPingTimer();
            } catch (Exception e) {
                System.out.println("Error connecting to server: " + e.getMessage());
            }
        });
    }

    public void disconnect() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                System.out.println("Error closing session: " + e.getMessage());
            }
        }
        executor.shutdownNow();
        stopPingTimer();
    }
}

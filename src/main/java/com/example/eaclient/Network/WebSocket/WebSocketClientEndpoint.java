package com.example.eaclient.Network.WebSocket;


import com.example.eaclient.Models.ReportTableModels.AllReportsTable;
import com.example.eaclient.Models.ReportTableModels.UpdateStageModel;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ClientEndpoint
public class WebSocketClientEndpoint {

    private final Gson gson = new Gson();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to WebSocket.");
    }

    @OnMessage
    public void onMessage(String message) {
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        if (jsonObject.has("report_id") && jsonObject.has("stage_name")) {
            UpdateStageModel reportData = gson.fromJson(message, UpdateStageModel.class);
            ServiceSingleton.getInstance().deliverNewStageToController(reportData);
            System.out.println("Обновление поля");
        } else {
            AllReportsTable reportsTable = gson.fromJson(message, AllReportsTable.class);
            ServiceSingleton.getInstance().deliverNewReportToController(reportsTable);
            System.out.println("Загрузка нового поля");
        }
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
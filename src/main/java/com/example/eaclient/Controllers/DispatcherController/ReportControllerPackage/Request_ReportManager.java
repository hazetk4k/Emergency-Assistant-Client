package com.example.eaclient.Controllers.DispatcherController.ReportControllerPackage;

import com.example.eaclient.Models.ReportWindowModels.Applicant;
import com.example.eaclient.Models.ReportWindowModels.DataReportApplicant;
import com.example.eaclient.Models.ReportWindowModels.DispChoice;
import com.example.eaclient.Models.ReportWindowModels.Report;
import com.example.eaclient.Models.ReportWindowModels.ReportApplicant;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Request_ReportManager {
    private final Gson gson = new Gson();

    public ReportApplicant loadReportApplicantData(int id) {
        ReportApplicant reportApplicantData = new ReportApplicant();
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-report-applicant-data", "report_id=" + id);
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                DataReportApplicant data = gson.fromJson(body, DataReportApplicant.class);
                Report report = data.getReport();
                Applicant applicant = data.getApplicant();
                reportApplicantData.setApplicant(applicant);
                reportApplicantData.setReport(report);
            } else {
                System.err.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return reportApplicantData;
    }

    public Map<String, List<String>> loadCharsServicesDistrictsData() {
        Map<String, List<String>> emergencyData = new HashMap<>();
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/set-up-emergency-data");
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                Type type = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                emergencyData = gson.fromJson(body, type);

            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return emergencyData;
    }

    public DispChoice loadDispChoice(int id) {
        DispChoice dispChoice = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-dispatcher-choice", "report_id=" + id);
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                dispChoice = gson.fromJson(body, DispChoice.class);
                System.out.println("Стадия реагирования на заявление: " + dispChoice.getStage());
            } else if (code == 404) {

                System.out.println("Заявление еще не просмотрено!");
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки результатов реагирования" + e.getMessage());
        }
        return dispChoice;
    }

    public Map<String, String> loadKindCharByType(String type) {
        Map<String, String> map = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-kind-char-by-type", "type_name=" + type);
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String response = httpResponse.getResponseBody();
                map = gson.fromJson(response, Map.class);
            } else if (code == 404) {
                System.out.println("Связь с типом не найдена!");
            } else {
                System.err.println("Ошибка при поиске связей с указанным типом!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при поиске связанных с типом данных: " + e.getMessage());
        }
        return map;
    }

    public List<String> loadRecommendedServices(String kind) {
        List<String> listOfRecommendedServices = new ArrayList<>();
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-services-by-kind", "kind=" + kind);
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                listOfRecommendedServices = gson.fromJson(body, List.class);
            } else if (code == 404) {
                listOfRecommendedServices = null;
                System.out.println("К данному виду нет рекомендаций");
            } else {
                System.err.println("Рекомендации не загружены!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
        }
        return listOfRecommendedServices;
    }

    public List<String> loadKindsOfChar(String selectedValue) {
        List<String> listOfKinds = new ArrayList<>();
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-kinds-of-char", "char=" + selectedValue);
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                listOfKinds = gson.fromJson(body, List.class);
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
        }
        return listOfKinds;
    }

    public int confirmStage(JsonObject object, String endpoint){
        int flag = 0;
        try {
            HttpResponse response = SimpleRequestManager.sendPostRequest(endpoint, gson.toJson(object));
            int code = response.getResponseCode();
            if (code == 200) {
                flag = 1;
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
        }
        return flag;
    }

    public List<String> getServicesAutoByName(String service_name, String district_name){
        List<String> listOfAutos = new ArrayList<>();
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-auto-by-service", "service_name=" + service_name + "&" + "district_name=" + district_name);
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                listOfAutos = gson.fromJson(body, List.class);
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
        }
        return listOfAutos;
    }
}
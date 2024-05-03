package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;


import static com.example.eaclient.Controllers.WindowManager.showAlert;

public class RelationsViewController {
    @FXML
    public TreeView<String> relationsTreeView;
    @FXML
    public ChoiceBox<String> choiceBoxKinds;
    @FXML
    public ChoiceBox<String> choiceBoxServices;
    @FXML
    public TextField chosenKind;
    @FXML
    public TextField chosenService;
    @FXML
    public String kindOfService;
    @FXML
    public TextField textKindOfService;

    private final Gson gson = new Gson();

//    private void showAlert(String title, String warning, int code) {
//        Alert alert = null;
//        if (code == 1){
//            alert = new Alert(Alert.AlertType.INFORMATION);
//        } else if(code == 2){
//            alert = new Alert(Alert.AlertType.WARNING);
//        }
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(warning);
//        alert.showAndWait();
//    }

    public void initData() {
        choiceBoxServices.getItems().clear();
        choiceBoxKinds.getItems().clear();
        setUpKinds();
        setUpServices();
        HttpResponse httpResponse;
        try {
            httpResponse = SimpleRequestManager.sendGetRequest("/get-relations-list");
            int response_code = httpResponse.getResponseCode();
            if (response_code == 200) {
                String responseBody = httpResponse.getResponseBody();
                Type type = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> kindRelationsMap = gson.fromJson(responseBody, type);

                // Создаем корневой элемент для TreeView
                TreeItem<String> rootItem = new TreeItem<>("Виды чрезвычайных ситуаций");
                rootItem.setExpanded(true);

                // Для каждого вида ЧС добавляем связанные с ними сервисы в TreeView
                for (Map.Entry<String, List<String>> entry : kindRelationsMap.entrySet()) {
                    String kindName = entry.getKey();
                    List<String> services = entry.getValue();

                    TreeItem<String> kindItem = new TreeItem<>(kindName);
                    for (String service : services) {
                        TreeItem<String> serviceItem = new TreeItem<>(service);
                        kindItem.getChildren().add(serviceItem);
                    }

                    rootItem.getChildren().add(kindItem);
                }
                relationsTreeView.setRoot(rootItem);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        relationsTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem<String> parentItem = newValue.getParent();
                if (parentItem != null) {
                    if (parentItem.getParent() == null) {
                        String selectedKind = newValue.getValue();
                        chosenKind.setText(selectedKind);
                    } else {
                        String selectedService = newValue.getValue();
                        kindOfService = parentItem.getValue();
                        chosenService.setText(selectedService);
                        textKindOfService.setText(kindOfService);
                    }
                }
            }
        });
    }

    private void setUpKinds() {
        choiceBoxKinds.getItems().clear();
        List<String> listOfKinds = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/set-up-kinds");
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                listOfKinds = gson.fromJson(body, List.class);
            } else {
                System.out.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        assert listOfKinds != null;
        choiceBoxKinds.getItems().addAll(listOfKinds);
    }

    private void setUpServices() {
        choiceBoxServices.getItems().clear();
        List<String> listOfServices = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/set-up-services");
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                listOfServices = gson.fromJson(body, List.class);
            } else {
                System.out.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        assert listOfServices != null;
        choiceBoxServices.getItems().addAll(listOfServices);
    }

    public void addNewRelation(ActionEvent actionEvent) {
        String kind = choiceBoxKinds.getSelectionModel().getSelectedItem();
        String service = choiceBoxServices.getSelectionModel().getSelectedItem();
        if (choiceBoxKinds.getSelectionModel().getSelectedItem() == null) {
            showAlert("Не выбран вид ЧС!", "Выберите вид ЧС, чтобы добавить связь!", 2);
            return;
        }

        if (choiceBoxServices.getSelectionModel().getSelectedItem() == null) {
            showAlert("Не выбрана служба реагирования!", "Выберите службу реагирования на ЧС, чтобы добавить связь!", 2);
            return;
        }

        Map<String, Object> newRelationMap = new HashMap<>();
        newRelationMap.put("service_name", service);
        newRelationMap.put("kind_name", kind);
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendPostRequest("/add-new-relation", gson.toJson(newRelationMap));
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                showAlert("Успешно!", "Добавлена новая связь. Вид ЧС: " + kind + ", служба:" + service + ".", 1);
                initData();
            } else if(code == 409){
                showAlert("Попытка повторного добавления связи!", "Данная связь уже существует!", 2);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteServiceRel(ActionEvent actionEvent) {
        if (chosenService.getText() == null || Objects.equals(chosenService.getText(), "")) {
            showAlert("Не выбрана служба!", "Выберите службу, связь с которой хотите убрать!", 2);
            return;
        }

        try {
            HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-service-relation", "kind_name=" + textKindOfService.getText() + "&" + "service_name=" + chosenService.getText());
            int code = response.getResponseCode();
            if (code == 200) {
                showAlert("Успешно!", "Связь вида " + textKindOfService.getText() + " и службы " + chosenService.getText() + " была удалена.", 1);
                initData();
            } else if (code == 400) {
                showAlert("Не удалось провести удаление", "Что-то пошло не так.", 2);
            } else if (code == 404) {
                showAlert("Не удалось найти указанные данные", "Записи о связях " + textKindOfService.getText() + " не найдены.", 2);
            } else if (code == 409) {
                showAlert("Не удалось провести удаление", "Данная связь используется в других записях.", 2);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllRels(ActionEvent actionEvent) {
        if (chosenKind.getText() == null || Objects.equals(chosenKind.getText(), "")) {
            showAlert("Не выбран вид ЧС!", "Выберите вид ЧС, чтобы удалить все ", 2);
            return;
        }
    //    String kind_name = URLEncoder.encode(chosenKind.getText(), StandardCharsets.UTF_8);
        try {
            HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-all-kind-relations", "kind_name=" + chosenKind.getText());
            int code = response.getResponseCode();
            if (code == 200) {
                showAlert("Успешно!", "Связи вида ЧС " + chosenKind.getText() + " была удалена.", 1);
                initData();
            } else if (code == 400) {
                showAlert("Не удалось провести удаление", "Что-то пошло не так.", 2);
            } else if (code == 404) {
                showAlert("Не удалось найти указанные данные", "Записи о связях " + chosenKind.getText() + " не найдены.", 2);
            } else if (code == 409) {
                showAlert("Не удалось провести удаление", "Данная связь используется в других записях.", 2);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

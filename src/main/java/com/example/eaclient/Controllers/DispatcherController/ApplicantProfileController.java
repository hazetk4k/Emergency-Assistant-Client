package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Models.Applicant;
import javafx.scene.control.TextField;

public class ApplicantProfileController {
    public TextField fieldFIO;
    public TextField fieldPhone;
    public TextField fieldEmail;
    public TextField fieldHomeAddress;
    public TextField fieldWorkAddress;

    public void initData(Applicant applicant) {
        String fio = makeFIO(applicant.getName(), applicant.getSurname(), applicant.getPatronymic());
        fieldFIO.setText(fio);
        fieldPhone.setText("+375 " + applicant.getPhone_number());
        fieldEmail.setText(applicant.getEmail());
        fieldHomeAddress.setText(applicant.getHome_address());
        fieldWorkAddress.setText(applicant.getWork_address());
    }

    public static String makeFIO(String name, String surname, String patronymic) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(surname.substring(0, 1).toUpperCase()).append(surname.substring(1).toLowerCase()).append(" ")
                .append(name.substring(0, 1).toUpperCase()).append(name.substring(1).toLowerCase());

        if (patronymic != null) {
            stringBuilder.append(" ").append(patronymic.substring(0, 1).toUpperCase()).append(patronymic.substring(1).toLowerCase());
        }
        return stringBuilder.toString();
    }
}

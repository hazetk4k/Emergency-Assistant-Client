package com.example.eaclient.Models.ReportWindowModels;

public class Applicant {
    public String name;
    public String surname;
    public String patronymic;
    public String home_address;
    public String work_address;
    public String email;
    public String phone_number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }


    public String getPatronymic() {
        return patronymic;
    }

    public String getHome_address() {
        return home_address;
    }

    public String getWork_address() {
        return work_address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Applicant(String name, String surname, String patronymic, String home_address, String work_address, String email, String phone_number) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.home_address = home_address;
        this.work_address = work_address;
        this.email = email;
        this.phone_number = phone_number;
    }
}

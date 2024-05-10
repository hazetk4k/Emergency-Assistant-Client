package com.example.eaclient.Models.AdminModels;

public class TypeEm {
    public String type;
    public String recommendations;
    public String kind;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public TypeEm(String type, String recommendations, String kind) {
        this.type = type;
        this.recommendations = recommendations;
        this.kind = kind;
    }
}

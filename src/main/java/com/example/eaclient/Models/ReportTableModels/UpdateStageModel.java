package com.example.eaclient.Models.ReportTableModels;

public class UpdateStageModel {
    public int report_id;
    public String stage_name;

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public String getStage_name() {
        return stage_name;
    }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }

    public UpdateStageModel(int report_id, String stage_name) {
        this.report_id = report_id;
        this.stage_name = stage_name;
    }
}

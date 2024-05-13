package com.example.eaclient.Controllers.DispatcherController.ReportControllerPackage;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class UI_ReportManager {

    private final ReportController c;

    public UI_ReportManager(ReportController c) {
        this.c = c;
    }

    public Tooltip setToolTip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(500));
        tooltip.setShowDuration(Duration.INDEFINITE);
        return tooltip;
    }

    public void blockAllButtons() {
        //window 1
        c.charChoiceBox.setDisable(true);
        c.kindComboBox.setDisable(true);
        c.districtsChoiceBox.setDisable(true);
        c.allServicesList.setDisable(true);
        c.btnCleanServices.setDisable(true);
        //window2
        c.checkDiedInDisaster.setDisable(true);
        c.checkPeopleAmount.setDisable(true);
        c.peopleAmountField.setEditable(false);
        c.diedAmountField.setEditable(false);
        c.btnCleanNewServices.setDisable(true);
        c.otherServicesList.setDisable(true);
        //отключить все кнопки
        c.buttonStartReacting.setDisable(true);
        c.buttonConfirmServices.setDisable(true);
        c.buttonCallOtherServices.setDisable(true);
        c.buttonConfirmReceivedData.setDisable(true);
        c.buttonConfirmEndReacting.setDisable(true);
    }

    public void setTipsStageZero() {
        c.vboxConfirmStartReaction.setStyle("-fx-background-color: #aed2ca; -fx-border-color: #0cc715; -fx-border-width: 3;");
        c.buttonStartReacting.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void removeTipsStageZero() {
        c.vboxConfirmStartReaction.setStyle("-fx-background-color: #aed2ca;");
        c.buttonStartReacting.setStyle("-fx-background-color: #1e2f56;");
    }

    public void setTipsStageOne() {
        c.vboxConfirmChosenServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        c.buttonConfirmServices.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");

    }

    public void removeTipsStageOne() {
        c.vboxConfirmChosenServices.setStyle("");
        c.buttonConfirmServices.setStyle("-fx-background-color: #1e2f56;");
    }

    public void setTipsStageTwo() {
        c.buttonNextWindow.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
        c.vboxRecievedData.setStyle("-fx-background-color: #aed2ca; -fx-border-color: #0cc715; -fx-border-width: 3;");
        c.buttonConfirmReceivedData.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void removeTipsStageTwo() {
        c.buttonNextWindow.setStyle("-fx-background-color: #1e2f56;");
        c.vboxRecievedData.setStyle("-fx-background-color: #aed2ca;");
        c.buttonConfirmReceivedData.setStyle("-fx-background-color: #1e2f56;");
    }

    public void setTipsStageThree() {
        c.vboxAdditionalServicesCalling.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        c.buttonCallOtherServices.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void removeTipsStageThree() {
        c.buttonCallOtherServices.setStyle("-fx-background-color: #1e2f56;");
    }

    public void setTipsStageFour() {
        c.buttonConfirmEndReacting.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void removeTipsStageFour() {
        c.vboxAdditionalServicesCalling.setStyle("");
        c.buttonConfirmEndReacting.setStyle("-fx-background-color: #1e2f56;");
    }

    public void stageZeroBlockUnlock(){
        c.allServicesList.setDisable(true);
    }

    public void stageOneBlockUnlock(){
        // отключить старые элементы
        c.charChoiceBox.setDisable(true);
        c.buttonStartReacting.setDisable(true);
        c.kindComboBox.setDisable(true);
        c.districtsChoiceBox.setDisable(true);

        // разблокировать новое
        c.allServicesList.setDisable(false);
        c.buttonConfirmServices.setDisable(false);
    }

    public void stageTwoBlockUnlock(){
        //блокировать предыдущие действия
        c.btnCleanServices.setDisable(true);
        c.buttonConfirmServices.setDisable(true);
        c.allServicesList.setDisable(true);
        c.tableChosenServices.setDisable(true);

        c.otherServicesList.setDisable(true);
        c.tableOtherChosenServices.setDisable(true);

        //Разблокировать новое
        c.buttonNextWindow.setDisable(false);
        c.buttonConfirmServices.setDisable(true);
        c.buttonConfirmReceivedData.setDisable(false);
    }

    public void stageThreeBlockUnlock(){
        //заблокировать старый функционал
        c.peopleAmountField.setEditable(false);
        c.diedAmountField.setEditable(false);
        c.checkDiedInDisaster.setDisable(true);
        c.checkPeopleAmount.setDisable(true);
        c.buttonConfirmReceivedData.setDisable(true);

        //разблокировать новое
        c.buttonCallOtherServices.setDisable(false);
        c.otherServicesList.setDisable(false);
        c.tableOtherChosenServices.setDisable(false);
    }

    public void stageFourBlockUnlock(){
        //заблокировать старый функционал
        c.tableOtherChosenServices.setDisable(true);
        c.otherServicesList.setDisable(true);
        c.btnCleanNewServices.setDisable(true);
        c.buttonCallOtherServices.setDisable(true);

        //разблокировать новое
        c.buttonConfirmEndReacting.setDisable(false);
    }

    public void stageFiveBlockUnlock(){
        //блокировать старое
        c.buttonConfirmEndReacting.setDisable(true);

        //разблокировать новое
        c.buttonMakeReport.setDisable(false);
        c.buttonMakeReport.setVisible(true);
    }

    public void openSecondWindow(){
        c.firstWindowSupport.setVisible(false);
        c.secondWindowSupport.setVisible(true);
    }
    public void openFirstWindow(){
        c.secondWindowSupport.setVisible(false);
        c.firstWindowSupport.setVisible(true);
    }
}

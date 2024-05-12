package com.example.eaclient.Service;

import com.example.eaclient.Models.ReportWindowModels.Applicant;
import com.example.eaclient.Models.ReportWindowModels.DispChoice;
import com.example.eaclient.Models.ReportWindowModels.Report;
import com.example.eaclient.Models.ReportWindowModels.StageTimings;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordReportGenerator {

    public void generateReport(int report_id, Report report, Applicant applicant, DispChoice dispChoice) {
        StageTimings timings = getAllReportDataRequest(dispChoice.getChoice_id());

        XWPFDocument document = new XWPFDocument();

        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun = title.createRun();
        titleRun.setText("Отчет по заявлению о происшествии №" + report_id);
        titleRun.setBold(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(16);

        generateBoldParagraph(document, "Общие сведения о заявлении:");
        generateParagraph(document, "Дата и время получения заявления: " + report.getReceived_datetime() + ".");
        generateParagraph(document, "Дата и время завершения процесса приема заявления: " + timings.getEnd_time() + ".");
        generateParagraph(document, "Указанный заявителем тип происшествия: " + report.getType() + ".");
        generateParagraph(document, "Указанный адрес, где произошла ЧС: " + report.getPlace() + ", район " + dispChoice.getDistrict_name() + ".");
        generateParagraph(document, "Указанные дата и время происшествия: " + report.getTimestamp() + ". ");

        generateParagraph(document, "");

        generateBoldParagraph(document, "Персональные данные заявителя:");
        generateParagraph(document, "ФИО заявителя: " + applicant.getSurname() + " " + applicant.getName() + " " + applicant.getPatronymic() + ".");
        generateParagraph(document, "Место жительства: " + applicant.getHome_address() + ". ");
        generateParagraph(document, "Электронная почта: " + applicant.getEmail() + ". ");
        generateParagraph(document, "Номер телефона: " + applicant.getPhone_number() + ". ");
        generateParagraph(document, "");
        generateParagraph(document, "Диспетчером выдана следующая рекомендация для заявителя: ");
        generateParagraph(document, report.getRecommendations());
        generateParagraph(document, "");

        generateBoldParagraph(document, "1. Определение характеристик чрезвычайной ситуации");
        generateBoldParagraph(document, "Время начала реагирования на происшествие: " + timings.getStart_time() + ".");
        generateParagraph(document, "");
        generateParagraph(document, "Характер ситуации определен диспетчером как «ЧС " + dispChoice.getName_char() + "». ");
        generateParagraph(document, "Вид чрезвычайной ситуации определен как «" + dispChoice.getName_kind() + "». ");

        generateParagraph(document, "");

        generateBoldParagraph(document, "2. Вызов служб на место происшествия");
        generateBoldParagraph(document, "Время принятия решения: " + timings.getCall_services_time() + ".");
        generateParagraph(document, "");
        generateParagraph(document, "Диспетчером были вызваны следующие службы: ");

        String[] lines = dispChoice.getServices().split("\n");
        if (lines.length == 1) {
            for (String line : lines) {
                String[] service_auto = line.split("//");
                generateParagraph(document, " " + service_auto[0] + ", транспорт: " + service_auto[1] + ".");
            }
        } else {
            int counter = 1;
            for (String line : lines) {
                String[] service_auto = line.split("//");
                generateParagraph(document, counter + " " + service_auto[0] + ", транспорт: " + service_auto[1] + ".");
                counter++;
            }
        }

        generateParagraph(document, "");

        generateBoldParagraph(document, "Предоставление информации службам, направленным на ликвидацию чрезвычайной ситуации, посредством радиосвязи:");
        if (report.getCasualties_amount().isEmpty() || Objects.equals(report.getCasualties_amount(), "0")) {
            generateParagraph(document, "Пострадавшие: отсутствуют. ");
        } else {
            generateParagraph(document, "Пострадавшие: " + report.getCasualties_amount() + ". ");
        }
        if (report.getUser_in_danger()) {
            generateParagraph(document, "Статус заявителя: в опасности. ");
        } else {
            generateParagraph(document, "Статус заявителя: в безопасности.");
        }

        generateParagraph(document, "Также переданы дополнительные сведения от заявителя: «" + report.getAdditional_info() + "»");

        generateParagraph(document, "");

        generateBoldParagraph(document, "3. Получение данных об обстановке на месте происшествия");
        generateParagraph(document, "Время получения данных: " + timings.getReceived_data_time() + ".");
        generateParagraph(document, "");
        if (dispChoice.getPeople_amount() == 0) {
            generateParagraph(document, "На месте происшествия людей не находилось.");
        } else {
            generateParagraph(document, "Число людей, находившихся на месте происшествия: " + dispChoice.getPeople_amount() + ".");
        }
        if (dispChoice.getDead_amount() == 0) {
            generateParagraph(document, "Погибших на месте происшествия нет.");
        } else {
            generateParagraph(document, "Число погибших на месте происшествия: " + dispChoice.getDead_amount() + ".");
        }

        generateParagraph(document, "");

        generateBoldParagraph(document, "4. Принятие решения по вызову дополнительных служб");
        generateParagraph(document, "Время принятия решения: " + timings.getCall_add_services_time() + ".");
        generateParagraph(document, "");
        if (Objects.equals(dispChoice.getAdditional_services(), "") || dispChoice.getAdditional_services() == null) {
            generateParagraph(document, "Дополнительные службы не были вызваны.");
        } else {
            generateParagraph(document, "Вызваны дополнительные службы: ");
            String[] lines_additional = dispChoice.getAdditional_services().split("\n");
            if (lines_additional.length == 1) {
                for (String line : lines_additional) {
                    String[] service_auto = line.split("//");
                    generateParagraph(document, service_auto[0] + ", транспорт: " + service_auto[1] + ".");
                }
            } else {
                int i = 1;
                for (String line : lines_additional) {
                    String[] service_auto = line.split("//");
                    generateParagraph(document, i + " " + service_auto[0] + ", транспорт: " + service_auto[1] + ".");
                    i++;
                }
            }
        }

        generateParagraph(document, "");

        generateBoldParagraph(document, "5. Сообщение о ликвидации происшествия");
        generateParagraph(document, "Время окончания действий по реагированию на происшествие: " + timings.getEnd_time() + ".");

        generateParagraph(document, "");

        generateParagraph(document, "Принятие решений по реагированию на происшествие выполнял диспетчер c логином «" + dispChoice.getDispatcher_login() + "».");
        generateParagraph(document, "Отчет составлен диспетчером, владеющим учетной записью системы под логином «" + ServiceSingleton.getInstance().getCurrentUser() + "».");
        saveDocument(document, Integer.toString(report_id));
    }

    private void generateParagraph(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        paragraph.setSpacingAfterLines(0);
        run.setFontFamily("Times New Roman");
        run.setFontSize(14);
        run.setText(text);
        setParagraphProperties(paragraph);
    }

    private void generateBoldParagraph(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        // Устанавливаем текст как жирный
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(14);
        run.setText(text);
        setFirstLineIndent(paragraph);
    }

    private void setFirstLineIndent(XWPFParagraph paragraph) {
        paragraph.setIndentationFirstLine(440);
    }

    private void setParagraphProperties(XWPFParagraph paragraph) {
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        int lineSpacing = Math.round(1.08f * 20);
        paragraph.setSpacingAfterLines(lineSpacing);
    }

    private void saveDocument(XWPFDocument document, String num) {
        try (FileOutputStream out = new FileOutputStream("D:\\University\\CourseProject\\EAClient\\src\\main\\resources\\reports\\" + "Заявление №" + num + ".docx")) {
            document.write(out);
            System.out.println("Отчет успешно создан!");
        } catch (IOException e) {
            System.out.println("Ошибка при создании отчета: " + e.getMessage());
        }
    }

    private StageTimings getAllReportDataRequest(int id) {
        StageTimings timings = null;
        try {
            Gson gson = new Gson();
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-stage-timings", "choice_id=" + id);
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                timings = gson.fromJson(body, StageTimings.class);
            } else {
                System.err.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return timings;
    }
}

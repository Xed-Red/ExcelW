package com.example.excel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

/**
 * Основной класс приложения для анализа успеваемости студентов.
 * Предоставляет графический интерфейс для загрузки данных, отображения их в таблице,
 * построения диаграмм и генерации отчётов.
 */
public class StudentAnalysisApp extends Application {

    /** Логгер для записи информации о работе приложения. */
    private static final Logger logger = LogManager.getLogger(StudentAnalysisApp.class);

    /** Таблица для отображения списка студентов и их оценок. */
    private TableView<Student> tableView;

    /** Текстовая область для вывода информации об отчёте. */
    private TextArea resultArea;

    /** Столбчатая диаграмма для отображения статистики оценок. */
    private BarChart<String, Number> barChart;

    /**
     * Точка входа в приложение.
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Инициализация основного окна приложения.
     * @param primaryStage главное окно приложения.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Performance Analysis");
        initializeUI(primaryStage);

        // Заполнение диаграммы начальными значениями при запуске
        List<Student> initialStudents = getInitialStudentData();
        tableView.setItems(FXCollections.observableArrayList(initialStudents));
        updateChart(initialStudents);

        primaryStage.show();
    }

    /**
     * Инициализирует пользовательский интерфейс приложения.
     * @param primaryStage главное окно приложения.
     */
    private void initializeUI(Stage primaryStage) {
        tableView = createTableView();
        resultArea = createResultArea();
        barChart = createBarChart();

        Button openButton = createOpenButton(primaryStage);
        Button generateReportButton = createGenerateReportButton();

        ToolBar toolBar = new ToolBar(openButton, generateReportButton);

        BorderPane root = new BorderPane();
        root.setTop(toolBar);
        root.setCenter(tableView);
        root.setBottom(resultArea);
        root.setRight(barChart);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Создаёт таблицу для отображения списка студентов и их оценок.
     * @return объект TableView с настройками столбцов.
     */
    private TableView<Student> createTableView() {
        TableView<Student> tableView = new TableView<>();

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Student, Integer> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        tableView.getColumns().addAll(nameColumn, gradeColumn);
        return tableView;
    }

    /**
     * Создаёт текстовую область для вывода информации об отчёте.
     * @return объект TextArea с настройками.
     */
    private TextArea createResultArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        return textArea;
    }

    /**
     * Создаёт столбчатую диаграмму для отображения статистики оценок.
     * @return объект BarChart с настройками осей и заголовком.
     */
    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Group Statistics");
        return barChart;
    }

    /**
     * Создаёт кнопку для открытия файла.
     * @param stage главное окно приложения.
     * @return объект Button с установленным обработчиком событий.
     */
    private Button createOpenButton(Stage stage) {
        Button button = new Button("Open File");
        button.setOnAction(e -> openFile(stage));
        return button;
    }

    /**
     * Создаёт кнопку для генерации отчёта.
     * @return объект Button с установленным обработчиком событий.
     */
    private Button createGenerateReportButton() {
        Button button = new Button("Generate Report");
        button.setOnAction(e -> generateReport());
        return button;
    }

    /**
     * Обрабатывает открытие файла и загрузку данных в таблицу.
     * @param stage главное окно приложения.
     */
    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                List<Student> students = StudentParser.parse(file.getAbsolutePath());

                if (students.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "File contains no data", "Ensure the table has two columns: Name and Grade.");
                    return;
                }

                tableView.setItems(FXCollections.observableArrayList(students));
                updateChart(students);
                logger.info("File loaded successfully: " + file.getAbsolutePath());
            } catch (Exception e) {
                logger.error("Failed to load file", e);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load file", "Ensure the file is in the correct format.");
            }
        }
    }

    /**
     * Генерирует отчёт по данным студентов и сохраняет его в Excel файл.
     */
    private void generateReport() {
        List<Student> students = tableView.getItems();
        if (students.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No data to analyze", "Load a file before generating a report.");
            return;
        }

        try {
            ResultWriter.writeResults("analysis_results.xlsx", students);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Report Generated", "Results saved to 'analysis_results.xlsx'.");
            resultArea.setText("Report successfully generated and saved to 'analysis_results.xlsx'.");
            logger.info("Report generated successfully");
        } catch (Exception e) {
            logger.error("Failed to generate report", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate report", "An error occurred during report generation.");
        }
    }

    /**
     * Обновляет данные на диаграмме на основе текущих данных студентов.
     * @param students список студентов.
     */
    private void updateChart(List<Student> students) {
        int[] gradeCounts = new int[6];  // Для оценок от 1 до 5
        for (Student student : students) {
            if (student.getGrade() >= 1 && student.getGrade() <= 5) {
                gradeCounts[student.getGrade()]++;
            }
        }

        // Очистка и обновление данных на графике
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Statistics");

        // Добавляем данные по оценкам
        for (int i = 5; i >= 2; i--) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), gradeCounts[i]));
        }

        barChart.getData().add(series);
        logger.info("Chart initialized with initial data");
    }

    /**
     * Получение начальных данных для студентов (можно использовать фиктивные данные для графика).
     * @return список студентов с фиктивными данными.
     */
    private List<Student> getInitialStudentData() {
        // Создаем список студентов с начальными оценками
        return List.of(
                new Student("Student 1", 5),
                new Student("Student 2", 4),
                new Student("Student 3", 3),
                new Student("Student 4", 2),
                new Student("Student 5", 5)
        );
    }

    /**
     * Отображает всплывающее окно с указанными параметрами.
     * @param alertType тип окна предупреждения.
     * @param title заголовок окна.
     * @param header текст заголовка.
     * @param content текст сообщения.
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

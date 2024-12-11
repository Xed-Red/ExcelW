package com.example.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для записи результатов анализа успеваемости студентов в Excel файл.
 * Содержит метод для создания Excel документа, который включает статистику успеваемости
 * и список студентов с их оценками.
 */
public class ResultWriter {

    /**
     * Записывает результаты в Excel файл.
     * Создаёт лист с заголовками, статистику по оценкам, а также ФИО студентов,
     * разделённых по категориям успеваемости.
     *
     * @param filePath Путь к файлу, в который будет записан результат.
     * @param students Список студентов для анализа.
     */
    public static void writeResults(String filePath, List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Анализ успеваемости");

            // Заголовки
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Показатель");
            header.createCell(1).setCellValue("Значение");

            // Статистика
            int[] gradeCounts = new int[6];
            double totalGrades = 0;
            for (Student student : students) {
                int grade = student.getGrade();
                if (grade >= 1 && grade <= 5) {
                    gradeCounts[grade]++;
                    totalGrades += grade;
                }
            }
            double averageGrade = totalGrades / students.size();

            String[] labels = {"Отличники (5)", "Хорошисты (4)", "Троечники (3)", "Не допущены (<3)", "Средний балл"};
            Object[] values = {gradeCounts[5], gradeCounts[4], gradeCounts[3], gradeCounts[2], averageGrade};

            for (int i = 0; i < labels.length; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(labels[i]);
                row.createCell(1).setCellValue(values[i].toString());
            }

            // Добавление секции с ФИО студентов
            int rowNum = 7; // Номер строки после статистики
            String[] categories = {"Отличники (5)", "Хорошисты (4)", "Троечники (3)", "Не допущены (<3)"};

            for (int grade = 5; grade >= 2; grade--) {
                Row gradeHeaderRow = sheet.createRow(rowNum++);
                gradeHeaderRow.createCell(0).setCellValue(categories[5 - grade]);

                List<String> names = new ArrayList<>();
                int currentGrade = grade; // Создаем локальную переменную
                if (currentGrade == 2) {
                    names = students.stream()
                            .filter(s -> s.getGrade() < 3)
                            .map(Student::getFullName)
                            .toList();
                } else {
                    names = students.stream()
                            .filter(s -> s.getGrade() == currentGrade)
                            .map(Student::getFullName)
                            .toList();
                }

                for (String name : names) {
                    Row studentRow = sheet.createRow(rowNum++);
                    studentRow.createCell(1).setCellValue(name);
                }
            }

            // Сохранение файла
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

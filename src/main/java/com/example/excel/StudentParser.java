package com.example.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для парсинга Excel файла и извлечения данных о студентах.
 * Содержит метод для чтения и извлечения информации о студентах (ФИО и оценки)
 * из Excel файла.
 */
public class StudentParser {

    /**
     * Парсит данные о студентах из Excel файла.
     * Извлекает ФИО и оценки студентов из первой таблицы Excel документа.
     *
     * @param filePath Путь к Excel файлу, который нужно распарсить.
     * @return Список студентов, извлечённых из файла.
     * @throws Exception В случае ошибки при чтении файла.
     */
    public static List<Student> parse(String filePath) throws Exception {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропускаем заголовок

                Cell nameCell = row.getCell(0);
                Cell gradeCell = row.getCell(1);

                if (nameCell != null && gradeCell != null) {
                    String name = nameCell.getStringCellValue();
                    int grade = (int) gradeCell.getNumericCellValue();
                    students.add(new Student(name, grade));
                }
            }
        }
        return students;
    }
}

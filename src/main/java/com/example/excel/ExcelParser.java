package com.example.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для парсинга Excel файлов с использованием библиотеки Apache POI.
 * Применяется для извлечения данных студентов (ФИО и оценки) из Excel файла.
 */
public class ExcelParser {
    private static final Logger logger = LogManager.getLogger(ExcelParser.class);

    /**
     * Парсит Excel файл и извлекает данные о студентах (ФИО и оценки).
     *
     * @param filePath Путь к Excel файлу.
     * @return Список студентов, извлечённых из файла.
     */
    public static List<Student> parse(String filePath) {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропуск заголовка
                Cell nameCell = row.getCell(0);
                Cell gradeCell = row.getCell(1);

                if (nameCell != null && gradeCell != null) {
                    String fullName = nameCell.getStringCellValue();
                    int grade = (int) gradeCell.getNumericCellValue();
                    students.add(new Student(fullName, grade));
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка при чтении Excel файла", e);
        }
        return students;
    }
}

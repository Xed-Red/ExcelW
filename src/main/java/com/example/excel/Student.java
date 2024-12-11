package com.example.excel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

/**
 * Класс Student представляет модель данных для студента с его именем и оценкой.
 */
public class Student {

    /**
     * Полное имя студента.
     */
    private StringProperty fullName;

    /**
     * Оценка студента.
     */
    private IntegerProperty grade;

    /**
     * Конструктор для создания объекта Student.
     *
     * @param fullName полное имя студента
     * @param grade оценка студента
     */
    public Student(String fullName, int grade) {
        this.fullName = new SimpleStringProperty(fullName);
        this.grade = new SimpleIntegerProperty(grade);
    }

    /**
     * Получить полное имя студента.
     *
     * @return полное имя студента
     */
    public String getFullName() {
        return fullName.get();
    }

    /**
     * Получить свойство полного имени студента для привязки.
     *
     * @return свойство полного имени
     */
    public StringProperty getFullNameProperty() {
        return fullName;
    }

    /**
     * Получить оценку студента.
     *
     * @return оценка студента
     */
    public int getGrade() {
        return grade.get();
    }

    /**
     * Получить свойство оценки студента для привязки.
     *
     * @return свойство оценки
     */
    public IntegerProperty getGradeProperty() {
        return grade;
    }
}

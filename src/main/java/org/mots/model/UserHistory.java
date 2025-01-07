package org.mots.model;

import java.util.Date;

public class UserHistory {
    private int id; // Уникальный идентификатор
    private String userId; // ID пользователя
    //private Date userDate; // Дата урока
    private String formattedDate; // Дата урока
    private int answerCount; // Количество ответов (правильных и неправильных)

    public UserHistory() {
    }

    // Конструктор
    public UserHistory(int id, String userId, String formattedDate, int answerCount) {
        this.id = id;
        this.userId = userId;
        // this.userDate = userDate;
        this.formattedDate = formattedDate;
        this.answerCount = answerCount;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public Date getUserDate() {
//        return userDate;
//    }
//
//    public void setUserDate(Date userDate) {
//        this.userDate = userDate;
//    }



    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public void plusAnswerCount() {
        this.answerCount++;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}

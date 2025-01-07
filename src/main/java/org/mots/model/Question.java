package org.mots.model;

import java.util.List;

public class Question {
    private String mot; // Французское слово
    private List<String> options; // Варианты ответов
    private String motId; // Идентификатор слова
    private boolean visible; // Видимость слова
    private String type; // Тип слова (существительное, глагол и т.д.)
    private String gender; // Род слова (m или f)

    public Question(String mot, List<String> options, String motId, String gender, String type) {
        this.mot = mot;
        this.options = options;
        this.motId = motId; // Идентификатор слова
        this.visible = visible; // Устанавливаем видимость
        this.type = type; // Устанавливаем тип слова
        this.gender = gender; // Устанавливаем тип слова
    }

    // Геттеры и сеттеры
    public String getMot() {
        return mot;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getMotId() {
        return motId;
    }

    public void setMotId(String motId) {
        this.motId = motId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}

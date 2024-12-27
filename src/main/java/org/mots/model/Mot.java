package org.mots.model;

public class Mot {
    private String id; // Уникальный идентификатор
    private String motFrancais; // Французское слово
    private String traductionRusse; // Перевод на русский
    private String type; // Тип слова (существительное, глагол и т.д.)
    private boolean visible; // Видимость слова
    private int answerCount; // Количество ответов (правильных и неправильных)

    public Mot() {
        this.visible = true; // По умолчанию слово видно
        this.answerCount = 0;
    }



    public Mot(String id, String motFrancais, String traductionRusse, String type) {
        this.id = id;
        this.motFrancais = motFrancais;
        this.traductionRusse = traductionRusse;
        this.type = type;
        this.visible = true; // По умолчанию слово видно
        this.answerCount = 0; // Изначально количество ответов 0
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMotFrancais() {
        return motFrancais;
    }

    public void setMotFrancais(String motFrancais) {
        this.motFrancais = motFrancais;
    }

    public String getTraductionRusse() {
        return traductionRusse;
    }

    public void setTraductionRusse(String traductionRusse) {
        this.traductionRusse = traductionRusse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void incrementAnswerCount() {
        this.answerCount++;
    }

    public void decrementAnswerCount() {
        this.answerCount--;
    }
}

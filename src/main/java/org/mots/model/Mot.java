package org.mots.model;

public class Mot {
    private String id; // Уникальный идентификатор
    private String motFrancais; // Французское слово
    private String traductionRusse; // Перевод на русский
    private String type; // Тип слова (существительное, глагол и т.д.)
    private String gender; // Род слова (m или f)

    public Mot() {
    }



    public Mot(String id, String motFrancais, String traductionRusse, String type, String gender) {
        this.id = id;
        this.motFrancais = motFrancais;
        this.traductionRusse = traductionRusse;
        this.type = type;
        this.gender = gender;
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
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}

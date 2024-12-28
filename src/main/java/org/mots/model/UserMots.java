package org.mots.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserMots {
    private String id; // Уникальный идентификатор
    private String userId; // ID пользователя
    private String motId; // ID слова
    private boolean visible; // Видимость слова для пользователя
    private int answerCount; // Количество ответов (правильных и неправильных)

    // Конструктор по умолчанию (необходим для Jackson)
    public UserMots() {}

    // Конструктор с параметрами
    @JsonCreator
    public UserMots(@JsonProperty("id") String id,
                    @JsonProperty("userId") String userId,
                    @JsonProperty("motId") String motId,
                    @JsonProperty("visible") boolean visible,
                    @JsonProperty("answerCount") int answerCount) {
        this.id = id;
        this.userId = userId;
        this.motId = motId;
        this.visible = visible;
        this.answerCount = answerCount;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public void incrementAnswerCount() {
        this.answerCount++;
    }

    public void decrementAnswerCount() {
        this.answerCount--;
    }

}

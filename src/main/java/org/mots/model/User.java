package org.mots.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String id; // Уникальный идентификатор
    private String email; // Электронная почта пользователя
    private String IP; // IP-адрес пользователя
    private String nickname; // Никнейм пользователя

    // Конструктор по умолчанию (необходим для Jackson)
    public User() {}

    // Конструктор с параметрами
    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("email") String email,
                @JsonProperty("IP") String IP,
                @JsonProperty("nickname") String nickname) {
        this.id = id;
        this.email = email;
        this.IP = IP;
        this.nickname = nickname;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

package org.mots.service;

import org.mots.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<User> users = new ArrayList<>(); // Хранение пользователей в памяти

    public User registerUser(User user) {
        users.add(user); // Регистрация нового пользователя
        return user;
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Другие методы...
}

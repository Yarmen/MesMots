package org.mots.controller;

import org.mots.model.UserMots;
import org.mots.service.UserMotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-mots")
public class UserMotsController {

    @Autowired
    private UserMotsService userMotsService;

    // Добавление связи между пользователем и словом
    @PostMapping("/add")
    public ResponseEntity<UserMots> addUserMot(@RequestBody UserMots userMot) {
        userMotsService.addUserMot(userMot);
        return ResponseEntity.ok(userMot);
    }

    // Получение всех слов пользователя
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserMots>> getUserMots(@PathVariable String userId) {
        List<UserMots> userMots = userMotsService.getUserMotsByUserId(userId);
        return ResponseEntity.ok(userMots);
    }

    // Другие методы для работы с UserMots...
}

package org.mots.controller;

import org.mots.model.User;
import org.mots.service.UserMotsService;
import org.mots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private UserMotsService userMotsService;

    public UserController(UserMotsService userMotsService) {
        this.userMotsService = userMotsService;
    }



    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

   /* @GetMapping("/user/{userId}/statistics")
    public String displayStatistics(@PathVariable String userId) {
        StringBuilder statistics = new StringBuilder();

        int sessionStats = userMotsService.calculateTotalAnswers(userId);
        int todayStats = userMotsService.getTodayStats(userId);

        statistics.append("Статистика сеанса: ").append(sessionStats).append("\n");
        statistics.append("Статистика за сегодня: ").append(todayStats).append("\n");

        int totalWordsInDictionary = 250; // Замените на реальное значение
        int guessedWordsCount = 50; // Замените на реальное значение

        statistics.append("Слов в словаре: ").append(totalWordsInDictionary)
                .append(", Угадано: ").append(guessedWordsCount);

        return statistics.toString();
    }*/

}


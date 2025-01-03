package org.mots.controller;

import org.mots.service.UserMotsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final UserMotsService userMotsService;

    public StatisticsController(UserMotsService userMotsService) {
        this.userMotsService = userMotsService;
    }

    @GetMapping("/statistics/{userId}")
    public String displayStatistics(@PathVariable String userId) {
        // Здесь мы можем использовать метод displayStatistics из UserMotsService
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
    }
}

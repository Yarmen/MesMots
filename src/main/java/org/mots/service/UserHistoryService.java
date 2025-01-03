package org.mots.service;

import org.mots.model.UserHistory;
import org.mots.model.UserMots;
import org.mots.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserHistoryService {
    private List<UserHistory> userHistories = new ArrayList<>();
    private JsonUtils jsonUtils;

    private int nextId = 1; // Счетчик для уникальных идентификаторов

    public UserHistoryService() {
        this.jsonUtils = new JsonUtils();
        loadFromJson(); // Загружаем данные при создании экземпляра сервиса
        // Устанавливаем следующий ID для новых записей
        if (!userHistories.isEmpty()) {
            nextId = userHistories.stream().mapToInt(UserHistory::getId).max().orElse(0) + 1;
        }
    }

    private void loadFromJson() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/userHistory.json";
        userHistories = jsonUtils.loadFromJson(filePath, UserHistory[].class); // Загружаем данные из JSON
        System.out.println("Загружено UserHistory: " + userHistories.size());
    }

    public void saveToJson() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/userHistory.json";
        jsonUtils.saveToJson(filePath, userHistories); // Сохраняем данные в JSON
    }

    public void addUserHistory(String userId, List<UserMots> userMotsList) {
        Date todayDate = new Date(); // Получаем текущую дату

        // Подсчитываем количество строк, где answerCount > 0
        long countOfAnswers = userMotsList.stream()
                .filter(userMot -> userMot.getAnswerCount() > 0)
                .count();

        // Проверяем, существует ли запись за сегодня
        Optional<UserHistory> existingEntry = userHistories.stream()
                .filter(history -> history.getUserId().equals(userId) && isSameDay(history.getUserDate(), todayDate))
                .findFirst();

        if (existingEntry.isPresent()) {
            // Если запись существует, обновляем количество ответов
            existingEntry.get().setAnswerCount(existingEntry.get().getAnswerCount() + (int) countOfAnswers);
        } else {
            // Если записи нет, создаем новую запись с числовым ID
            UserHistory history = new UserHistory(nextId++, userId, todayDate, (int) countOfAnswers);
            userHistories.add(history);
        }

        saveToJson(); // Сохраняем изменения после добавления
    }


    public int getTodayStats(String userId) {
        Date today = new Date(); // Получаем текущую дату
        return userHistories.stream()
                .filter(history -> history.getUserId().equals(userId) && isSameDay(history.getUserDate(), today))
                .mapToInt(UserHistory::getAnswerCount)
                .sum();
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) &&
                (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}

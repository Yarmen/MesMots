package org.mots.service;

import org.mots.model.UserHistory;
import org.mots.model.UserMots;
import org.mots.utils.JsonUtils;
import org.mots.utils.PathUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        String filePath = PathUtil.getProjectDir()  + "\\src\\main\\resources\\userHistory.json";
        userHistories = jsonUtils.loadFromJson(filePath, UserHistory[].class); // Загружаем данные из JSO0System.out.println("Загружено UserHistory: " + userHistories.size());
    }

    public void saveToJson() {
        String filePath = PathUtil.getProjectDir() + "/src/main/resources/userHistory.json";
        jsonUtils.saveToJson(filePath, userHistories); // Сохраняем данные в JSON
    }

    public void addUserHistory(String userId, List<UserMots> userMotsList) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = localDate.format(formatter);
        if (userHistories.isEmpty()) {
            loadFromJson();
        }
        Optional<UserHistory> existingEntry = userHistories.stream()
                .filter(history -> {
                    String userIdValue = history.getUserId(); // Получаем userId
                    //Date userDateValue = history.getUserDate(); // Получаем userDate
                    boolean b =  isSameDayString(history.getFormattedDate(), formattedDate);
                    // Возвращаем результат фильтрации

                    return userIdValue.equals(userId) && b;
                })
                .findFirst();

        if (!existingEntry.isPresent()) {
            UserHistory history = new UserHistory(nextId++, userId, formattedDate, 0);
            userHistories.add(history);
        }// else {existingEntry.get().setAnswerCount(existingEntry.get().getAnswerCount() + (int) countOfAnswers);}

        saveToJson(); // Сохраняем изменения после добавления
    }


    public int getTodayStats(String userId) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = localDate.format(formatter);
        List<UserHistory> userHistoryFilter =userHistories.stream()
                .filter(history -> history.getUserId().equals(userId)
                        && isSameDayString(history.getFormattedDate(), formattedDate)).collect(Collectors.toList());
        UserHistory lastElement = userHistoryFilter.get(userHistoryFilter.size() - 1);

        return lastElement.getAnswerCount();

/*        return userHistories.stream()
                .filter(history -> history.getUserId().equals(userId) && isSameDay(history.getUserDate(), today))
                .mapToInt(UserHistory::getAnswerCount)
                .sum();        */
    }

    public void plusTodayStats(String userId) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = localDate.format(formatter);
        List<UserHistory> userHistoryFilter =userHistories.stream()
                .filter(history -> history.getUserId().equals(userId)
                        && isSameDayString(history.getFormattedDate(), formattedDate)).collect(Collectors.toList());
        UserHistory lastElement = userHistoryFilter.get(userHistoryFilter.size() - 1);
        lastElement.plusAnswerCount();
    }


    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) &&
                (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private boolean isSameDayString(String date1, String date2) {
        return date1.equals(date2);
    }

}

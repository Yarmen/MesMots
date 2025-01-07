package org.mots.service;

import org.mots.model.Mot;
import org.mots.model.UserMots;
import org.mots.utils.JsonUtils; // Обновленный импорт
import org.mots.utils.PathUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMotsService {
    private List<UserMots> userMotsList = new ArrayList<>(); // Хранение связей между пользователями и словами
    private MotService motService; // Сервис для работы со словами
    private JsonUtils jsonUtils; // Обновленный тип
    private UserHistoryService userHistoryService; // Добавляем сервис истории

    public UserMotsService(MotService motService) {
        this.motService = motService; // Инициализация сервиса Mot
        this.jsonUtils = new JsonUtils(); // Инициализация утилиты JSON
        this.userHistoryService = new UserHistoryService();
        loadFromJson(); // Загружаем данные при создании экземпляра сервиса
        loadMissingWords("1");
    }

    private void loadFromJson() {
        String filePath = PathUtil.getProjectDir() + "/src/main/resources/userdata.json";
        userMotsList = jsonUtils.loadFromJson(filePath, UserMots[].class); // Загружаем данные из JSON
        System.out.println("Загружено UserMots: " + userMotsList.size());
    }



    public void loadMissingWords(String userId) {
        List<Mot> allWords = motService.getAllMots(); // Получаем все слова из Mot

        // Получаем идентификаторы слов, которые уже есть у пользователя
        List<String> existingMotIds = userMotsList.stream()
                .filter(userMot -> userMot.getUserId().equals(userId))
                .map(UserMots::getMotId)
                .collect(Collectors.toList());

        // Находим недостающие слова и добавляем их в userMotsList
        for (Mot mot : allWords) {
            String motId =  mot.getId();
            if (!existingMotIds.contains(motId)) {
                UserMots newUserMot = new UserMots(motId, userId, motId, true, 0 ); // false, если слово еще не было отвечено

                userMotsList.add(newUserMot); // Добавляем новый объект в список
            }
        }
    }

    public List<Mot> getVisibleWordsForUser(String userId, List<Mot> allWords) {
        return userMotsList.stream()
                .filter(userMot -> userMot.getUserId().equals(userId) && userMot.isVisible())
                .flatMap(userMot -> allWords.stream()
                        .filter(mot -> mot.getId().equals(userMot.getMotId())))
                .collect(Collectors.toList());
    }

    public void processCorrectAnswer(String userId, String motId) {
        for (UserMots userMot : userMotsList) {
            if (userMot.getUserId().equals(userId) && userMot.getMotId().equals(motId)) {
                userMot.incrementAnswerCount();
                saveToJson();
                break;
            }
        }
        // Добавляем запись в историю после правильного ответа
        List<UserMots> currentSessionMots = getCurrentSessionMots(userId); // Получите список UserMots для текущего пользователя
        userHistoryService.addUserHistory(userId, currentSessionMots);
    }

    public void processIncorrectAnswer(String userId, String motId) {
        for (UserMots userMot : userMotsList) {
            if (userMot.getUserId().equals(userId) && userMot.getMotId().equals(motId)) {
                userMot.decrementAnswerCount(); // Уменьшаем счетчик неправильных ответов
                saveToJson(); // Сохраняем изменения
                break;
            }
        }
        // Добавляем запись в историю после правильного ответа
        List<UserMots> currentSessionMots = getCurrentSessionMots(userId); // Получите список UserMots для текущего пользователя
        userHistoryService.addUserHistory(userId, currentSessionMots);
    }

    private List<UserMots> getCurrentSessionMots(String userId) {
        return userMotsList.stream()
                .filter(userMot -> userMot.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public int getTodayStats(String userId) {
        return userHistoryService.getTodayStats(userId); // Используем метод из UserHistoryService;
    }

    public void plusTodayStats(String userId) {
        userHistoryService.plusTodayStats(userId); // Используем метод из UserHistoryService;
    }
    // Метод для подсчета общего количества ответов за сеанс (пример)
    public int calculateTotalAnswers(String userId) {

        List<UserMots> userMotsListFiltered =userMotsList.stream()
                .filter(userMot -> userMot.getUserId().equals(userId) && userMot.getAnswerCount() > 0).collect(Collectors.toList());

        return (int)userMotsListFiltered.size();
    }

    // Метод для подсчета общего количества ответов за сеанс (пример)
    public int totalMotsCount(String userId) {
        return userMotsList.size();
                //.filter(userMot -> userMot.getUserId().equals(userId) && userMot.getAnswerCount() > 0)

    }

    public void saveToJson() {
        //String userHome = System.getProperty("user.home");
        String outputFilePath = PathUtil.getProjectDir() + "/src/main/resources/userdata.json";
        //String filePath = userHome + "userdata.json"; // Путь к файлу
        jsonUtils.saveToJson(outputFilePath, userMotsList); // Сохраняем данные в JSON
    }


    public List<UserMots> getUserMotsByUserId(String userId) {
        return userMotsList.stream()
                .filter(userMot -> userMot.getUserId().equals(userId))
                .collect(Collectors.toList());
    }


}

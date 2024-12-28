package org.mots.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mots.model.Mot;
import org.mots.model.UserMots;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMotsService {
    private List<UserMots> userMotsList = new ArrayList<>(); // Хранение связей между пользователями и словами

    public void addUserMot(UserMots userMot) {
        userMotsList.add(userMot); // Добавляем связь между пользователем и словом
    }

    public UserMotsService() {
        loadFromJson(); // Загружаем данные при создании экземпляра сервиса
    }

    private void loadFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            InputStream inputStream = getClass().getResourceAsStream("/userdata.json");
            if (inputStream == null) {
                throw new FileNotFoundException("Файл userdata.json не найден.");
            }
            UserMots[] userMotsArray = objectMapper.readValue(inputStream, UserMots[].class);

            for (UserMots userMot : userMotsArray) {
                userMotsList.add(userMot); // Добавляем загруженные объекты в список
            }
            System.out.println("Загружено UserMots: " + userMotsList.size());
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке данных из userdata.json: " + e.getMessage());
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
                userMot.incrementAnswerCount(); // Увеличиваем счетчик правильных ответов
                saveToJson(); // Сохраняем изменения
                break;
            }
        }
    }

    public void processIncorrectAnswer(String userId, String motId) {
        for (UserMots userMot : userMotsList) {
            if (userMot.getUserId().equals(userId) && userMot.getMotId().equals(motId)) {
                userMot.decrementAnswerCount(); // Уменьшаем счетчик неправильных ответов
                saveToJson(); // Сохраняем изменения
                break;
            }
        }
    }

    private void saveToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Указываем путь к файлу, например, в пользовательской директории
            String userHome = System.getProperty("user.home");
            File outputFile = new File(userHome, "userdata.json"); // Сохраняем файл в домашней директории пользователя

            objectMapper.writeValue(outputFile, userMotsList);
            System.out.println("Данные успешно сохранены в " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }


    public List<UserMots> getUserMotsByUserId(String userId) {
        return userMotsList.stream()
                .filter(userMot -> userMot.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

}


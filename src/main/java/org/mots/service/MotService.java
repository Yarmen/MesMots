package org.mots.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mots.model.Mot;
import org.mots.model.Question;
import org.mots.model.User;
import org.mots.model.UserMots;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotService {
    private List<Mot> mots = new ArrayList<>(); // Инициализация списка слов

    // Конструктор для загрузки слов из JSON при инициализации сервиса
    public MotService() {
        loadFromJson(); // Загружаем слова из файла при старте приложения
    }

    // Метод для добавления нового слова
    public void addMot(Mot newMot) {
        String newId = generateUniqueId(); // Генерация уникального ID
        newMot.setId(newId); // Устанавливаем ID для нового слова
        mots.add(newMot); // Добавляем новое слово в список
        //saveToJson(); // Сохраняем список в JSON файл (если это необходимо)
    }

    // Метод для обновления существующего слова
    public void updateMot(Mot updatedMot) {
        for (int i = 0; i < mots.size(); i++) {
            if (mots.get(i).getId().equals(updatedMot.getId())) {
                mots.set(i, updatedMot); // Обновляем слово в списке
               // saveToJson(); // Сохраняем изменения в JSON файл
                return;
            }
        }
        throw new IllegalArgumentException("Слово с ID " + updatedMot.getId() + " не найдено.");
    }

    // Метод для генерации уникального идентификатора
    private String generateUniqueId() {
        if (mots.isEmpty()) {
            return "1"; // Если список пуст, начинаем с 1
        } else {
            int maxId = mots.stream()
                    .mapToInt(mot -> Integer.parseInt(mot.getId()))
                    .max()
                    .orElse(0);
            return String.valueOf(maxId + 1); // Увеличиваем максимальный ID на 1
        }
    }

    // Метод для получения случайного вопроса
    public Question getRandomQuestion() {
        // Фильтруем список, чтобы оставить только видимые слова
        List<Mot> visibleMots = mots.stream()
                .collect(Collectors.toList());

        if (visibleMots.isEmpty()) {
            throw new IllegalStateException("Нет доступных слов. Пожалуйста, добавьте слова.");
        }

        Mot randomMot = visibleMots.get((int) (Math.random() * visibleMots.size())); // Выбираем случайное слово

        List<String> options = new ArrayList<>();
        options.add(randomMot.getTraductionRusse()); // Добавляем правильный ответ

        // Генерация нескольких неправильных ответов
        while (options.size() < 5) { // Предположим, что мы хотим 4 варианта ответа
            Mot wrongMot = visibleMots.get((int) (Math.random() * visibleMots.size()));
            if (!options.contains(wrongMot.getTraductionRusse())) {
                options.add(wrongMot.getTraductionRusse());
            }
        }

        Collections.shuffle(options); // Перемешиваем варианты ответов

        return new Question(randomMot.getMotFrancais(), options, randomMot.getId(), randomMot.getGender(), randomMot.getType());
    }


    // Метод для получения всех слов
    public List<Mot> getAllMots() {
        return mots; // Возвращаем текущий список слов
    }

    // Метод для получения слова по ID
    public Mot getMotById(String id) {
        return mots.stream()
                .filter(mot -> mot.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Метод для сохранения списка слов в JSON файл
    private void loadFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Загрузка пользователей
            InputStream userInputStream = getClass().getResourceAsStream("/user.json");
            User[] users = null;
            if (userInputStream != null) {
                users = objectMapper.readValue(userInputStream, User[].class);
                System.out.println("Загружены пользователи: " + users.length);
            } else {
                System.out.println("Файл user.json не найден.");
                return; // Выход из метода, если файл не найден
            }

            // Загрузка слов
            InputStream motsInputStream = getClass().getResourceAsStream("/data.json"); // Убедитесь, что путь правильный
            Mot[] motsArray = null;
            if (motsInputStream != null) {
                motsArray = objectMapper.readValue(motsInputStream, Mot[].class);
                for (Mot mot : motsArray) {
                    mots.add(mot); // Добавляем загруженные объекты в список
                }
                System.out.println("Загружено слов: " + mots.size());
            } else {
                System.out.println("Файл data.json не найден.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Mot> getAllWords() {
        return mots; // Возвращаем список всех слов
    }


}

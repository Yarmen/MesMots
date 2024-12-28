package org.mots.controller;

import org.mots.model.Mot;
import org.mots.model.Question;
import org.mots.model.UserAnswer;
import org.mots.model.UserMots;
import org.mots.service.MotService;
import org.mots.service.UserMotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MotController {


    @Autowired
    private MotService motService; // Сервис для работы со словами

    @Autowired
    private UserMotsService userMotsService; // Сервис для работы с UserMots

    public MotController(MotService motService) {

        this.motService = motService;
        this.isInitialized = true;
    }

    private int correctGuesses = 0; // Счетчик угаданных слов

    private boolean isInitialized = false;

    public void initialize() {
        // Логика инициализации
        isInitialized = true; // Установка значения true после инициализации
    }

    @GetMapping("question-aleatoire")
    public ResponseEntity<?> getRandomQuestion(@RequestParam String userId) {
        // Получаем все UserMots для данного пользователя
        List<UserMots> userMotsList = userMotsService.getUserMotsByUserId(userId); // Предполагается, что у вас есть такой метод

        // Фильтруем и сортируем слова
        List<UserMots> filteredUserMots = userMotsList.stream()
                .filter(userMot -> userMot.isVisible()) // Фильтруем только видимые слова
                .sorted(Comparator.comparingInt(UserMots::getAnswerCount)) // Сортируем по количеству просмотров в порядке возрастания
                .collect(Collectors.toList());

        if (filteredUserMots.isEmpty()) {
            return ResponseEntity.badRequest().body("Нет доступных вопросов для данного пользователя.");
        }

        // Получаем первое слово из отсортированного списка
        UserMots selectedUserMot = filteredUserMots.get(0);
        Mot selectedMot = motService.getMotById(selectedUserMot.getMotId()); // Получаем объект Mot по ID

        // Создаем вопрос на основе выбранного слова
        Question question = createQuestionFromMot(selectedMot);

        return ResponseEntity.ok(question); // Возвращаем вопрос
    }


    // Метод для получения случайного элемента из списка
    private <T> T getRandomElement(List<T> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    // Метод для создания вопроса на основе слова
    private Question createQuestionFromMot(Mot mot) {
        // Получаем необходимые данные из объекта Mot
        String motText = mot.getMotFrancais(); // Предположим, что это текст вопроса
        List<String> options = getOptionsForMot(mot); // Метод для получения списка вариантов
        String motId = mot.getId(); // Идентификатор слова
        String type = mot.getType(); // Предположим, что у вас есть метод getType() в Mot

        // Создаем объект Question
        return new Question(motText, options, motId, type);
    }

    // Метод для получения списка вариантов для слова (пример)
    private List<String> getOptionsForMot(Mot mot) {
        List<String> options = new ArrayList<>();

        // Добавьте правильный ответ
        options.add(mot.getTraductionRusse());

        // Добавьте случайные неправильные ответы (например, из другого списка слов)
        List<Mot> allWords = motService.getAllWords(); // Получаем все слова
        Random random = new Random();

        while (options.size() < 4) { // Предположим, что вы хотите 4 варианта
            Mot randomMot = allWords.get(random.nextInt(allWords.size()));
            String wrongAnswer = randomMot.getTraductionRusse();

            // Убедитесь, что неправильный ответ не совпадает с правильным
            if (!options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }

        Collections.shuffle(options); // Перемешиваем варианты перед возвратом
        return options;
    }



    @PostMapping("/verifier-reponse")
    public ResponseEntity<String> verifierReponse(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId"); // Получаем ID пользователя
        String motId = requestBody.get("motId");
        String selectedOption = requestBody.get("selectedOption");

        Mot mot = motService.getMotById(motId);
        if (mot == null) {
            return ResponseEntity.notFound().build();
        }

        // Проверяем правильность ответа
        boolean isCorrect = mot.getTraductionRusse().equals(selectedOption);

        // Обработка правильного и неправильного ответа
        if (isCorrect) {
            correctGuesses++; // Увеличиваем счетчик угаданных слов
            userMotsService.processCorrectAnswer(userId, motId); // Увеличиваем счетчик правильных ответов
        } else {
            userMotsService.processIncorrectAnswer(userId, motId); // Уменьшаем счетчик неправильных ответов
        }

        // Проверяем, достиг ли answerCount 50 (если это необходимо)
        // Здесь вы можете добавить логику проверки видимости слова, если это актуально

        return ResponseEntity.ok(isCorrect ? "Правильно!" : "Неправильно!");
    }




    @PostMapping("/add-word")
    public String addWord(@RequestBody Mot newMot) {
        motService.addMot(newMot); // Добавляем новое слово в сервис
        return "Слово добавлено успешно!";
    }

    @PostMapping("/hide-word")
    public ResponseEntity<Void> hideWord(@RequestBody Map<String, String> requestBody) {
        String motId = requestBody.get("motId");
        Mot motToHide = motService.getMotById(motId); // Получаем слово по ID

        if (motToHide != null) {
            //motToHide.setVisible(false); // Скрываем слово
            motService.updateMot(motToHide); // Обновляем слово в списке
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("api/статистика")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok("Количество угаданных слов: " + correctGuesses);
    }



}

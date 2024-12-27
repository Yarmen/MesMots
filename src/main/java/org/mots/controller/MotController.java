package org.mots.controller;

import org.mots.model.Mot;
import org.mots.model.Question;
import org.mots.model.UserAnswer;
import org.mots.service.MotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MotController {

    private final MotService motService;

    public MotController(MotService motService) {

        this.motService = motService;
        this.isInitialized = true;
    }

    private boolean isInitialized = false;

    public void initialize() {
        // Логика инициализации
        isInitialized = true; // Установка значения true после инициализации
    }

    @GetMapping("question-aleatoire")
    public ResponseEntity<?> getRandomQuestion() {
        try {
            Question question = motService.getRandomQuestion(); // Получаем случайный вопрос
            return ResponseEntity.ok(question); // Возвращаем вопрос
        } catch (IllegalStateException e) {
            // Возвращаем ошибку 400 с сообщением
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            // Возвращаем ошибку 500 с сообщением
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сервера: " + e.getMessage());
        }
    }

    @PostMapping("/verifier-reponse")
    public ResponseEntity<String> verifierReponse(@RequestBody Map<String, String> requestBody) {
        String motId = requestBody.get("motId");
        String selectedOption = requestBody.get("selectedOption");

        Mot mot = motService.getMotById(motId);
        if (mot == null) {
            return ResponseEntity.notFound().build();
        }

        // Проверяем правильность ответа
        if (mot.getTraductionRusse().equals(selectedOption)) {
            mot.incrementAnswerCount(); // Увеличиваем счетчик ответов при правильном ответе
        } else {
            mot.decrementAnswerCount(); // Уменьшаем счетчик ответов при неправильном ответе
        }

        // Проверяем, достиг ли answerCount 50
        if (mot.getAnswerCount() >= 50) {
            mot.setVisible(false); // Скрываем слово
        }

        motService.updateMot(mot); // Обновляем слово в списке
        return ResponseEntity.ok(mot.getTraductionRusse().equals(selectedOption) ? "Правильно!" : "Неправильно!");
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
            motToHide.setVisible(false); // Скрываем слово
            motService.updateMot(motToHide); // Обновляем слово в списке
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }



}

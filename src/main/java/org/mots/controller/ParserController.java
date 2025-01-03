package org.mots.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mots.model.Mot;
import org.mots.utils.FrenchDictionaryParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@RestController
public class ParserController {

    @GetMapping("/parse-dictionary")
    public ResponseEntity<List<Mot>> parseDictionary() {
        FrenchDictionaryParser parser = new FrenchDictionaryParser();
        List<Mot> dictionaryList = parser.parseDictionary();

        // Логируем количество элементов
        System.out.println("Количество слов в словаре: " + dictionaryList.size());

        if (dictionaryList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращаем 204 No Content если массив пуст
        }

        return ResponseEntity.ok(dictionaryList); // Возвращаем 200 OK с данными
    }

    @PostMapping("/save-dictionary")
    public ResponseEntity<String> saveDictionary(@RequestBody List<Mot> dictionaryData) {
        if (dictionaryData == null || dictionaryData.isEmpty()) {
            return new ResponseEntity<>("Получены пустые данные", HttpStatus.BAD_REQUEST);
        }

        saveToFile(dictionaryData); // Вызов метода для сохранения данных
        return new ResponseEntity<>("Файл успешно сохранен", HttpStatus.OK);
    }

    private void saveToFile(List<Mot> motList) {
        // Укажите путь для сохранения файла
        String outputFilePath = System.getProperty("user.dir") + "/src/main/resources/parsed.json";

        ObjectMapper objectMapper = new ObjectMapper(); // Создаем объект ObjectMapper для сериализации

        try {
            // Сериализуем список объектов Mot в JSON и записываем в файл
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFilePath), motList);
            System.out.println("Данные успешно сохранены в файл: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при сохранении файла");
        }
    }
}



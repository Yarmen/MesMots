package org.mots.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mots.model.Mot;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrenchDictionaryParser {
    private List<Mot> existingDictionary; // Список существующих словарных записей
    private int currentId; // Текущий ID для новых слов

    public List<Mot> parseDictionary() throws FileNotFoundException {
        this.existingDictionary = loadExistingDictionary();
        this.currentId = findMaxId() + 1; // Устанавливаем текущий ID на 1 больше максимального
        List<Mot> dictionaryList = new ArrayList<>();
        String filePath = PathUtil.getProjectDir() + "\\src\\main\\resources\\dictionary.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] sexToCheck = {"m", "f"};
            while ((line = reader.readLine()) != null) {
                // Убираем лишние пробелы
                line = line.trim();
                String[] parts = line.split("\\s*,\\s*"); // Разделяем по запятой для разных форм

                // Обрабатываем каждую часть
                for (String part : parts) {
                    String[] words = part.trim().split("\\s+"); // Разделяем по пробелам

                    if (words.length >= 2) {
                        String motFrancais = words[0]; // Первое слово - французское
                        if (isWordExists(motFrancais)){
                            continue;
                        }

                        boolean isSex = Arrays.asList(sexToCheck).contains(words[1]);
                        String gender = "";
                        String traductionRusse = ""; // Перевод на русский
                        String type = ""; // Тип по умолчанию
                        if (isSex) {
                            gender = words[1]; // Второе слово - род
                            traductionRusse = words[2];
                            type = "noun";
                        }
                        else {
                            traductionRusse = words[1];
                        }
                        Mot entry = new Mot();
                        entry.setId(String.valueOf(currentId++)); // Устанавливаем ID, начиная с текущего значения и увеличиваем его
                        entry.setMotFrancais(motFrancais); // Устанавливаем французское слово
                        entry.setTraductionRusse(traductionRusse); // Устанавливаем перевод
                        entry.setType(type); // Устанавливаем тип слова
                        entry.setGender(gender); // Устанавливаем род слова
                        dictionaryList.add(entry);
                    }
                }
            }
            System.out.println("Количество элементов в списке: " + dictionaryList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dictionaryList;
    }

    private boolean isWordExists(String motFrancais) {
        for (Mot mot : existingDictionary) {
            if (mot.getMotFrancais().equalsIgnoreCase(motFrancais)) { // Сравниваем без учета регистра
                return true; // Слово найдено
            }
        }
        return false; // Слово не найдено
    }

    private int findMaxId() {
        int maxId = 0;
        for (Mot mot : existingDictionary) {
            int currentId = Integer.parseInt(mot.getId());
            if (currentId > maxId) {
                maxId = currentId; // Находим максимальный ID среди существующих слов
            }
        }
        return maxId;
    }

    private List<Mot> loadExistingDictionary() {
        String filePath = PathUtil.getProjectDir() + "/src/main/resources/data.json";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, Mot.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Возвращаем пустой список в случае ошибки
        }
    }

}

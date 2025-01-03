package org.mots.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DictionaryPopulator {

    private final WordFetcher wordFetcher = new WordFetcher();

    public Set<String> populateDictionary(String apiUrl) {
        Set<String> dictionary = new HashSet<>(); // Используем Set для уникальности

        // Извлечение слов из API
        try {
            List<String> wordsFromApi = wordFetcher.fetchWordsFromApi(apiUrl);
            dictionary.addAll(wordsFromApi);
        } catch (IOException e) {
            System.err.println("Ошибка при получении слов из API: " + e.getMessage());
        }

        return dictionary; // Возвращаем уникальные слова
    }
}

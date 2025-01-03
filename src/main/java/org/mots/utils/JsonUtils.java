package org.mots.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mots.model.UserMots;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtils { // Переименованный класс

    private final ObjectMapper objectMapper;

    public JsonUtils() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Включаем форматирование
    }

    public <T> void saveToJson(String filePath, List<T> dataList) {
        try {
            File outputFile = new File(filePath);
            objectMapper.writeValue(outputFile, dataList);
            System.out.println("Данные успешно сохранены в " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    public <T> List<T> loadFromJson(String filePath, Class<T[]> clazz) {
        try {
            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                throw new IOException("Файл не найден: " + filePath);
            }
            T[] dataArray = objectMapper.readValue(inputFile, clazz);
            return new ArrayList<>(Arrays.asList(dataArray)); // Возвращаем изменяемый список
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке данных из JSON: " + e.getMessage());
            return new ArrayList<>(); // Возвращаем пустой изменяемый список в случае ошибки
        }
    }



}

package org.mots.model;

public class UserAnswer {
    private String motId;      // Идентификатор слова
    private String selectedOption; // Выбранный вариант ответа

    public UserAnswer() {
        // Пустой конструктор необходим для десериализации JSON
    }

    public UserAnswer(String motId, String selectedOption) {
        this.motId = motId;
        this.selectedOption = selectedOption;
    }

    public String getMotId() {
        return motId;
    }

    public void setMotId(String motId) {
        this.motId = motId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
}

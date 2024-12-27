async function addWord() {
    const newWord = document.getElementById('newWord').value;
    const newTranslation = document.getElementById('newTranslation').value;

    if (!newWord || !newTranslation) {
        alert("Пожалуйста, заполните оба поля.");
        return;
    }

    const response = await fetch('/add-word', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            motFrancais: newWord,
            traductionRusse: newTranslation,
            type: 'noun', // Укажите тип слова
            visible: true // Устанавливаем видимость по умолчанию
        })
    });

    if (response.ok) {
        alert("Слово добавлено успешно!");
        document.getElementById('newWord').value = ''; // Очистка поля ввода
        document.getElementById('newTranslation').value = ''; // Очистка поля ввода
    } else {
        alert("Ошибка при добавлении слова.");
    }
}

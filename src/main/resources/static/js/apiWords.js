document.getElementById("populateButton").addEventListener("click", async function() {
    try {
        const response = await fetch('/parse-dictionary');

        if (!response.ok) {
            throw new Error('Сетевая ошибка: ' + response.statusText);
        }

        const contentType = response.headers.get("Content-Type");
        if (!contentType || !contentType.includes("application/json")) {
            throw new TypeError("Ответ не является JSON");
        }

        // Теперь мы можем получить данные JSON
        const dictionaryData = await response.json(); // Ждем, пока промис выполнится

        // Здесь вы можете проверить или использовать dictionaryData
        console.log("Полученные данные словаря:", dictionaryData);

        // Вызываем функции для отображения и сохранения данных
        //displayDictionary(dictionaryData);
        saveToServer(dictionaryData);

    } catch (error) {
        console.error('Ошибка при загрузке словаря:', error);
    }
});


function displayDictionary(dictionaryData) {
    const displayDiv = document.getElementById("dictionaryDisplay");
    displayDiv.innerHTML = ""; // Очищаем предыдущие данные

    for (const [word, translation] of Object.entries(dictionaryData)) {
        const entry = document.createElement("p");
        entry.textContent = `${word}: ${translation}`;
        displayDiv.appendChild(entry);
    }
}

function saveToServer(dictionaryData) {
    fetch('/save-dictionary', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dictionaryData) // Отправляем JSON-данные на сервер
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка при сохранении на сервере: ' + response.statusText);
        }
        return response.text(); // Возвращаем текст ответа
    })
    .then(message => {
        console.log(message); // Выводим сообщение об успешном сохранении
    })
    .catch(error => {
        console.error('Ошибка:', error);
    });
}


/*document.getElementById('populateButton').addEventListener('click', async function() {
    const apiUrl = 'https://example.com/api/words'; // Замените на ваш URL
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) {
            throw new Error('Ошибка при получении данных: ' + response.statusText);
        }

        const words = await response.json(); // Предполагается, что API возвращает массив строк
        updateDictionary(words); // Обновляем словарь с новыми словами
    } catch (error) {
        console.error('Ошибка:', error);
    }
});

function updateDictionary(words) {
    const dictionaryDisplay = document.getElementById('dictionaryDisplay');
    dictionaryDisplay.innerHTML = ''; // Очищаем текущее отображение

    words.forEach(word => {
        const wordElement = document.createElement('div');
        wordElement.textContent = word; // Добавляем каждое слово в элемент
        dictionaryDisplay.appendChild(wordElement);
    });
}*/
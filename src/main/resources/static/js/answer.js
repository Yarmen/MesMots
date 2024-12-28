async function getQuestion(userId) {
    try {
        const response = await fetch(`/question-aleatoire?userId=${userId}`); // Получаем случайный вопрос с userId
        if (!response.ok) {
            const errorMessage = await response.text(); // Получаем текст ошибки от сервера
            throw new Error(`Ошибка при получении вопроса: ${errorMessage}`); // Генерируем ошибку с текстом статуса
        }

        const data = await response.json();
        console.log("Полученные данные:", data); // Логируем ответ сервера

        // Проверяем, что данные содержат нужные поля
        if (data && data.mot && data.options && data.motId && data.type) {
            document.getElementById('question').innerText = `${data.mot} (${data.type})`; // Отображаем французское слово с типом
            const optionsContainer = document.getElementById('options');
            optionsContainer.innerHTML = ''; // Очистить предыдущие варианты

            // Добавляем кнопки для вариантов ответов
            data.options.forEach(option => {
                const button = document.createElement('button');
                button.innerText = option;
                button.onclick = () => checkAnswer(data.motId, option, button); // Передаем ID слова, выбранный вариант и кнопку
                optionsContainer.appendChild(button);
            });

            // Находим кнопку "Скрыть слово" и назначаем обработчик события
            const hideButton = document.getElementById('hideButton');
            hideButton.style.display = 'inline'; // Делаем кнопку видимой
            hideButton.onclick = () => hideWord(data.motId); // Назначаем обработчик события

            // Очищаем сообщение о результате
            document.getElementById('resultMessage').innerText = '';
        } else {
            console.error("Получены некорректные данные:", data);
            document.getElementById('question').innerText = "Ошибка: некорректные данные";
        }
    } catch (error) {
        console.error("Произошла ошибка:", error.message); // Логируем сообщение об ошибке
    }
}

async function hideWord(motId) {
    try {
        const response = await fetch('/hide-word', {  // Предполагается, что у вас есть такой API-метод на сервере
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ motId: motId })
        });

        if (response.ok) {
            console.log("Слово скрыто успешно!");
            getQuestion(userId); // Получаем новое слово после скрытия текущего, передавая userId
        } else {
            console.error("Ошибка при скрытии слова.");
        }

    } catch (error) {
        console.error("Ошибка:", error);
    }
}

async function checkAnswer(motId, selectedOption, button) {
    const userId = "1"; // Замените на актуальный ID пользователя или получите его из контекста

    try {
        const response = await fetch('/verifier-reponse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ motId: motId, selectedOption: selectedOption, userId: userId }) // Добавляем userId
        });

        if (response.ok) {
            const result = await response.text(); // Получаем текстовый ответ от сервера

            // Если ответ был правильным
            if (result === "Правильно!") {
                getQuestion(userId); // Вызываем функцию для получения нового слова
                displayStatistics(); // Обновляем статистику
            } else {
                // Если ответ неправильный, блокируем кнопку
                button.disabled = true; // Блокируем кнопку
                button.style.backgroundColor = '#ccc'; // Изменяем цвет кнопки для визуального эффекта
                button.style.cursor = 'not-allowed'; // Меняем курсор на "недоступно"
            }
        } else {
            console.error("Ошибка при проверке ответа.");
        }
    } catch (error) {
        console.error("Ошибка:", error);
    }
}


async function displayStatistics() {
    try {
        const response = await fetch('/api/статистика');
        if (response.ok) {
            const stats = await response.text();
            document.getElementById('statistics').innerText = stats; // Предполагается, что у вас есть элемент с id 'statistics'
        } else {
            console.error("Ошибка при получении статистики.");
        }
    } catch (error) {
        console.error("Ошибка:", error);
    }
}

// Вызывайте displayStatistics() после каждого правильного ответа


// Пример вызова getQuestion с userId при загрузке страницы или по событию
const userId = "1"; // Замените на актуальный ID пользователя
getQuestion(userId);

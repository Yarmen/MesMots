async function getQuestion(userId) {
    if (typeof userId == 'undefined') {
      userId = '1';
    }
    try {
        const response = await fetch(`/question-aleatoire?userId=${userId}`);
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Ошибка при получении вопроса: ${errorMessage}`);
        }

        const data = await response.json();
        console.log("Полученные данные:", data);
        if (data && data.mot && data.options && data.motId) {

            const type = data.type ? `(${data.type})` : '';
            const gender = data.gender ? `(${data.gender})` : '';
            var dataOutput='';
            // Объединяем все части вместе
            if (type && gender) {
                dataOutput += `(${data.gender}, ${data.type})`; // Если оба значения присутствуют, добавляем их через запятую
            } else if (type) {
                dataOutput += `(${data.type})`; // Если только type присутствует
            } else if (gender) {
                dataOutput += `(${data.gender})`; // Если только gender присутствует
            }
            document.getElementById('question').innerText = `${data.mot} `+ dataOutput;

            const optionsContainer = document.getElementById('options');
            optionsContainer.innerHTML = '';
            data.options.forEach(option => {
                const button = document.createElement('button');
                button.innerText = option;
                button.onclick = () => checkAnswer(data, option, button);
                optionsContainer.appendChild(button);
            });

            const hideButton = document.getElementById('hideButton');
            hideButton.style.display = 'inline';
            hideButton.onclick = () => hideWord(data.motId);
            document.getElementById('resultMessage').innerText = '';
        } else {
            console.error("Получены некорректные данные:", data);
            document.getElementById('question').innerText = "Ошибка: некорректные данные";
    }
    } catch (error) {
        console.error("Произошла ошибка:", error.message);
    }
}

async function hideWord(motId) {
    try {
        const response = await fetch('/hide-word', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ motId: motId })
        });

        if (response.ok) {
            console.log("Слово скрыто успешно!");
            getQuestion(userId);
        } else {
            console.error("Ошибка при скрытии слова.");
        }

    } catch (error) {
        console.error("Ошибка:", error);
    }
}

async function checkAnswer(data, selectedOption, button) {
    const userId = "1";
    motId=data.motId;
    try {
        const response = await fetch('/verifier-reponse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({motId: motId, selectedOption: selectedOption, userId: userId })
        });
        if (response.ok) {
            const result = await response.text();
            if (result === "Правильно!") {
                getQuestion(userId);
                //displayStatistics();
            } else {
                button.disabled = true;
                button.style.backgroundColor = '#ccc';
                button.style.cursor = 'not-allowed';
            }
        } else {
            console.error("Ошибка при проверке ответа.");
        }
    displayStatistics(userId);
    displayGuessedWord(data.mot, selectedOption);
    } catch (error) {
        console.error("Ошибка:", error);
    }
}

async function displayStatistics(userId) {
    if (typeof userId == 'undefined') {
      userId = '1';
    }
    try {
        const response = await fetch(`/statistics/${userId}`);
        if (response.ok) {
            const stats = await response.text();
            document.getElementById('statistics').innerText = stats;
        } else {
            console.error("Ошибка при получении статистики.");
        }
    } catch (error) {
        console.error("Ошибка:", error);
    }
}

function displayGuessedWord(word, translation) {
    const guessedWordDiv = document.getElementById('guessedWord');
    const wordElement = document.getElementById('word');
    const translationElement = document.getElementById('translation');

    wordElement.innerText = `${word} ${translation}`;
    guessedWordDiv.style.display = 'block'; // Показываем элемент

    // Скрываем элемент через некоторое время (например, через 3 секунды)
    /*setTimeout(() => {
        guessedWordDiv.style.display = 'none';
    }, 3000);*/
}

//displayStatistics(userId);
const userId = "1";
getQuestion(userId);


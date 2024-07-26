<b>Ссылка на сообщество с ботом:</b>

https://vk.com/club226612098
***

Бот разработан на Kotlin + Spring без использования библиотек для взаимодействия с VK Api.

Сервис успешно проходит тесты.

Также были добавлены ретраи и тесты ретраев.
***

VK_ACCESS_TOKEN берется из .env файла, который добавлен в .gitignore. Если он понадобится, то уведомите, пожалуйста.

Также из .env читается CONFIRMATION_CODE из Работа с API -> Callback API
***
<b>Для запуска проекта:</b>

1. В корневой папке проекта создать .env файл
2. Добавить в .env файл переменную VK_ACCESS_TOKEN="your_token", который можно взять в Работа с API -> Ключи доступа
3. Добавить в .env файл переменную CONFIRMATION_CODE="your_confirmation_code", который можно взять из Работа с API -> Callback API
4. С запущенным Docker из корневой папки проекта прописываем команду `docker build -t vk-bot-app .`
5. После успешной сборки также в корневой папке прописываем команду `docker run -p 8080:8080 vk-bot-app`
6. Заранее сконфигурированный ngrok запускаем командой `ngrok http 8080` (или другой порт, указаный при запуске)
7. Полученный из ngrok url вписываем в поле "Адрес" в разделе Callback API в формате ngrok_url/vk
8. Подтверждаем адрес сервера
9. Радуемся тому, что оно работает :)
***
Прямая связь со мной:

<a href="https://t.me/JabriMuhi">
    <img src="https://cdn.worldvectorlogo.com/logos/telegram-1.svg" alt="Telegram" width="80" height="80">
</a>

<a href="https://vk.com/jabrimuhi">
    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/VK_Compact_Logo_%282021-present%29.svg/768px-VK_Compact_Logo_%282021-present%29.svg.png" alt="Telegram" width="80" height="80">
</a>


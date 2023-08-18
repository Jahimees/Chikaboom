# Extra Milieux V0.2.15-ALPHA-S

<p><b>Суть проекта:</b> веб-приложение, которое позволит клиентам онлайн записываться к мастерам и салонам красоты на услуги. Мастера, в свою очередь, смогут определять предоставляемые услуги, определять свой рабочий график, вести учёт клиентов, отслеживать статистику своей работы.</p>

<p><b>Версионирование:</b></p>

<ul>
<li>x.x.0 - последняя цифра отображает внесение минимальных изменений. 1 коммит - 1 цифра. Внутреннее версионирование</li>
<li>x.0.x - вторая цифра отображает внесение целого ряда весомых изменений. Например: добавление функциональности какой-то страницы</li>
<li>0.x.x - первая цифра отражает крупные изменения. Например, релиз целого проекта или, в дальнейшем, крупные обновления</li>
</ul>
<h2>Работа с Миграцией (FlyWay)</h2>
<p>В проекте есть механизм миграций, который позволяет применять изменения к существующей БД посредством версионируемых скриптов.</p>
<p>То есть. Если при выполнении задачи одному разработчику нужно было изменить структуру БД, то он создает файл sql с необходимыми изменениями и кладет его в определенную папку.
После чего механизм миграций применяет изменения.</p>
<p>Таким образом, мы избавляемся от необходимости каждый раз пересоздавать базу данных для каждого разработчика или заставлять их вручную выполнять скрипты</p>

<b><p>Фреймворк работает следующим образом:</p></b>
<ul>
<li>Проверяет схему базы данных на наличие таблицы метаданных (по умолчанию SCHEMA_VERSION). Если таблица метаданных не существует, то создает ее.</li>
<li>Сканирует classpath на наличие доступных миграций.</li>
<li>Сравнивает миграции с таблицей метаданных. Если номер версии меньше или равен версии, помеченной как текущая, то игнорирует ее.</li>
<li>Отмечает все оставшиеся миграции как ожидающие (pending). Потом сортирует их по возрастанию номеров версий и выполняет в указанном порядке.</li>
<li>По мере применения миграций обновляет таблицу метаданных.</li>
</ul>

<h3>Как с этим работать?</h3>
<h4>При первом запуске</h4>
<ol>
<li>Удостовериться, что в build.gradle указаны верные данные подключения к базе</li>
<li>Выполнить flywayClean</li>
<li>Выполнить flywayMigrate</li>
</ol>
<h4>При необходимости произвести миграцию</h4>
<ol>
<li>Выполнить flywayMigrate</li>
</ol>
<h2>Работа с JPA</h2>
<p>Возможно, при загрузке проекта в классах-моделях будет всплывать ошибка на аннотации "Table".</p>
<p>Возникновение ошибки связано с тем, что приложение не связано ни с каким источником данных, либо он не соответствует нужному источнику</p>
<h3>Что делать?</h3>
<ol>
<li>В Intellij Idea переходим через панель инструментов View->Tool Windows->Persistence</li>
<li>Появилась вкладка Persistence. Пока оставим её</li>
<li>В правой части экрана выберем вкладку Database</li>
<li>Далее необходимо настроить наш источник данных</li>
<li>Нажимаем значок "+" вверху вкладки и выбираем MySql</li>
<li>Вводим параметры пользователя, пароля и названия БД. Нажимаем Test connection</li>
<li>После успешной проверки возвращаемся на вкладку "Persistence"</li>
<li>Нажимаем правой кнопкой по имени проекта и выбираем Assign Data Sources</li>
<li>Из выпадающего меню выбираемм только что созданный DataSource</li>

</ol>

<hr>
<h2>Обновление V0.3.0-BETA</h2>
<ul>
<li>Переработан интерфейс в соответствии с дизайном</li>
<li>Добавлен REST-API</li>
<li>Переработано хранение даты и времени записей</li>
<li>Исправлен баг, позволяющий записываться на прошлые даты</li>
<li>Добавлена возможность сортировки, поиска и фильтрации записей (в разработке)</li>
<li>Добавлена функциональность вкладки "Мои клиенты" (в разработке)</li>
</ul>
<h2>Обновление V0.2.15-ALPHA-S</h2>
<ul>
<li>Глубокий рефакторинг кода</li>
<li>Добавлена поддержка REST-API (Документация будет в следующих версиях)</li>
<li>Настроены параметры доступа к API и страницам</li>
<li>HashPasswordService окончательно удален и помещен в репозиторий памяти</li>
<li>Удалены лишние зависимости и библиотеки (-10000 строк кода)</li>
</ul>
<h2>Обновление V0.2.14-ALPHA-S</h2>
<ul>
<li>Добавлена поддержка Spring Security</li>
<li>Авторизация теперь происходит по имени пользователя</li>
<li>Переработана база данных
<ol>
<li>nickname переименован в username</li>
<li>удалено поле salt из таблицы account</li>
<li>service переименован в service_type</li>
<li>subservice переименован в service_subtype</li>
<li>userservice переименован в service</li>
</ol>
</li>
<li>Удалены все предыдущие миграции</li>
<li>Загружен новый скрипт инициализации базы данных</li>
<li>Удалены некоторые сервисы и интерфейсы за ненадобностью</li>
<li>Сервис HashPasswordService помечен как deprecated</li>
</ul>
<h2>Обновление V0.2.13-ALPHA-S</h2>
<ul>
<li>Добавлена новая вкладка "Услуги"</li>
<li>Добавлена новая вкладка "Поиск услуг"</li>
</ul>
<h2>Обновление V0.2.12-ALPHA-S</h2>
<ul>
<li>Добавлена новая вкладка "Мои записи"</li>
<li>Добавлена новая вкладка "Записи ко мне"</li>
<li>Добавлен личный кабинет клиента</li>
<li>Добавлена возможность клиенту записываться на услуги</li>
<li>Данные о записи отображаются и у клиента и у мастера + у мастера в календаре</li>
<li>Добавлена новая версия миграции flyway (9 записи) </li>
</ul>

<h2>Обновление V0.2.11-ALPHA-S</h2>
<ul>
<li>Добавлена новая вкладка "График"</li>
<li>Добавлена возможность создавать/удалять свои рабочие дни</li>
<li>Добавлена возможность устанавливать продолжительность рабочего дня</li>
<li>Добавлена новая версия миграции flyway (8 рабочие дни) </li>
</ul>

<h2>Обновление V0.2.10-ALPHA-S</h2>
<ul>
<li>Добавлена возможность загрузки фотографии аккаунта</li>
<li>Добавлена поддержка hot reload для разработчика</li>
<li>Исправлены ошибки подгрузки информации после изменений в настройках</li>
<li>Добавлена вкладка "Услуги" а также возможности создания/удаления/редактирования услуг</li>
<li>Данные о пользовательских услугах на странице мастера подгружаются из базы данных</li>
</ul>

<h2>Обновление V0.2.9-ALPHA-S</h2>
<ul>
<li>Переработан внешний вид страницы аккаунта</li>
<li>Доработана подгрузка некоторой информации на страницу аккаунта</li>
<li>Добавлены новые поля: о себе, вид деятельности, адрес</li>
<li>Подключен плагин автозаполнения адреса</li>
<li>Добавлена 6-я миграция (добавление колонки вида деятельности)</li>
</ul>

<h2>Обновление V0.2.8-ALPHA-S</h2>
<ul>
<li>Добавлена вторая активная вкладка в настройках - настроки профиля</li>
</ul>

<h2>Обновление V0.2.7-ALPHA-S</h2>
<ul>
<li>Добавлена вкладка основных настроек</li>
</ul>

<h2>Обновление V0.2.6-ALPHA-S</h2>
<ul>
<li>Переделана функция регистрации</li>
<li>Произведена чистка кода</li>
</ul>

<h2>Обновление V0.2.5-ALPHA-S</h2>
<ul>
<li>Добавлена страница личного кабинета</li>
<li>В корне переделана база данных и её модель</li>
<li>Удалены предыдущие миграции</li>
</ul>

<h2>Обновление V0.2.4-ALPHA-S</h2>
<ul>
<li>Изменение функций регистрации и авторизации</li>
<li>Чистка кода на представлениях</li>
</ul>

<h2>Обновление V0.2.3-ALPHA-S</h2>
<ul>
<li>Добавлена функция выхода из аккаунта и сброса сесии</li>
</ul>

<h2>Обновление V0.2.2-ALPHA-S</h2>
<ul>
<li>Чистка кода на пользовательской стороне</li>
</ul>

<h2>Обновление V0.2.1-ALPHA-S</h2>
<ul>
<li>Создана страница мастера/внешний вид аккаунта</li>
</ul>

<h2>Обновление V0.2.0-ALPHA-S</h2>
<ul>
<li>Конец перевода проекта на Spring</li>
<li>Добавление хэширования паролей</li>
</ul>

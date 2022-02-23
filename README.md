# Extra Milieux V0.1.4-ALPHA-S
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
<li>Удостовериться, что в pom.xml указаны верные данные подключения к базе</li>
<li>Выполнить mvn flyway:clean</li>
<li>Выполнить mvn flyway:migrate</li>
</ol>
<h4>При необходимости произвести миграцию</h4>
<ol>
<li>Выполнить mvn flyway:migrate</li>
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
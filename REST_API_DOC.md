# REST API Документация (v0.3.1.3)

<h3>Авторизация</h3>
Для начала, необходимо авторизоваться, чтобы полноценно использовать API.
<br>
<code>/login?username=user&password=pass</code>
<br>
После этого, за Вами будет сохранена сессия.

<h3>Аккаунт (Account)</h3>

<h4>Поиск:</h4>
<code>Method=GET</code>
<code>/accounts/{idAccount} permit=ALL</code> - возвращает данные указанного аккаунта. Доступ есть у всех.

<h4>Редактирование:</h4>

Изменяет только некоторые поля (переданные в теле запроса).
Изменять можно следующее поля:
<ul>
<li>Номер телефона и код (phone, phoneCode)</li>
<li>Видимость телефона на странице (phoneVisible)</li>
<li>Пароль (в этом случае обязателен параметр oldPassword (password)</li>
<li>Имя пользователя (username)</li>
<li>Адрес электронной почты (address)</li>
<li>Раздел "О себе" (about.text, about.profession, about.tags)</li>
</ul>
Поля не из списка будут игнорироваться!
Запрос:
<br>
Доступ есть только у авторизованного пользователя и только к своему аккаунту!
<br>
<code>Method=PATCH</code>
<code>localhost:8080/accounts/{idAccount}</code>
<br>
Json-тело:
<code>
{
    "password": "12345",<br>
    "oldPassword": "bbbbb",<br>
    "username": "aaaaa",<br>
    "email": "abcd@com.ru",<br>
    "about": {<br>
        "text": "Hello world",<br>
        "profession": "Hello worldist"<br>
    },<br>
    "phoneCode": {<br>
        "phoneCode": 375<br>
    },<br>
    "phone": "124124214",<br>
    "phoneVisible": false<br>
}</code><br>
Все поля опциональны. Если их не будет в запросе, то в базе останутся старые значения.

<h3>Запись (Appointment)</h3>

<h4>Поиск:</h4>
<code>Method=GET</code>
<code>/appointments permit=AUTHORIZED</code> - возвращает все возможные записи. Доступ есть у авторизованного пользователя.
<br>
<code>Method=GET</code>
<code>/appointments/{idAppointment} permit=ALL</code> - возвращает конкретную запись. Доступ есть у авторизованного пользователя.
<br>
<code>Method=GET</code>
<code>/accounts/{idAccount}/income-appointments permit=ALL</code> - возвращает входящие записи к выбранному мастеру.
<br>
<code>Method=GET</code>
<code>/accounts/{idAccount}/outcome-appointments permit=AUTHORIZED</code> - возвращает исходящие записи аккаунта.
Доступ только к своим записям

<h4>Создание:</h4>
<code>Method=POST</code>
<code>/appointments permit=AUTHORIZED</code>
<br>
Json-тело:

<code>
{
    "masterAccount": {<br>
        "idAccount": 13<br>
    },<br>
    "clientAccount": {<br>
        "idAccount": 14<br>
    },<br>
    "service": {<br>
        "idService": 6<br>
    },<br>
    "appointmentDateTime": "2023-08-27T12:00:00.000+00:00"<br>
}<br>
</code>
<p>Обратите внимание на формат даты! В базе, время хранится в нулевом часовом поясе.</p>
<p>Соответственно, если Вы находитесь в Минске (+3), то он необходимого времени отнимите 3 часа.</p>
<p>В данном примере запись на 15:00</p>

<h4>Удаление:</h4>
Удалить запись на услугу можно только в случае, если вы авторизованы и связаны с этой записью (мастер или клиент).
<br>
<code>Method=DELETE</code>
<code>/appointments/{idAppointment} permit=AUTHORIZED</code> - Удаление записи на услугу.
<br>
<code>Method=DELETE</code>
<code>/accounts/{idAccount}/outcome-appointments/{idAppointment} permit=CLIENT</code> - Удаление входящей записи на услугу. (Как клиента)
<br>
<code>Method=DELETE</code>
<code>/accounts/{idAccount}/income-appointments/{idAppointment} permit=MASTER</code> - Удаление исходящей записи на услугу. (Как мастера)

<h3>Услуги (service)</h3>
<h4>Поиск:</h4>
<code>Method=GET</code>
<code>/services permit=AUTHORIZED</code> - Получение всех услуг. Необходимо быть авторизованным
<br>
<code>Method=GET</code>
<code>/services/{idService} permit=ALL</code> - Получение конкретной услуги по id
<br>
<code>Method=GET</code>
<code>/accounts/{idAccount}/services permit=ALL</code> - Возвращает все созданные услуги мастером
<br>
<code>Method=GET</code>
<code>/service-types/{idServiceType}/service-subtypes/services permit=ALL</code> - Поиск всех услуг по перечню подтипов услуг конкретного типа услуги.
<br>

<h4>Создание:</h4>
<code>Method=POST</code>
<code>/services permit=MASTER</code> - Создание услуги.
<br>
Тело запроса:

<code>
{
    "name": "Покрытие гель-лаком", //название услуги<br>
    "price": 123.0, //Цена<br>
    "time": "1 час 30 минут", //строго x час(а) или x час(а) xx минут. Время на услугу<br>
    "account": {<br>
        "idAccount": 13 //чей аккаунт<br>
    },<br>
    "serviceSubtype": {<br>
        "idServiceSubtype": 7 //id подтипа услуги<br>
    }<br>
}
</code><br>
Все поля обязательны

<h4>Изменение:</h4>
Все параметры обязательны
<br>
<code>Method=PUT</code>
<code>/services/{idService} permit=MASTER</code> - Изменение услуги. Возможно изменить только свою услугу
<br>
Json-тело точно такое же как у создания
<br>

<h4>Удаление:</h4>
<code>Method=DELETE</code>
<code>/services/{idService} permit=MASTER</code> - Удаляет услугу. Возможно удалить только свою услугу

<h3>Подтипы услуг (serviceSubtype)</h3>
<h4>Поиск:</h4>
<code>Method=GET</code>
<code>/service-subtypes permit=ALL</code> - Получение всех подуслуг.
<br>
<code>Method=GET</code>
<code>/service-subtypes/{idServiceSubtype} permit=ALL</code> - Производит поиск подтипа услуги по её идентификатору.
<br>
<code>Method=GET</code>
<code>/service-types/{idServiceType}/service-subtypes permit=ALL</code> - Производит поиск всех подтипов услуг по идентификатору типа услуги.

<h3>Типы услуг (serviceType)</h3>
<h4>Поиск:</h4>
<code>Method=GET</code>
<code>/service-types permit=ALL</code> - Получение всех типов услуг.
<br>
<code>Method=GET</code>
<code>/service-types/{idServiceType} permit=ALL</code> - Получение типа услуги по её идентификатору.
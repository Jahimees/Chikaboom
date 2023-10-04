# REST API Документация (v0.3.1.3)

### Авторизация

Для начала, необходимо авторизоваться, чтобы полноценно использовать API.
<br>
<code>POST: /login?username=+375447635465_by&password=bbbbb</code>
<br>
После этого, за Вами будет сохранена сессия.

## Аккаунт (Account)

### Поиск <code>GET</code>:

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>
            <code>/accounts/{idAccount}</code>
        </td>
        <td>ALL</td>
        <td>
            возвращает данные указанного аккаунта.
        </td>
        <td>
            В случае, если Вы не авторизованы под аккаунт, который запрашиваете, то Вам вернётся не вся информация. 
            Также, если Вы не владелец аккаунта и у владельца установлен флаг isPhoneVisible, Вам не вернётся информация о его номере телефона
        </td>
    </tr>
    <tr>
        <td>
            <code>/accounts</code>
        </td>
        <td>ADMIN</td>
        <td>
            Возвращает данные ВСЕХ аккаунтов.
        </td>
        <td>
            Команда запрещена и невозможна к использованию
        </td>
    </tr>
</tbody>
</table>

<br>
<b> Ответ (если Вы не владелец аккаунта):</b>
<br>
<code>{
    "idAccount": 14,
    "username": "Mariska",
    "roles": [
        {
            "idRole": 1,
            "name": "ROLE_MASTER",
        },
        {
            "idRole": 2,
            "name": "ROLE_CLIENT",
        }
    ],
    "address": "Example st, 8, 123",
    "userDetails": {
        "idUserDetails": 1,
        "about": "Hello my name is Marina. I am pretty good girl",
        "socialNetwork": "tg.com/123",
        "phoneCode": {
            "idPhoneCode": 105,
            "countryName": "Латвия",
            "phoneCode": 371,
            "countryCut": "lv"
        },
        "displayedPhone": "+375 44 555 44 33",
        "firstName": "Марина",
        "lastName": "Петровна",
    },
    "accountSettings": {
        "idAccountSettings": 1,
        "defaultWorkingDayStart": "06:00:00",
        "defaultWorkingDayEnd": "15:00:00",
        "phoneVisible": false
    }
}</code>
<br>
<br>
<b> Ответ (если Вы владелец аккаунта)</b>
<br>
<code>{
    "idAccount": 23,
    "password": "$2a$10$sIgFFM/3o5q4VTKYoMFoee98h4Euh9jkr19mHI4Ig4qfFDsy6I/IK",
    "username": "Marishechka",
    "registrationDate": "2023-09-05T21:00:00.000+00:00",
    "email": "little_baby@gmail.com",
    "roles": [
        {
            "idRole": 1,
            "name": "ROLE_MASTER",
        },
        {
            "idRole": 2,
            "name": "ROLE_CLIENT",
        }
    ],
    "address": "Улица Продольная, 15",
    "userDetails": {
        "idUserDetails": 47,
        "about": "Привет, я Маришка, но не Иришка!",
        "socialNetwork": "inst@gas.com",
        "phoneCode": {
            "idPhoneCode": 20,
            "countryName": "Беларусь",
            "phoneCode": 375,
            "countryCut": "by"
        },
        "phone": "+375 44 763-54-65",
        "displayedPhone": "+375 44 763-54-65",
        "firstName": "Марина",
        "lastName": "Агафьевна",
    },
    "accountSettings": {
        "idAccountSettings": 2,
        "defaultWorkingDayStart": "03:00:00",
        "defaultWorkingDayEnd": "20:00:00",
        "phoneVisible": false
    }
}</code>

### Редактирование <code>PATCH</code>:

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>
            <code>/accounts/{idAccount}</code>
        </td>
        <td>изменяемый аккаунт == авторизованный аккаунт</td>
        <td>
            Позволяет частично изменять данные аккаунта.
        </td>
        <td>
            Некоторые поля невозможно изменить без других
        </td>
    </tr>
</tbody>
</table>

<table>
<thead>
    <td>Поле для изменения</td>
    <td>Название параметра</td>
    <td>Тип</td>
    <td>Примечание</td>
</thead>
<tbody>
    <tr>
        <td>Пароль</td>
        <td>password</td>
        <td>Строка</td>
        <td>+ поле oldPassword - необходимо ввести для подтверждения смены пароля</td>
    </tr>
    <tr>
        <td>Имя пользователя</td>
        <td>username</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Электронная почта</td>
        <td>email</td>
        <td>Строка</td>
        <td>Формат: example@ul.com</td>
    </tr>
    <tr>
        <td>Адрес</td>
        <td>address</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Текст о себе</td>
        <td>text</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Теги</td>
        <td>tags</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Вид деятельности</td>
        <td>profession</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Социальные сети</td>
        <td>socialNetwork</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Номер телефона и код страны</td>
        <td>phone + countryCut</td>
        <td>Строка</td>
        <td>При изменении одного из параметров, второй обязательно должен присутствовать</td>
    </tr>
    <tr>
        <td>Имя</td>
        <td>fistName</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Фамилия</td>
        <td>lastName</td>
        <td>Строка</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Начало рабочего дня по умолчанию</td>
        <td>defaultWorkingDayStart</td>
        <td>Строка</td>
        <td>Формат: "05:00:00"</td>
    </tr>
    <tr>
        <td>Конец рабочего дня по умолчанию</td>
        <td>defaultWorkingDayEnd</td>
        <td>Строка</td>
        <td>Формат: "18:30:00"</td>
    </tr>
    <tr>
        <td>Видимость телефона на странице аккаунта</td>
        <td>isPhoneVisible</td>
        <td>true/false</td>
        <td>true или false</td>
    </tr>
</tbody>
</table>

Поля не из списка будут игнорироваться.
Каждое поле является опциональным, так что Вы можете изменять только те поля, которые Вам необходимо.
<br>
Пример тела запроса:
<br>
<code>{"password": "MyNeWPassw0rD","oldPassword": "myOldPassword11","username": "Jakline","email": "alex@mail.com","
address": "Пушкинская 56","userDetails": {"about": {"text": "Привет, я занимаюсь профессиональной обработкой
ноготочков","tags": "маникюр,педикюр,брови","profession": "Мастер по маникюру"},"socialNetwork": null,"phoneCode": {"
countryCut": "by"},"phone": "+375441233243","firstName": "Василиса","lastName": "Прекрасная"},"accountSettings": {"
defaultWorkingDayStart": "06:00:00","defaultWorkingDayEnd": "20:00:00","phoneVisible": false }
}</code><br>

### Удаление <code>DELETE</code>:

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>
            <code>/accounts/{idAccount}</code>
        </td>
        <td>ADMIN</td>
        <td>
            Позволяет удалить аккаунт
        </td>
        <td>
            Команда запрещена и невозможна к использованию
        </td>
    </tr>
</tbody>
</table>

## Запись (Appointment)

<h4>Поиск:</h4>
<code>Method=GET</code>
<code>/appointments permit=AUTHORIZED</code> - возвращает все возможные записи. Доступ есть у авторизованного
пользователя.
<br>
<code>Method=GET</code>
<code>/appointments/{idAppointment} permit=ALL</code> - возвращает конкретную запись. Доступ есть у авторизованного
пользователя.
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
<code>/accounts/{idAccount}/outcome-appointments/{idAppointment} permit=CLIENT</code> - Удаление входящей записи на
услугу. (Как клиента)
<br>
<code>Method=DELETE</code>
<code>/accounts/{idAccount}/income-appointments/{idAppointment} permit=MASTER</code> - Удаление исходящей записи на
услугу. (Как мастера)

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
<code>/service-types/{idServiceType}/service-subtypes/services permit=ALL</code> - Поиск всех услуг по перечню подтипов
услуг конкретного типа услуги.
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
<code>/service-types/{idServiceType}/service-subtypes permit=ALL</code> - Производит поиск всех подтипов услуг по
идентификатору типа услуги.

<h3>Типы услуг (serviceType)</h3>
<h4>Поиск:</h4>
<code>Method=GET</code>
<code>/service-types permit=ALL</code> - Получение всех типов услуг.
<br>
<code>Method=GET</code>
<code>/service-types/{idServiceType} permit=ALL</code> - Получение типа услуги по её идентификатору.
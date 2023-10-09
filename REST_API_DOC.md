# REST API Документация (v0.3.1.3)

### Авторизация

Для начала, необходимо авторизоваться, чтобы полноценно использовать API.
Обратите внимание, что для авторизации используется номер телефона
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
<p><b> Ответ (если Вы владелец аккаунта)</b></p>
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
        <td>искомый аккаунт == авторизованный аккаунт</td>
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
Каждое поле является опциональным, так что Вы можете изменять только те поля, которые необходимо.
<br>
Пример тела запроса:
<br>
<code>{
"password": "MyNeWPassw0rD",
"oldPassword": "myOldPassword11",
"username": "Jakline",
"email": "alex@mail.com",
"address": "Пушкинская 56",
"userDetails": {
"about": {
"text": "Привет, я занимаюсь профессиональной обработкой ноготочков",
"tags": "маникюр,педикюр,брови",
"profession": "Мастер по маникюру"
},
"socialNetwork": "@my_inst",
"phoneCode": {
"countryCut": "by"
},
"phone": "+375441233243",
"firstName": "Василиса",
"lastName": "Прекрасная"
},
"accountSettings": {"
defaultWorkingDayStart": "06:00:00",
"defaultWorkingDayEnd": "20:00:00",
"phoneVisible": false
}
}</code>
<br>

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
<hr>

## Настройки аккаунта (AccountSettings)

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
            <code>/accounts/{idAccount}/settings</code>
        </td>
        <td>искомый аккаунт == авторизованный аккаунт</td>
        <td>
            Производит поиск настроек аккаунта
        </td>
        <td>
            -
        </td>
    </tr>
</tbody>
</table>
<p><b>Ответ</b></p>
<code>{
    "idAccountSettings": 1,
    "defaultWorkingDayStart": "09:00:00",
    "defaultWorkingDayEnd": "23:00:00",
    "phoneVisible": false
}</code>

### Редактирование <code>PATCH</code>:

Вы также можете изменять настройки напрямую, а не через REST-API аккаунта

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td><code>/accounts/{idAccount}/settings</code></td>
        <td>Искомый аккаунт == авторизованный аккаунт</td>
        <td>Позволяет частично изменять данные настроек аккаунта.</td>
        <td>-</td>
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
        <td>Логический</td>
        <td>true или false</td>
    </tr>
</tbody>
</table>

<p>Параметры являются опциональными, однако следует помнить,
что конец рабочего дня не должен быть раньше начала рабочего дня!</p>

<p><b>Пример Json-тела запроса</b></p>
<code>{
    "defaultWorkingDayStart": "10:00:00",
    "defaultWorkingDayEnd": "23:00:00",
    "phoneVisible": true
}</code>
<br>
<p><b>Ответ:</b></p>
<code>{
    "idAccountSettings": 1,
    "defaultWorkingDayStart": "10:00:00",
    "defaultWorkingDayEnd": "23:00:00",
    "phoneVisible": true
}</code>
<hr>

## Запись (Appointment)

### Поиск <code>GET</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td><code>/appointments/{idAppointment}</code></td>
        <td>Только собственные записи</td>
        <td>Производит поиск записи по её id</td>
        <td>-</td>
    </tr>
    <tr>
        <td><code>/appointments</code></td>
        <td>ADMIN</td>
        <td>Производит поиск всех возможных записей</td>
        <td>Запрещено и невозможно к использованию</td>
    </tr>
    <tr>
        <td><code>/accounts/{idAccount}/income-appointments</code></td>
        <td>ALL</td>
        <td>Производит поиск записей к определенному мастеру</td>
        <td>idAccount - идентификатор аккаунта МАСТЕРА</td>
    </tr>
    <tr>
        <td><code>/accounts/{idAccount}/outcome-appointments</code></td>
        <td>Только владелец аккаунта имеет доступ к своим записям</td>
        <td>Производит поиск записей определенного клиента</td>
        <td>idAccount - идентификатор аккаунта КЛИЕНТА</td>
    </tr>
    <tr>
        <td><code>/accounts/{idMasterAccount}/appointments?idUserDetails={idUserDetails}</code></td>
        <td>MASTER и владелец аккаунта</td>
        <td>Поиск всех записей определенного клиента к определенному мастеру</td>
        <td>-</td>
    </tr>
</tbody>
</table>

<p><b>Пример ответа json</b></p>
<code>{
    "idAppointment": 54,
    "masterAccount": {
        //информация о мастере
    },
    "service": {
        "idService": 8,
        "name": "Подпиливание",
        "price": 190.0,
        "time": "2 часа 30 минут",
        "account": {
             //данные о владельце услуги (повторяет данные мастера)
        },
        "serviceSubtype": {
            "idServiceSubtype": 1,
            "name": "Маникюр",
            "serviceType": {
                "idServiceType": 1,
                "name": "Ногтевой сервис"
            }
        }
    },
    "appointmentDateTime": "2023-09-27T08:00:00.000+00:00",
    "userDetailsClient": {
        //информация о клиенте
    }
}</code>
<p>Как видно из ответа, данные приходят полностью, без внешних ключей. С одной стороны это выглядит громоздко, 
однако с другой - мы можем получить полную информацию сразу</p>

### Создание <code>POST</code>

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
            <code>/appointments</code>
        </td>
        <td>Необходимо быть авторизованным</td>
        <td>
            Создает новую запись
        </td>
        <td>
            В теле запроса должен фигурировать Ваш аккаунт (либо как мастера, либо как клиента)
        </td>
    </tr>
</tbody>
</table>

<p><b>Пример json-тела запроса</b></p>
<code>{
    "masterAccount": {
        "idAccount": 23     
    },
    "service": {
        "idService": 13
    },
    "appointmentDateTime": "2023-10-13T15:30:00.000+03:00",
    "userDetailsClient": {
        "idUserDetails": 46        
    }
}</code>
<p>Ответ аналогичен ответу при поиске.</p>
<p>Обратите внимание на то, что необходимо знать идентификаторы аккаунта, услуги, а также деталей клиента. 
Также, стоит обратить внимание на формат даты. Если попытаться на сервер отправить дату в формате 
<code>2023-10-13T15:30:00.000+0<label style="color: red">0</label>:00
</code>, то данные на сервере сохраняться на +3 часа, поскольку сервер хранит дату и время именно в часовом поясе +3,
что необходимо учитывать при отправке запроса.</p>

### Полное изменение <code>PUT</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td><code>/appointments/{idAppointment}</code></td>
        <td>ADMIN</td>
        <td>Полностью замещает существующую запись</td>
        <td>Команда запрещена и невозможна к использованию</td>
    </tr>
</tbody>
</table>

### Удаление <code>DELETE</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/appointments/{idAppointment}</td>
        <td>Авторизован + необходимо быть участником удаляемой записи (мастером или клиентом)</td>
        <td>Производит удаление записи по id</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/accounts/{idAccount}/outcome-appointments/{idAppointment}</td>
        <td>CLIENT + необходимо быть участником удаляемой записи (клиентом)</td>
        <td>Производит удаление записи по id</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/accounts/{idAccount}/income-appointments/{idAppointment}</td>
        <td>MASTER + необходимо быть участником удаляемой записи (мастером)</td>
        <td>Производит удаление записи по id</td>
        <td>-</td>
    </tr>
</tbody>
</table>

<p>По большому счёту, разницы в указанных методах нет. Скорее всего, в будущих версиях какой-либо из методов будет упразднён.</p>

## Услуги (Service)

### Поиск <code>GET</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/services/{idService}</td>
        <td>ALL</td>
        <td>Производит поиск услуги по её id</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/accounts/{idAccountMaster}/services</td>
        <td>ALL</td>
        <td>Производит поиск всех услуг выбранного мастера</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/service-types/{idServiceType}/service-subtypes/services?serviceSubtypeIds=1,2,4,5</td>
        <td>ALL</td>
        <td>
            Поиск всех услуг по перечню подтипов услуг конкретного типа услуги. 
            Выбирается тип услуги (напр. Барбершоп), выбирается несколько подтипом (Стрижка бороды, усов) и по этим
            параметрам производится поиск созданных пользовательских услуг.
        </td>
        <td>-</td>
    </tr>
</tbody>
</table>

### Создание <code>POST</code></h4>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/services</td>
        <td>MASTER</td>
        <td>Производит создание услуги для мастера</td>
        <td>Указанный idAccount в теле запроса должен совпадать с idAccount авторизованного пользователя</td>
    </tr>
</tbody>
</table>

Тело запроса:
<code>{
"name": "Покрытие гель-лаком", //название услуги
"price": 123.0, //Цена<br>
"time": "1 час 30 минут", //строго x час(а,ов) или x час(а,ов) xx минут. Время на услугу
"account": {
"idAccount": 13 //чей аккаунт
},<br>
"serviceSubtype": {
"idServiceSubtype": 7 //id подтипа услуги
}
}
</code>
<p>Все поля обязательны</p>

### Полное изменение <code>PUT</code>

Все параметры обязательны

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/services/{idService}</td>
        <td>MASTER</td>
        <td>Производит полное изменение услуги мастера</td>
        <td>Указанный idAccount в json-теле должен совпадать с авторизованным пользователем</td>
    </tr>
</tbody>
</table>
<br>
Тело запроса:
<code>{
    "name": "Покрытие гель-лаком", //название услуги
    "price": 123.0, //Цена<br>
    "time": "1 час 30 минут", //строго x час(а,ов) или x час(а,ов) xx минут. Время на услугу
    "account": {
        "idAccount": 13 //чей аккаунт
    },<br>
    "serviceSubtype": {
        "idServiceSubtype": 7 //id подтипа услуги
    }
}</code>
<br>

### Удаление <code>DELETE</code>

<code>Method=DELETE</code>
<code>/services/{idService} permit=MASTER</code> - Удаляет услугу. Возможно удалить только свою услугу

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/services/{idService}</td>
        <td>MASTER</td>
        <td>Производит полное удаление услуги мастера</td>
        <td>Указанный сервис должен принадлежать авторизованному пользователю</td>
    </tr>
</tbody>
</table>

## Подтипы услуг (serviceSubtype)

### Поиск <code>GET</code>

<code>Method=GET</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/service-subtypes</td>
        <td>ALL</td>
        <td>Производит поиск всех возможных подтипов услуг</td>
        <td>Скорее всего, этот метод Вам не понадобится</td>
    </tr>
    <tr>
        <td>/service-subtypes/{idServiceSubtype}</td>
        <td>ALL</td>
        <td>Производит поиск конкретного подтипа услуги по его id</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/service-types/{idServiceType}/service-subtypes</td>
        <td>ALL</td>
        <td>Производит поиск всех подтипов услуг по типу услуги</td>
        <td>-</td>
    </tr>
</tbody>
</table>

## Типы услуг (serviceType)

### Поиск <code>GET</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/service-types</td>
        <td>ALL</td>
        <td>Производит поиск всех возможных типов услуг</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/service-types/{idServiceType}</td>
        <td>ALL</td>
        <td>Производит поиск конкретного типа услуги по его id</td>
        <td>-</td>
    </tr>
</tbody>
</table>

## Детали пользователя (userDetails)

### Поиск <code>GET</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/accounts/{idAccount}/clients</td>
        <td>Мастер + idAccount совпадает с авторизованным пользователем</td>
        <td>Производит поиск всей информации о пользователях, которые являются клиентом мастера</td>
        <td>-</td>
    </tr>
</tbody>
</table>

### Создание <code>POST</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/user-details</td>
        <td>MASTER</td>
        <td>Производит создание пользовательской информации (клиента без владельца. Виден только самому мастеру)</td>
        <td>-</td>
    </tr>
</tbody>
</table>

<p>Тело запроса создания:</p>
<code>{
    displayedPhone: "+375 44 123 23 23",
    firstName: "Алёша", //обязательное поле
    lastName: "Попович",
    about: {
        text: "Клиент отличается капризностью",
    },
    phoneCode: {
        countryCut: "by" //поле обязательно, если указан телефон
    },
    masterOwner: {
        idAccount: 13 //совпадает с Вашим аккаунтом, обязательное поле
    }
}</code>

### Изменение <code>PATCH</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/user-details/{idUserDetails}</td>
        <td>MASTER</td>
        <td>Производит частичное изменение выбранных полей пользовательской информации, которую ранее создавал ЭТОТ мастер</td>
        <td>-</td>
    </tr>
</tbody>
</table>


<p>Тело запроса изменения:</p>
<code>{
    displayedPhone: "+375 44 123 23 23",
    firstName: "Алёша",
    lastName: "Попович",
    about: {
        text: "Клиент отличается капризностью",
    },
    phoneCode: {
        countryCut: "by" //поле обязательно, если указан телефон
    }
}</code>

### Удаление <code>DELETE</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/accounts/{idAccount}/clients/{idUserDetails}</td>
        <td>MASTER</td>
        <td>Удаляет пользовательскую информацию о клиенте ранее созданную авторизованным мастером</td>
        <td>Невозможно удалить чужого клиента</td>
    </tr>
</tbody>
</table>

## Рабочие дни мастера (workingDays)

### Поиск <code>GET</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/accounts/{idAccount}/working-days</td>
        <td>ALL</td>
        <td>Возвращает все рабочие дни указанного мастера</td>
        <td>-</td>
    </tr>
</tbody>
</table>

### Создание <code>POST</code>

<table>
<thead>
    <td>URL</td>
    <td>Доступ</td>
    <td>Описание</td>
    <td>Дополнительная информация</td>
</thead>
<tbody>
    <tr>
        <td>/accounts/{idAccount}/working-days</td>
        <td>MASTER</td>
        <td>Создает новый рабочий день для мастера</td>
        <td>-</td>
    </tr>
</tbody>
</table>
<p>Тело запроса для создания:</p>
<code>{
    idAccount: 13,
    date: "2023-09-05T00:00:00.000+00:00",
    workingDayStart: "09:00:00", // необязательное
    workingDayEnd: "15:00:00" //необязательное
}
</code>
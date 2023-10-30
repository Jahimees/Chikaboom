<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="full-width-inline-box">
    <div class="news-data-box col-6">
        <div id="greeting-info-box" class="big-medium-black-text padding-0-0-10-px">
            Добро пожаловать,
        </div>
        <div class="smaller-medium-text">
            Отличного дня и хорошего настроения!
        </div>
        <div id="profile-filling-progress">
            <div class="diagram progress" data-percent="75.5">
                <div class="piece left"></div>
                <div class="piece right"></div>
                <div class="text">
                    <div>
                        <span id="progress-percent-placeholder">100%</span>
                    </div>
                </div>
            </div>
            <div id="profile-filling-text">
                <div class="small-black-text"><b>Прогресс заполнения</b></div>
                <div class="small-black-text">Заполненный профиль вызывает доверие клиента!</div>
            </div>
        </div>

        <div id="actual-news" style="height: 600px; overflow: auto">
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/rating-news.png" style="width: 100px; height: 100px">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Не просто цифры 30.10.2023</b>
                    <br>
                    <div>Рейтинг мастера теперь напрямую влияет на позицию в поиске!
                    </div>
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/comment-news.png" style="width: 100px; height: 100px">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Ставьте лайки, оставляйте комментарии! 27.10.2023</b>
                    <br>
                    <div>Оставляйте свои комментарии любимым мастерам и узнайте, что думают люди про ваши работы!
                        Количество комментариев, а также соотношение лайков и дизлайков влияют на рейтинг аккаунта!
                    </div>
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/favorite-news.gif" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Звёздный мастер 25.10.2023</b>
                    <br>
                    <div>Добавлен раздел "Избранное". Сохраняйте любимых мастеров и не теряйте их</div>
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/analytics-news.png" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Цифры и буквы 23.10.2023</b>
                    <br>
                    <div>Добавлен новый раздел статистики! В вашем распоряжении 3 графика с аналитикой!</div>
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/gallery-news.png" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Ого, Ваши работы неотразимы! 19.10.2023</b>
                    <br>
                    <div>Загружайте свои работы на вкладке персонализации в настройках и смотрите их на своей
                        странице!
                    </div>
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/timetable-news.svg" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Настрой свой день! 25.09.2023</b>
                    <br>
                    <div>Теперь Вы можете настраивать рабочий график каждого дня!</div>
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/tooltip-news.jpg" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Опа! Наводочка! 25.09.2023</b>
                    <br>
                    <div data-tooltip="Подсказка (всплываеть)">Добавлены всплывающие подсказки, чтобы было легче
                        ориентироваться! (тык)
                    </div>
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/phone-news.png" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Алло-алло! 30.08.2023</b>
                    <br>
                    Теперь выбор кода страны и ввод телефона стали более удобными! А авторизация происходит по номеру
                    телефона!
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/clients-news.png" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Ну что там с клиентами? 30.08.2023</b>
                    <br>
                    Появилась новая вкладка - клиенты! Посмотрите, кто ходит к вам чаще всего!
                </div>
            </div>
            <div class="news-block" style="display: inline-flex">
                <img src="../../../../image/test/name-news.png" style="width: 100px;">
                <div class="small-black-text" style="margin: auto 0 auto 20px;">
                    <b>Ура! Личности - быть! 06.09.2023</b>
                    <br>
                    Мы добавили имя и фамилию, чтобы Вас было проще узнать!
                </div>
            </div>
        </div>
    </div>
    <div class="col-6">
        <div class="menu-box-horizontal">
            <div id="general-setting-tab" class="horizontal-menu-child" selected="true">
                <div class="horizontal-menu-text"><a href="#">Основные</a></div>
            </div>
            <sec:authorize access="hasRole('ROLE_MASTER')">
                <div id="personalization-setting-tab" class="horizontal-menu-child" selected="false">
                    <div class="horizontal-menu-text"><a href="#">Персонализация</a></div>
                </div>
                <div id="security-setting-tab" class="horizontal-menu-child" selected="false">
                    <div class="horizontal-menu-text"><a href="#">Безопасность</a></div>
                </div>
                <div id="notification-setting-tab" class="horizontal-menu-child" selected="false">
                    <div class="horizontal-menu-text"><a href="#">Уведомления</a></div>
                </div>
                <div id="payment-details-setting-tab" class="horizontal-menu-child" selected="false">
                    <div class="horizontal-menu-text"><a href="#">Реквизиты</a></div>
                </div>
            </sec:authorize>
        </div>

        <div id="setting-content-placeholder" class="inner-content-placeholder">

        </div>
    </div>
</div>

<script>
    $(document).ready(() => {
        loadSettingTab("general", ${idAccount});
    })

    $("#general-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadSettingTab("general", ${idAccount});
    });

    $("#personalization-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadSettingTab("personalization", ${idAccount});
    });

    $("#security-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadUnderConstruction();
    });

    $("#notification-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadUnderConstruction();
    });

    $("#payment-details-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadUnderConstruction();
    });
</script>
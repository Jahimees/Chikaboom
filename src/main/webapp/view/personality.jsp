<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html lang="ru">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>XM. Аккаунт</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/media/common_media.css">
    <link rel="stylesheet" href="/css/account.css">
    <link rel="stylesheet" href="/css/personality.css">
    <link rel="stylesheet" href="/css/popup.css">
    <link rel="stylesheet" href="/css/media/popup_media.css">
    <link rel="stylesheet" href="/css/addition/carousel.css">
    <link rel="stylesheet" href="/css/addition/menu_bar.css">
    <link rel="stylesheet" href="/css/addition/phonecode.css"/>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
</head>
<body>
<div class="flex-box-gray">
    <div class="w-20 me-1">

        <div style="text-align: center; font-size: 3em; background-color: #5F4E7D;" id="logo">
            <a href="/chikaboom/main">
                <img style="width: 50%" src="/image/logo_gradient.svg">
            </a>
        </div>

        <div class="menu-box">
            <div id="settings-btn" class="menu-child button">
                <div><img class="small-icon" src="/image/icon/settings_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Настройки</a></div>
            </div>
            <div id="profile-btn" class="menu-child button" selected="false">
                <div><img class="small-icon" src="/image/icon/profile_icon_2.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/account/${idAccount}">Профиль</a></div>
            </div>

            <sec:authorize access="hasRole('ROLE_MASTER')">
                <div id="services-btn" class="menu-child button" selected="false">
                    <div><img class="small-icon" src="/image/icon/service_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Услуги</a></div>
                </div>
                <div id="statistic-btn" class="menu-child button" selected="false">
                    <div><img class="small-icon" src="/image/icon/statistic_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Статистика</a></div>
                </div>
                <div id="appointments-btn" class="menu-child button" selected="false">
                    <div><img class="small-icon" src="/image/icon/notebook_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Записи ко мне</a></div>
                </div>
                <div id="timetable-btn" class="menu-child button" selected="false">
                    <div><img class="small-icon" src="/image/icon/calendar_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">График</a></div>
                </div>
                <div id="clients-btn" class="menu-child button" selected="false">
                    <div><img class="small-icon" src="/image/icon/cleitns_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Мои клиенты</a></div>
                </div>
                <div id="messages-btn" class="menu-child button" selected="false">
                    <div><img class="small-icon" src="/image/icon/message_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Сообщения</a></div>
                </div>
                <div id="reviews-btn" class="menu-child button" selected="false">
                    <div><img class="small-icon" src="/image/icon/review_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Отзывы</a></div>
                </div>
            </sec:authorize>
            <div id="my-appointments-btn" class="menu-child button" selected="false">
                <div><img class="small-icon" src="/image/icon/notebook_icon_2.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Мои записи</a></div>
            </div>
            <div id="logout-btn" onclick="" class="menu-child button" selected="false">
                <div><img class="small-icon" src="/image/icon/exit_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/logout">Выйти</a></div>
            </div>
        </div>
    </div>

    <div id="content-placeholder" class="w-80 content-placeholder">

    </div>
    <script type="text/javascript" src="/js/dynamic_popup.js"></script>

    <jsp:include page="common/popup/edit_popup.jsp"/>
    <jsp:include page="common/popup/message_popup.jsp"/>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous">
</script>
</body>
</html>

<script type="text/javascript" src="/js/personality.js"></script>
<script type="text/javascript" src="/js/account.js"></script>

<script>
    var accountJson;

    function loadSettingsTab() {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/settings",
            contentType: "application/text",
            dataType: "text",
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                $("#popup-message-text")[0].innerText = "Невозможно загрузить настройки!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');
            }
        });
    }

    function loadServicesTab() {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/services",
            contentType: "application/text",
            dataType: "text",
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                $("#popup-message-text")[0].innerText = "Невозможно загрузить услуги!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');
            }
        });
    }

    function loadTimetableTab() {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/timetable",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                $("#popup-message-text")[0].innerText = "Невозможно загрузить вкладку график!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');
            }
        });
    }

    function loadAppointmentTab() {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/appointment",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                $("#popup-message-text")[0].innerText = "Невозможно загрузить вкладку записи!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');
            }
        });
    }

    function loadMyAppointmentTab() {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/myappointment",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                $("#popup-message-text")[0].innerText = "Невозможно загрузить вкладку ваших записей!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');
            }
        });
    }

    function underConstruction() {
        $.ajax({
            type: "get",
            url: "/chikaboom/under_construction",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                console.error("ERROR")
            }
        });
    }

    $(document).ready(function () {
        accountJson = loadAccount(${idAccount});
        let url = new URL(window.location.href);
        var currentTab = url.searchParams.get("tabName")
        if (currentTab === null) {
            $("#settings-btn").click();
        } else {
            $("#" + currentTab + "-btn").click();
        }
    })
</script>

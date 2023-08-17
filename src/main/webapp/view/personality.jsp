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

    <link href="https://fonts.cdnfonts.com/css/source-sans-pro" rel="stylesheet">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
</head>
<body onresize="resizeFlexBox()" style="overflow: hidden">
<div class="flex-box-purple">
    <div class="w-250-px">
        <a href="/chikaboom/main">
            <div id="home-button" class="margin-35-0-0-0-px light-small-text">
                < На сайт
            </div>
        </a>

        <a href="/chikaboom/account/${idAccount}">
            <div id="menu-photo-placeholder">
                <img class="small-avatar-image" src="/image/user/${idAccount}/avatar.jpeg"
                     onerror="this.src='/image/user/no_photo.jpg'" alt="error on load">
            </div>
        </a>

        <div class="username-placeholder light-medium-text" style="text-align: center; margin-bottom: 40px">
        </div>

        <div class="menu-box">
            <div id="settings-btn" onclick="loadSettingsTab(${idAccount}, this)" class="menu-child button">
                <div><img class="small-icon" src="/image/icon/settings_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Настройки</a></div>
            </div>
            <div id="profile-btn" class="menu-child button" selected="false">
                <div><img class="small-icon" src="/image/icon/profile_icon_2.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/account/${idAccount}">Профиль</a></div>
            </div>
            <div id="appointments-btn" onclick="loadAppointmentTab(${idAccount}, this)" class="menu-child button"
                 selected="false">
                <div><img class="small-icon" src="/image/icon/notebook_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Записи</a></div>
            </div>
            <sec:authorize access="hasRole('ROLE_MASTER')">
                <div id="services-btn" onclick="loadServicesTab(${idAccount}, this)" class="menu-child button"
                     selected="false">
                    <div><img class="small-icon" src="/image/icon/service_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Услуги</a></div>
                </div>
                <div id="statistic-btn" onclick="loadStatistic(${idAccount}, this)" class="menu-child button"
                     selected="false">
                    <div><img class="small-icon" src="/image/icon/statistic_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Статистика</a></div>
                </div>
                <div id="timetable-btn" onclick="loadTimetableTab(${idAccount}, this)" class="menu-child button"
                     selected="false">
                    <div><img class="small-icon" src="/image/icon/calendar_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">График</a></div>
                </div>
                <div id="clients-btn" onclick="loadClients(${idAccount}, this)" class="menu-child button"
                     selected="false">
                    <div><img class="small-icon" src="/image/icon/cleitns_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Мои клиенты</a></div>
                </div>
                <div id="messages-btn" onclick="loadMessages(${idAccount}, this)" class="menu-child button"
                     selected="false">
                    <div><img class="small-icon" src="/image/icon/message_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Сообщения</a></div>
                </div>
                <div id="reviews-btn" onclick="loadReviews(${idAccount}, this)" class="menu-child button"
                     selected="false">
                    <div><img class="small-icon" src="/image/icon/review_icon.svg" alt="no_image"></div>
                    <div class="menu-text"><a href="#">Отзывы</a></div>
                </div>
            </sec:authorize>
            <div id="logout-btn" class="menu-child button" selected="false">
                <div><img class="small-icon" src="/image/icon/exit_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/logout">Выйти</a></div>
            </div>
        </div>
    </div>

    <div id="content-placeholder" class="w-100 content-placeholder">

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

    $(document).ready(function () {
        accountJson = loadAccount(${idAccount});
        let url = new URL(window.location.href);
        var currentTab = url.searchParams.get("tabName")
        $(".username-placeholder").text(accountJson.username);
        if (currentTab === null) {
            $("#settings-btn").click();
        } else {
            $("#" + currentTab + "-btn").click();
        }
    })
</script>

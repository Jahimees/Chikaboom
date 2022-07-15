<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML>
<html lang="ru">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>XM. Аккаунт</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/account.css">
    <link rel="stylesheet" href="/css/addition/carousel.css">
    <link rel="stylesheet" href="/css/addition/menu_bar.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
</head>
<body>
<div class="flex-box">
    <div class="w-20 me-1">
        <jsp:include page="common/common_header.jsp"/>
        <div style="text-align: center; font-size: 3em; background-color: #5F4E7D; color: white">
            ${id}
            ЛОГО
        </div>
        <div class="menu-box">
            <div id="settings-btn" class="menu-child button" selected="true">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Настройки</a></div>
            </div>
            <div id="profile-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Профиль</a></div>
            </div>
            <div id="services-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Услуги</a></div>
            </div>
            <div id="statistic-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Статистика</a></div>
            </div>

            <div id="orders-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Записи</a></div>
            </div>
            <div id="chart-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">График</a></div>
            </div>
            <div id="clients-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Мои клиенты</a></div>
            </div>
            <div id="messages-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Сообщения</a></div>
            </div>
            <div id="reviews-btn" class="menu-child button" selected="false">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="#">Отзывы</a></div>
            </div>
        </div>
    </div>

    <div id="content-placeholder" class="w-80">

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous">
</script>
</body>
</html>

<script type="text/javascript" src="../../js/personality.js"></script>

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
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Настройки</a></div>
            </div>
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Профиль</a></div>
            </div>
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Услуги</a></div>
            </div>
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Статистика</a></div>
            </div>

            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Записи</a></div>
            </div>
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">График</a></div>
            </div>
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Мои клиенты</a></div>
            </div>
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Сообщения</a></div>
            </div>
            <div class="menu-child button">
                <div class="small-icon"><img src="/image/icon/login_icon.svg" alt="no_image"></div>
                <div class="menu-text"><a href="/chikaboom/under_construction">Отзывы</a></div>
            </div>
        </div>
    </div>
</div>
<div class="row w-80">
fa
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>
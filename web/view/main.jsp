<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Extra Milieux</title>

    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/main_media.css">
    <link rel="stylesheet" href="../css/popup.css">

    <%--    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"--%>
    <%--          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">--%>
    <link rel="stylesheet" href="../css/bootstrap/bootstrap.css">
    <link rel="script" href="../js/bootstrap/bootstrap.bundle.js">
    <%--    <script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"--%>
    <%--            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"--%>
    <%--            crossorigin="anonymous"></script>--%>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=PT+Serif&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/4be5e8b664.js" crossorigin="anonymous"></script>
</head>
<body>
<div class="content">

    <div class="header">
        <div class="menu-top">
            <div><a href="#" class="open-login-popup">▸Личный кабинет◂</a></div>
        </div>
        <div class="menu-right">
            <div class="open-login-popup"><a href="#">Вход</a></div>
            <div class="open-register-popup"><a href="#">Регистрация</a></div>
            <div><a href="https://i.ytimg.com/vi/lAqf71knZF8/maxresdefault.jpg">Быстрая запись</a></div>
            <div><a href="https://i.ytimg.com/vi/lAqf71knZF8/maxresdefault.jpg">Мои записи</a></div>
            <div><a href="https://i.ytimg.com/vi/lAqf71knZF8/maxresdefault.jpg">Избранное</a></div>
        </div>
    </div>

    <div class="service-block">
        <div class="service-block-name">
            <div>Запишитесь на самые</div>
            <div>популярные бьюти услуги</div>
        </div>
        <div class="service-block-all row">
            <c:set var="counter" value="1"/>
            <c:forTokens var="i"
                         items="Визаж/Брови,Парикмахерские услуги,Ресницы,Татуаж/Тату,Ногтевой сервис,Косметология/Уход за телом"
                         delims=",">
                <div class="col-xl-4 service-outer-image">
                    <a style="display: inline-flex" href="https://i.ytimg.com/vi/lAqf71knZF8/maxresdefault.jpg">
                        <img class="service-image" src="../image/service/service_icon_${counter}.png">
                        <div class="service-item">
                            <p><c:out value="${i}"/></p>
                        </div>
                    </a>
                </div>
                <c:set var="counter" value="${counter + 1}"/>
            </c:forTokens>
        </div>

        <jsp:include page="common/login_popup.jsp"/>
        <jsp:include page="common/register_popup.jsp"/>

        <div style="display: flex">
            <a class="service-block-button" href="https://i.ytimg.com/vi/lAqf71knZF8/maxresdefault.jpg">ВСЕ УСЛУГИ</a>
        </div>

    </div>
    <div class="block-map">
        <div class="img-block"><img src="../image/map.png"></div>
        <div class="block-big-part">
            <div class="block-title">Найдите мастера близко к дому</div>
            <div class="block-text">Все мастера Минска на одной карте!</div>
            <div class="service-block-button">СМОТРЕТЬ</div>
        </div>
    </div>

    <div class="block-map">
        <div class="block-big-part">
            <div class="block-title">Обучение у лучших мастеров Минска</div>
            <div class="block-text">Не пропустите ни один курс или мастер-класс!</div>
            <div class="service-block-button">УЗНАТЬ БОЛЬШЕ</div>
        </div>
        <div class="img-block"><img src="../image/course.png"></div>
    </div>

    <div class="block-title">Топ-мастера в городе Минск</div>
    <div class="top_masters_block">
        <img src="../image/top_master_template.png" class="top_master_block"/>
        <img src="../image/top_master_template.png" class="top_master_block"/>
    </div>

</div>
</body>
</html>

<script type="text/javascript" src="../js/popup.js"></script>
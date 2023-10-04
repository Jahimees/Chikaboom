<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>XM. Услуги</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/css/intlTelInput.css">
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/media/common_media.css">
    <link rel="stylesheet" href="../css/account.css">
    <link rel="stylesheet" href="../css/personality.css">
    <link rel="stylesheet" href="../css/popup.css">
    <link rel="stylesheet" href="../css/media/popup_media.css">
    <link rel="stylesheet" href="../css/addition/menu_bar.css">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/media/main_media.css">
    <link rel="stylesheet" href="../css/popup.css">

    <link href="https://fonts.cdnfonts.com/css/source-sans-pro" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/js/intlTelInput.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script rel="script" src="../js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="../../../js/static_popup.js"></script>

</head>
<body>
<jsp:include page="common/common_header.jsp"/>
<div class="big-text padding-0-5">
    Услуги
</div>
<div class="medium-text padding-0-5 full-width-inline-box">
    <a href="/chikaboom/main" style="text-decoration: none; color: #523870">Главная</a>
    <div style="padding: 0 15px">></div>
    <a href="/chikaboom/service" style="text-decoration: none; color: #523870">Услуги</a>
</div>
<hr>
<div class="service-type-block-all row">
    <c:set var="counter" value="1"/>
    <c:forTokens var="i"
                 items="Ногтевой сервис,Парикмахерские услуги,Ресницы,Брови,Визаж,Депиляция/Эпиляция,Барбершоп,Косметология/Уход за телом,Тату/татуаж,Массаж"
                 delims=",">
        <div class="col-xl-4 service-type-outer-image">
            <a style="display: inline-flex" href="/chikaboom/service/search/${counter}">
                <img class="service-type-image" src="../image/serviceType/service_icon_${counter}.png" alt="no_image">
                <div class="service-type-item">
                    <p><c:out value="${i}"/></p>
                </div>
            </a>
        </div>
        <c:set var="counter" value="${counter + 1}"/>
    </c:forTokens>
    <jsp:include page="common/popup/message_popup.jsp"/>
    <jsp:include page="common/popup/login_popup.jsp"/>
</div>
</body>
</html>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>

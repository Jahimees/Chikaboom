<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html lang="ru">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Extra Milieux</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/media/main_media.css">
    <link rel="stylesheet" href="../css/popup.css">
    <link rel="stylesheet" href="../css/media/popup_media.css">
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/css/intlTelInput.css">

    <link href="https://fonts.cdnfonts.com/css/source-sans-pro" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/js/intlTelInput.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script rel="script" src="../js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="../js/countrycode/countryCode.js"></script>
</head>
<body>
<div class="content">
    <div class="main-header">
        <div class="menu-top">
            <sec:authorize access="isAuthenticated()">
                <div><a href="/chikaboom/personality/<sec:authentication property="principal.idAccount"/>">▸Личный
                    кабинет◂</a></div>
            </sec:authorize>
        </div>
        <div class="menu-right">
            <sec:authorize access="!isAuthenticated()">
                <div data-bs-toggle="modal" style="cursor: pointer" data-bs-target="#loginModal">
                    Вход
                </div>
                <div data-bs-toggle="modal" style="cursor: pointer" data-bs-target="#registerModal">
                    Регистрация
                </div>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <div>
                    <a href="/chikaboom/personality/<sec:authentication property="principal.idAccount"/>?tabName=my-appointments">Мои
                        записи</a></div>
                <div><a href="/logout">Выйти из аккаунта</a></div>
                <div><a href="/chikaboom/under_construction">Избранное</a></div>
            </sec:authorize>
        </div>
    </div>

    <div class="service-type-block">
        <div class="service-type-block-name">
            <div>Запишитесь на самые</div>
            <div>популярные бьюти услуги</div>
        </div>
        <div class="service-type-block-all row">
            <c:set var="counter" value="1"/>
            <c:forTokens var="i"
                         items="Ногтевой сервис,Парикмахерские услуги,Ресницы,Брови,Визаж,Депиляция/эпиляция"
                         delims=",">
                <div class="col-xl-4 service-type-outer-image">
                    <a style="display: inline-flex" href="/chikaboom/service/search/${counter}">
                        <img class="service-type-image" src="../image/serviceType/service_icon_${counter}.png"
                             alt="no_image">
                        <div class="service-type-item">
                            <p><c:out value="${i}"/></p>
                        </div>
                    </a>
                </div>
                <c:set var="counter" value="${counter + 1}"/>
            </c:forTokens>
        </div>

        <div class="flex-box-neutral">
            <a class="service-type-block-button" href="/chikaboom/service">ВСЕ УСЛУГИ</a>
        </div>

    </div>
    <div class="block-map">
        <div class="img-block"><img src="../image/map.png" alt="no_image"></div>
        <div class="block-big-part">
            <div class="block-title">Найдите мастера близко к дому</div>
            <div class="block-text">Все мастера Минска на одной карте!</div>
            <div class="service-type-block-button"><a href="/chikaboom/under_construction">СМОТРЕТЬ</a></div>
        </div>
    </div>

    <div class="block-map">
        <div class="block-big-part">
            <div class="block-title">Обучение у лучших мастеров Минска</div>
            <div class="block-text">Не пропустите ни один курс или мастер-класс!</div>
            <div class="service-type-block-button"><a href="/chikaboom/under_construction">УЗНАТЬ БОЛЬШЕ</a></div>
        </div>
        <div class="img-block"><img src="../image/course.png" alt="no_image"></div>
    </div>

    <div class="block-title">Топ-мастера в городе Минск</div>
    <div id="topMastersCarousel" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-indicators">
            <button type="button" data-bs-target="#topMastersCarousel" data-bs-slide-to="0" class="active"
                    aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#topMastersCarousel" data-bs-slide-to="1"
                    aria-label="Slide 2"></button>
            <button type="button" data-bs-target="#topMastersCarousel" data-bs-slide-to="2"
                    aria-label="Slide 3"></button>
        </div>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <div class="w-flex_mrgn-2">
                    <img src="../image/top_master_template.png" class="d-block top_master_block" alt="...">
                    <img src="../image/top_master_template.png" class="d-block top_master_block" alt="...">
                </div>
            </div>
            <c:forEach var="x" begin="0" end="1" step="1">
                <div class="carousel-item">
                    <div class="w-flex_mrgn-2">
                        <img src="../image/top_master_template.png" class="d-block top_master_block" alt="...">
                        <img src="../image/top_master_template.png" class="d-block top_master_block" alt="...">
                    </div>
                </div>
            </c:forEach>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#topMastersCarousel"
                data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Предыдущий</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#topMastersCarousel"
                data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Следующий</span>
        </button>
    </div>
    <div class="block-title">Записаться к мастеру стало ещё проще!</div>
    <div class="block-text w-flex_mrgn-2">
        <ol class="padding-0-5">
            <li>Большой выбор мастеров по различным услугам</li>
            <li>Простой и удобный способ записи</li>
        </ol>
        <ol class="padding-0-5">
            <li>Смс-оповещения о предстоящем визите</li>
            <li>Возможность оставить отзыв</li>
        </ol>
    </div>

    <iframe class="yt-video" src="https://www.youtube.com/embed/dQw4w9WgXcQ"
            title="YouTube video player"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen>
    </iframe>

    <jsp:include page="common/popup/login_popup.jsp"/>
    <jsp:include page="common/popup/register_popup.jsp"/>
    <jsp:include page="common/popup/message_popup.jsp"/>
    <jsp:include page="common/footer.jsp"/>
</div>

</body>
</html>

<script type="text/javascript" src="../js/dynamic_popup.js"></script>

<script type="text/javascript" src="../js/static_popup.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>

<script>
    $(document).ready(() => {
        var hash = window.location.hash;

        if (hash == '#login') {
            $("#loginModal").modal('show');
        }

        if (hash == '#register') {
            $("#registerModal").modal('show');
        }
    });
</script>

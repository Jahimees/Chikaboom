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
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/account.css">
    <link rel="stylesheet" href="../css/addition/carousel.css">
    <link rel="stylesheet" href="../css/addition/menu_bar.css">
</head>
<body>
<c:set var="session" value="${id}"/>

<jsp:include page="common/common_header.jsp"/>
<div class="flex-box">
    <c:if test="${empty id}">
        <c:redirect url="/chikaboom/main"/>
    </c:if>
    <div class="row w-100 justify-content-md-center">

        <%-- ЛЕВАЯ ЧАСТЬ --%>
        <div class="content-box col-7 ms-2 g-0">

            <%-- БЛОК ИНФОРМАЦИИ --%>
            <div class="content">
                <div class="full-width-inline-box">
                    <p class="common-text">
                        Маркиза Анфисова
                    </p>
                    <div class="right-inline-box">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg" alt="no_image">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg" alt="no_image">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg" alt="no_image">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg" alt="no_image">
                        <img class="small-icon" src="../image/icon/star_icon.svg" alt="no_image">
                    </div>
                </div>
                <div class="small-light-text">
                    inst: @murmurmur
                </div>

                <hr>

                <div>
                    <p class="common-text">Телефон(ы): +375 (25) 728-41-58 Life:)</p>
                    <p class="common-text">Адрес: г. Милашек, ул. Пушистиков, 40</p>
                </div>

                <hr>

                <div>
                    <p class="common-text">Мастер ногтевого сервиса</p>
                    <div class="small-light-text">
                        #френч #модные коготки #быстрый маникюр #аппаратный педикюр
                    </div>
                </div>

                <%-- КАРУСЕЛЬ ГАЛЕРЕИ --%>
                <div class="common-text">Галерея</div>
                <div id="carouselGallery" class="carousel slide" data-bs-interval="false" data-bs-ride="carousel">
                    <div class="carousel-inner">
                        <div class="carousel-item active">
                            <div class="w-flex_mrgn-2">
                                <img class="gallery_image" src="../image/test/test1.png" alt="...">
                                <img class="gallery_image" src="../image/test/test2.png" alt="...">
                                <img class="gallery_image" src="../image/test/test3.png" alt="...">
                                <img class="gallery_image" src="../image/test/test4.png" alt="...">
                                <img class="gallery_image" src="../image/test/test5.png" alt="...">
                            </div>
                        </div>
                        <div class="carousel-item">
                            <div class="w-flex_mrgn-2">
                                <img class="gallery_image" src="../image/stub.png" alt="...">
                                <img class="gallery_image" src="../image/test/test5.png" alt="...">
                                <img class="gallery_image" src="../image/space_background.jpg" alt="...">
                                <img class="gallery_image" src="../image/top_master_template.png" alt="...">
                                <img class="gallery_image" src="../image/service/service_icon_1.png" alt="...">
                            </div>
                        </div>
                    </div>
                    <button class="carousel-control-prev" data-bs-target="#carouselGallery"
                            data-bs-slide="prev">
                        <span class="carousel-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Предыдущий</span>
                    </button>
                    <button class="carousel-control-next" data-bs-target="#carouselGallery"
                            data-bs-slide="next">
                        <span class="carousel-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Следующий</span>
                    </button>
                </div>

                <div class="small-light-text">О себе</div>
                <div class="common-text">
                    Мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу
                    мур мур мурмур мур мурмур мур мурмур мур мурмур мур мурмур мур мурмур мур мур
                    мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур
                </div>

                <%-- СТЕНА И ОТЗЫВЫ --%>
                <div class="full-width-inline-box">
                    |
                    <div class="small-light-text wall-menu-child">
                        Стена
                    </div>
                    |
                    <div class="small-light-text wall-menu-child">
                        Отзывы
                    </div>
                    |
                </div>
                <hr>
                <div class="content" class="solid-dark-border">
                    <div class="full-width-inline-box">
                        <div class="right-inline-box">ooo</div>
                    </div>

                    <%-- TODO REFACTOR STYLE. Будет генерироваться--%>
                    <div id="carouselPost" class="carousel slide" data-bs-interval="false" data-bs-ride="carousel">
                        <div class="carousel-inner">
                            <div class="carousel-item active">
                                <div class="w-flex_mrgn-2">
                                    <img src="../image/test/test1.png"
                                         class="post_image" alt="...">
                                </div>
                            </div>
                            <div class="carousel-item">
                                <div class="w-flex_mrgn-2">
                                    <img src="../image/stub.png"
                                         class="post_image"
                                         alt="...">
                                </div>
                            </div>
                        </div>

                        <div class="carousel-indicators">
                            <button data-bs-target="#carouselPost" data-bs-slide-to="0"
                                    class="active"
                                    aria-current="true" aria-label="Slide 1"></button>
                            <button data-bs-target="#carouselPost" data-bs-slide-to="1"
                                    aria-label="Slide 2"></button>
                        </div>
                        <button class="carousel-control-prev" data-bs-target="#carouselPost"
                                data-bs-slide="prev">
                            <span class="carousel-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Предыдущий</span>
                        </button>
                        <button class="carousel-control-next" data-bs-target="#carouselPost"
                                data-bs-slide="next">
                            <span class="carousel-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Следующий</span>
                        </button>
                    </div>
                    <div class="common-text">
                        Всем привет, с вами Джонни Кэтсвилл и я немного набрал весу....
                        Такие дела. Тада-да-да
                    </div>
                    <div class="small-light-text">30.03.2022 4:05</div>
                </div>
            </div>

        </div>

        <%-- ПРАВАЯ ЧАСТЬ --%>
        <div class="content-box col-3 g-0">

            <div class="content">
                <img class="full-width-inline-box" src="../image/user/${id}/avatar.png"
                     onerror="this.src='../image/user/no_photo.jpg'" alt="error on load">
                <div class="button big-button"><a href="/chikaboom/under_construction">Редактировать</a></div>
            </div>

            <%-- ЦЕНЫ НА УСЛУГИ --%>
            <div class="content">
                <div class="small-light-text">
                    Цены на услуги
                </div>
                <hr>
                <div>
                    <div class="content">
                        <div id="servicesCarousel" class="carousel slide" data-bs-interval="false"
                             data-bs-ride="carousel">
                            <div class="carousel-inner">
                                <div class="carousel-item active">
                                    <div class="w-flex_mrgn-2" style="display: grid">
                                        <p><span>Снятие чужого покрытия</span><span>5р</span></p>
                                        <p><span>Однотонное покрытие</span><span>5р</span></p>
                                        <p><span>Наращивание</span><span>5р</span></p>
                                        <p><span>Сложный дизайн</span><span>5р</span></p>
                                        <p>
                                            <span>Снятие чужого покрытия Снятие чужого покрытия Снятие чужого покрытия</span><span>5р</span>
                                        </p>
                                    </div>
                                </div>
                                <div class="carousel-item">
                                    <div class="w-flex_mrgn-2" style="display: grid">
                                        <p><span>покрытия чужого </span><span>5р</span></p>
                                        <p><span>покрытие Однотонное </span><span>5р</span></p>
                                        <p><span>Себя Наращивание</span><span>5р</span></p>
                                        <p><span>дизайн Сложный </span><span>5р</span></p>
                                        <p>
                                            <span>Снятие чужого покрытия Снятие чужого покрытия Снятие чужого покрытия</span><span>5р</span>
                                        </p>
                                    </div>
                                </div>
                            </div>

                            <div class="carousel-indicators">
                                <button data-bs-target="#servicesCarousel" data-bs-slide-to="0"
                                        class="active"
                                        aria-current="true" aria-label="Slide 1"></button>
                                <button data-bs-target="#servicesCarousel" data-bs-slide-to="1"
                                        aria-label="Slide 2"></button>
                            </div>
                            <div class="full-width-inline-box">
                                <button class="carousel-control-prev" style="position: relative"
                                        data-bs-target="#servicesCarousel"
                                        data-bs-slide="prev">
                                    <span class="carousel-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Предыдущий</span>
                                </button>
                                <button class="carousel-control-next" style="position: relative; margin: 0 0 0 auto;"
                                        data-bs-target="#servicesCarousel"
                                        data-bs-slide="next">
                                    <span class="carousel-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Следующий</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>

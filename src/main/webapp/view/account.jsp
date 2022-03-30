<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>XM. Аккаунт</title>

    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/account.css">
    <link rel="stylesheet" href="../css/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="../css/addition/carousel.css">
    <link rel="stylesheet" href="../css/addition/menu_bar.css">
</head>
<body>
<jsp:include page="common/common_header.jsp"/>
<c:set var="session" value="${id}"/>

<div class="flex-box">
    <jsp:include page="common/menu_bar.jsp"/>
    <div class="row w-80">

        <%-- ЛЕВАЯ ЧАСТЬ --%>
        <div class="content-box col my-2 ms-2 g-0">

            <%-- БЛОК ИНФОРМАЦИИ --%>
            <div class="content">
                <%-- TODO REFACTOR STYLE --%>
                <div style="display: inline-flex; width: 100%">
                    <p class="common-text">
                        Маркиза Анфисова
                    </p>
                    <%-- TODO REFACTOR STYLE --%>
                    <div style="display: inline-flex; margin: 0 0 0 auto">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg">
                        <img class="small-icon" src="../image/icon/star_filled_icon.svg">
                        <img class="small-icon" src="../image/icon/star_icon.svg">
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
            </div>

            <%-- КАРУСЕЛЬ ГАЛЕРЕИ TODO REFACTOR--%>
            <div class="content">
                <div class="common-text">Галерея</div>
                <div id="carouselGallery" class="carousel slide" data-bs-interval="false" data-bs-ride="carousel">
                    <div class="carousel-inner">
                        <div class="carousel-item active">
                            <div class="w-flex_mrgn-2">
                                <img class="gallery_image" src="../image/test/test1.png"
                                     class="d-block top_master_block" alt="...">
                                <img class="gallery_image" src="../image/test/test2.png"
                                     class="d-block top_master_block" alt="...">
                                <img class="gallery_image" src="../image/test/test3.png"
                                     class="d-block top_master_block" alt="...">
                                <img class="gallery_image" src="../image/test/test4.png"
                                     class="d-block top_master_block" alt="...">
                                <img class="gallery_image" src="../image/test/test5.png"
                                     class="d-block top_master_block" alt="...">
                            </div>
                        </div>
                        <div class="carousel-item">
                            <div class="w-flex_mrgn-2">
                                <img class="gallery_image" src="../image/stub.png" class="d-block top_master_block"
                                     alt="...">
                                <img class="gallery_image" src="../image/test/test5.png"
                                     class="d-block top_master_block" alt="...">
                                <img class="gallery_image" src="../image/space_background.jpg"
                                     class="d-block top_master_block" alt="...">
                                <img class="gallery_image" src="../image/top_master_template.png"
                                     class="d-block top_master_block" alt="...">
                                <img class="gallery_image" src="../image/service/service_icon_1.png"
                                     class="d-block top_master_block" alt="...">
                            </div>
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselGallery"
                            data-bs-slide="prev">
                        <span class="carousel-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Предыдущий</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselGallery"
                            data-bs-slide="next">
                        <span class="carousel-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Следующий</span>
                    </button>
                </div>
            </div>

            <div class="content">
                <div class="small-light-text">О себе</div>
                <div class="common-text">
                    Мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу мяу
                    мур мур мурмур мур мурмур мур мурмур мур мурмур мур мурмур мур мурмур мур мур
                    мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур мур
                </div>
            </div>

            <%-- СТЕНА И ОТЗЫВЫ TODO REFACTOR STYLE --%>
            <div class="content">
                <div style="display: inline-flex">
                    |
                    <div class="small-light-text" style="padding: 0 5px">
                        Стена
                    </div>
                    |
                    <div class="small-light-text" style="padding: 0 5px">
                        Отзывы
                    </div>
                    |
                </div>
                <hr>
                <div class="content" style="border: #1a1e21 1px solid">
                    <div style="width: 100%; display: inline-flex">
                        <div style="margin: 0 0 0 auto">ooo</div>
                    </div>

                    <%-- TODO REFACTOR STYLE --%>
                    <div id="carouselPost" class="carousel slide" data-bs-interval="false" data-bs-ride="carousel">
                        <div class="carousel-inner">
                            <div class="carousel-item active">
                                <div class="w-flex_mrgn-2">
                                    <img style="width: 65%; max-height: 350px" class="gallery_image"
                                         src="../image/test/test1.png"
                                         class="d-block top_master_block" alt="...">
                                </div>
                            </div>
                            <div class="carousel-item">
                                <div class="w-flex_mrgn-2">
                                    <img style="width: 65%; max-height: 350px" class="gallery_image"
                                         src="../image/stub.png"
                                         class="d-block top_master_block"
                                         alt="...">
                                </div>
                            </div>
                        </div>

                        <div class="carousel-indicators">
                            <button type="button" data-bs-target="#carouselGallery2" data-bs-slide-to="0"
                                    class="active"
                                    aria-current="true" aria-label="Slide 1"></button>
                            <button type="button" data-bs-target="#carouselGallery2" data-bs-slide-to="1"
                                    aria-label="Slide 2"></button>
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#carouselPost"
                                data-bs-slide="prev">
                            <span class="carousel-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Предыдущий</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselPost"
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
        <div class="content-box col-3 my-2 ms-2 g-0">

            <div class="content">
                <img style="width: 100%" src="../image/test/test.svg">
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
                        <div id="carouselGallery2" class="carousel slide" data-bs-interval="false"
                             data-bs-ride="carousel">
                            <div class="carousel-inner">
                                <div class="carousel-item active">
                                    <div class="w-flex_mrgn-2 table" style="display: block">
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
                                    <div class="w-flex_mrgn-2 table" style="display: block">
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

                            <%-- TODO FIX STYLE JSTL--%>
                            <div class="carousel-indicators">
                                <button type="button" data-bs-target="#carouselGallery2" data-bs-slide-to="0"
                                        class="active"
                                        aria-current="true" aria-label="Slide 1"></button>
                                <button type="button" data-bs-target="#carouselGallery2" data-bs-slide-to="1"
                                        aria-label="Slide 2"></button>
                            </div>
                            <div style="display: inline-flex; width: 100%">
                                <button class="carousel-control-prev" style="position: relative" type="button"
                                        data-bs-target="#carouselGallery2"
                                        data-bs-slide="prev">
                                    <span class="carousel-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Предыдущий</span>
                                </button>
                                <button class="carousel-control-next" style="position: relative; margin: 0 0 0 auto;"
                                        type="button"
                                        data-bs-target="#carouselGallery2"
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
</div>
</body>
</html>

<script type="text/javascript" src="../js/bootstrap/bootstrap.js"></script>

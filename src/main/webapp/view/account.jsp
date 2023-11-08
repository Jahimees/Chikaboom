<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html lang="ru">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>XM. Аккаунт</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="../../css/common.css">
    <link rel="stylesheet" href="../../css/account.css">
    <link rel="stylesheet" href="../../css/addition/carousel.css">
    <link rel="stylesheet" href="../../css/addition/menu_bar.css">
    <link rel="stylesheet" href="../../css/media/main_media.css">
    <link rel="stylesheet" href="../../css/popup.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/css/intlTelInput.css">
    <link rel="stylesheet" href="https://fonts.cdnfonts.com/css/source-sans-pro">

    <script src="../../js/util.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="../../js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/js/intlTelInput.min.js"></script>
    <script src="../../js/countrycode/countryCode.js"></script>
</head>
<jsp:include page="common/popup/appointment_modal.jsp"/>
<body>
<jsp:include page="common/common_header.jsp"/>
<div class="flex-box-white">
    <%-- БЛОК ИНФОРМАЦИИ --%>
    <div class="content" style="width: 95%">
        <div class="d-flex divided-background padding-0-0-0-5 ">
            <div class="padding-0-0-30px">
                <img class="avatar-image" src="../../image/user/${idAccount}/avatar.jpeg"
                     onerror="this.src='../../../image/user/no_photo.jpg'"
                     alt="error on load">
                <div class="d-flex flex-row-reverse master-only">
                    <sec:authorize access="hasRole('ROLE_CLIENT')">
                        <div id="add-remove-favorite" data-tooltip="Добавить в избранное"
                             class="full-middle-box"
                             style="cursor:pointer;">
                            <img class="w-70px" src="../../image/icon/favorite_star.png">
                        </div>

                        <!-- Кнопка-триггер модального окна -->
                        <button type="button" class="purple-button m-2 master-only"
                                data-bs-toggle="modal"
                                data-bs-target="#sendMessageModal">
                            Написать мастеру
                        </button>
                    </sec:authorize>
                </div>
            </div>

            <div class="main-information-block">
                <b>
                    <div id="username-placeholder" class="common-black-text">
                    </div>
                </b>
                <div id="profession-placeholder" class="small-text master-only"></div>
                <div class="main-information">
                    <div id="phone-placeholder" class="medium-text"></div>
                    <div id="address-placeholder" class="medium-text master-only">Адрес:</div>
                    <div class="d-inline-flex medium-text master-only">
                        <div class="chapter-header">
                            О себе:
                        </div>
                        <div class="vertical-blue-line"></div>
                        <div id="about-text-placeholder"></div>
                    </div>

                </div>
            </div>
        </div>

        <%--    СТАТИСТИКА    --%>
        <div class="d-inline-flex w-100 statistic-line master-only">
            <div id="avg-rating" class="light-medium-text light-statistic-elem">
                ОБЩИЙ РЕЙТИНГ: -
            </div>
            <div id="pos-rating" class="light-medium-text statistic-elem">
                - <i class="fas fa-thumbs-up"></i>
            </div>
            <div id="neg-rating" class="light-medium-text statistic-elem">
                - <i class="fas fa-thumbs-down"></i>
            </div>
        </div>

        <%--    ЯКОРЯ    --%>
        <div class="d-inline-flex medium-text margin-2-10-0-10 master-only">
            <a href="#price-block">
                ЦЕНЫ
            </a>
            <div class="horizontal-black-line"></div>
            <a href="#photo-gallery-block">
                ГАЛЕРЕЯ ФОТО
            </a>
            <div class="horizontal-black-line"></div>
            <a href="#review-block">
                ОТЗЫВЫ
            </a>
        </div>
        <hr>

        <%--    ЦЕНЫ И УСЛУГИ    --%>
        <div id="price-block" class="d-flex margin-2-10-0-10 master-only">
            <div class="chapter-header medium-text">
                ЦЕНЫ И УСЛУГИ
            </div>
            <div id="service-placeholder" class="d-block w-100">
            </div>
        </div>

        <hr>

        <%--    ГАЛЕРЕЯ ФОТО    --%>
        <div id="photo-gallery-block" class="d-flex margin-2-10-0-10 master-only">
            <div class="chapter-header medium-text">
                ГАЛЕРЕЯ ФОТО
            </div>
            <div class="d-inline-flex w-100" id="photo-container" style="justify-content: center;">

            </div>
        </div>

        <%--    ОТЗЫВЫ    --%>
        <div id="review-block" class="d-flex margin-2-10-0-10 master-only">
            <div class="chapter-header medium-text">
                ОТЗЫВЫ
            </div>

            <div class="margin-0-20">
                <div class="margin-5-10">
                    <div class="d-inline-flex margin-0-10px">
                        <div class="radio_group d-inline-flex">
                            <input type="radio" id="like" checked value="true" name="like">
                            <label for="like">
                                <i class="fas fa-thumbs-up"></i>
                            </label>
                        </div>

                        <div class="radio_group">
                            <input type="radio" id="dislike" value="false" name="like">
                            <label for="dislike">
                                <i class="fas fa-thumbs-down"></i>
                            </label>
                        </div>
                        <label class="invalid-field-label-popup" style="display: none" id="invalid-text-lbl"
                               for="comment-text-area">Поле не должно превышать 500 символов и не может быть
                            пустым
                        </label>
                    </div>
                    <div data-tooltip="Оставить отзыв. 500 символов максимум" class="d-inline-flex">

                        <textarea id="comment-text-area" style="width: 500px;" class="margin-0-10px"></textarea>
                        <div id="send-comment" class="purple-button">Отправить</div>
                    </div>
                </div>
                <div id="comments-container">
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://kit.fontawesome.com/1fc4ea1c6a.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>

</html>
<jsp:include page="/view/common/popup/login_popup.jsp"/>
<jsp:include page="/view/common/popup/message_popup.jsp"/>
<jsp:include page="/view/common/popup/send_message_modal.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@7.0.0/bundles/stomp.umd.min.js"></script>
<script type="text/javascript" src="/js/static_popup.js"></script>
<script type="text/javascript" src="/js/dynamic_popup.js"></script>
<script type="text/javascript" src="/js/account.js"></script>
<script type="text/javascript" src="/js/favorite.js"></script>
<script type="text/javascript" src="/js/service.js"></script>
<script type="text/javascript" src="/js/appointment.js"></script>
<script type="text/javascript" src="/js/client.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bs5-lightbox@1.8.3/dist/index.bundle.min.js"></script>
<script>

    let commentsCache;

    $(document).ready(() => {
        initializePage(${idAccount}, ${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.idAccount});

        document.querySelectorAll('.my-lightbox-toggle').forEach((el) => el.addEventListener('click', (e) => {
            e.preventDefault();
            const lightbox = new Lightbox(el);
            lightbox.show();
        }));

        const idAccountMaster = "${idAccount}";
        const idAccountClient = "${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.idAccount}";
        loadComments(idAccountMaster, idAccountClient);
        initDeleteCommentBind(idAccountClient);
        initCreateCommentBind(idAccountMaster, idAccountClient);
    })
</script>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>XM. Поиск услуг</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="../../../css/common.css">
    <link rel="stylesheet" href="../../../css/media/common_media.css">
    <link rel="stylesheet" href="../../../css/account.css">
    <link rel="stylesheet" href="../../../css/popup.css">
    <link rel="stylesheet" href="../../../css/media/popup_media.css">
    <link rel="stylesheet" href="../../../css/addition/menu_bar.css">
    <link rel="stylesheet" href="../../../css/main.css">
    <link rel="stylesheet" href="../../../css/media/main_media.css">
    <link rel="stylesheet" href="../../../css/popup.css">
    <link rel="stylesheet" href="https://fonts.cdnfonts.com/css/source-sans-pro">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/css/intlTelInput.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/js/jquery.suggestions.min.js"></script>
    <script src="../../../js/jquery-ui-1.10.4.custom.min.js"></script>

    <script src="../../../js/countrycode/countryCode.js"></script>

    <script src="../../../js/service_search.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/js/intlTelInput.min.js"></script>

</head>
<body>
<jsp:include page="common/common_header.jsp"/>
<div class="big-text padding-0-5">
    ${serviceSubtypeList[0].serviceType.name}
</div>
<div class="medium-text padding-0-5 full-width-inline-box">
    <a href="/chikaboom/main" style="text-decoration: none; color: #523870">Главная</a>
    <div style="padding: 0 15px">></div>
    <a href="/chikaboom/service" style="text-decoration: none; color: #523870">
        Услуги</a>
    <div style="padding: 0 15px">></div>
    <div id="service-type-name-placeholder"></div>
</div>
<hr>
<div class="row">
    <div class="padding-0-5 col-4" style="border-right: solid 1px;">
        <div class="common-text">
            Тип услуги
        </div>
        <div id="service-subtype-block">

        </div>
        <div id="do-search-btn" class="margin-5-0 purple-button">
            Показать
        </div>
    </div>
    <div class="col-8">
        <div id="search-result-placeholder" style="display: inline-flex; flex-wrap: wrap;">
        </div>
    </div>

</div>
<jsp:include page="common/popup/message_popup.jsp"/>
<jsp:include page="common/popup/login_popup.jsp"/>
</body>
</html>

<script src="../../../js/util.js"></script>
<script src="https://kit.fontawesome.com/1fc4ea1c6a.js"></script>
<script src="../../../js/static_popup.js"></script>
<script src="../../../js/dynamic_popup.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
<script>
    $("#do-search-btn").on("click", function () {
        doSearch(${idServiceType});
    })

    $(document).ready(() => {
        loadSubtypeData(${idServiceType});
        doSearch(${idServiceType});
    })
</script>

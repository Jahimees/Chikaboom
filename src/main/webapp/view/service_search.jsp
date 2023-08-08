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
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/media/common_media.css">
    <link rel="stylesheet" href="/css/account.css">
    <link rel="stylesheet" href="/css/personality.css">
    <link rel="stylesheet" href="/css/popup.css">
    <link rel="stylesheet" href="/css/media/popup_media.css">
    <link rel="stylesheet" href="/css/addition/carousel.css">
    <link rel="stylesheet" href="/css/addition/menu_bar.css">
    <link rel="stylesheet" href="/css/addition/phonecode.css"/>
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/media/main_media.css">
    <link rel="stylesheet" href="/css/popup.css">


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

</head>
<body>
<jsp:include page="common/common_header.jsp"/>
<div class="big-text padding-0-5">
    ${serviceSubtypeList[0].serviceType.name}
</div>
<div class="medium-text padding-0-5 full-width-inline-box">
    <a href="/chikaboom/main" style="text-decoration: none; color: #5F4E7D">Главная</a>
    <div style="padding: 0 15px">></div>
    <a href="/chikaboom/service" style="text-decoration: none; color: #5F4E7D">
    Услуги</a>
    <div style="padding: 0 15px">></div>
    ${serviceSubtypeList[0].serviceType.name}
</div>
<hr>
<div class="row">
    <div class="padding-0-5 col-4" style="border-right: solid 1px;">
        <%--        <div class="common-text">--%>
        <%--            Сортировка--%>
        <%--        </div>--%>
        <div class="common-text">
            Тип услуги
        </div>
        <div id="service-subtype-block">
            <c:forEach items="${serviceSubtypeList}" var="serviceSubtype">
                <div class="medium-text">
                    <input type="checkbox" class="service-subtype-checkbox" id="${serviceSubtype.idServiceSubtype}">
                    <label for="${serviceSubtype.idServiceSubtype}">${serviceSubtype.name}</label>
                </div>
            </c:forEach>

        </div>
        <div id="do-search-btn" class="margin-5-0 purple-button">
            Показать
        </div>
    </div>
    <div class="col-8">
        <div id="search-result-placeholder" class="row">
        </div>
    </div>

</div>
</body>
</html>


<script>
    var serviceListJson;

    $("#do-search-btn").on("click", function () {
        doSearch();
    })

    function doSearch() {
        let serviceSubtypeIdList = [];

        Array.from($(".service-subtype-checkbox")).forEach(function (serviceSubtypeCheckbox) {
            if (serviceSubtypeCheckbox.checked) {
                serviceSubtypeIdList.push(serviceSubtypeCheckbox.getAttribute("id"));
            }
        })

        $.ajax({
            type: "get",
            url: "/chikaboom/service/search/${idServiceType}/dosearch",
            contentType: "application/text",
            dataType: "text",
            data: {
                serviceSubtypeIdListJson: JSON.stringify(serviceSubtypeIdList),
            },
            success: function (data) {
                serviceListJson = JSON.parse(data);
                fillResultSearchTable(serviceListJson)
            }
        })
    }

    function fillResultSearchTable(serviceListJson) {
        let searchResultPlaceHolder = $("#search-result-placeholder")[0];
        searchResultPlaceHolder.innerHTML = "";

        if (serviceListJson.length !== 0) {
            serviceListJson.forEach(function (service) {
                let price = service.price;
                let serviceName = service.name;
                let idMasterAccount = service.account.idAccount
                let masterName = service.account.username;

                let accountHref = document.createElement("a");
                accountHref.setAttribute("href", "/chikaboom/account/" + idMasterAccount);
                accountHref.setAttribute("class", "col-xl-3 non-decorated-link");

                let img = document.createElement("img");
                img.setAttribute("class", "result-image");
                img.setAttribute("src", "/image/user/" + idMasterAccount + "/avatar.jpeg");

                let divName = document.createElement("div");
                divName.setAttribute("class", "result-item-name");

                let pName = document.createElement("p");
                pName.setAttribute("class", "small-white-text");
                pName.innerText = masterName;

                let divInfo = document.createElement("div");
                divInfo.setAttribute("class", "result-item");

                let pServiceName = document.createElement("p");
                pServiceName.setAttribute("class", "small-white-text");
                pServiceName.innerText = serviceName;

                let pPrice = document.createElement("p");
                pPrice.setAttribute("class", "small-text");
                pPrice.setAttribute("style", "background-color: antiquewhite; border-radius: 2px; text-align: center; font-weight: bold");
                pPrice.innerText = price + " руб.";

                divName.appendChild(pName);

                divInfo.appendChild(pServiceName);
                divInfo.appendChild(pPrice);

                accountHref.appendChild(img);
                accountHref.appendChild(divName);
                accountHref.appendChild(divInfo);


                searchResultPlaceHolder.appendChild(accountHref);
            })
        } else {
            let divLbl = document.createElement("div");
            divLbl.setAttribute("class", "common-text");
            divLbl.innerText = "Поиск не дал результатов...";

            searchResultPlaceHolder.appendChild(divLbl);
        }
    }


    $(document).ready(function () {
        doSearch();
    })
</script>

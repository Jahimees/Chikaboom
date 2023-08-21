<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="general-setting-tab"  class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Услуги</a></div>
    </div>
</div>

<div id="service-type-content-placeholder" class="setting-content-placeholder">

</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/css/suggestions.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/js/jquery.suggestions.min.js"></script>
<script src="/js/service.js"></script>
<script>

    var servicesJson;
    var serviceSubtypes; //= JSON.parse(JSON.stringify(${serviceSubtypes}));
    var serviceTypes;

    function loadServiceTab(tabName) {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/services/" + tabName,
            contentType: "application/text",
            dataType: "text",
            success: function (data) {
                setCurrentTabName(tabName);
                loadServiceSubtypes();
                $("#service-type-content-placeholder").html(data);
            },
            error: function () {
                loadUnderConstruction();
            }
        });
    }

    function loadServiceSubtypes() {
        $.ajax({
            contentType: "application/json",
            dataType: "json",
            method: "get",
            url: "/service-subtypes",
            async: false,
            success: function (data) {
                serviceSubtypes = data;
            },
            error: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Что-то пошло не так. Невозможно загрузить подтипы услуг!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Невозможно загрузить подтипы услуг!";
                openPopup('message-popup');
            }
        })
    }

    $(document).ready(function () {
        loadServiceTab("general");
        loadServiceSubtypes();
    })
</script>
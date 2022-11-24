<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="general-setting-tab" class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Услуги</a></div>
    </div>
</div>

<div id="service-content-placeholder" class="setting-content-placeholder">

</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/css/suggestions.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/js/jquery.suggestions.min.js"></script>
<script src="/js/service.js"></script>
<script>

    var userServicesJson;
    var subservices = JSON.parse(JSON.stringify(${subservices}));
    var services;

    function loadServiceTab(tabName) {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/services/" + tabName,
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                setCurrentTabName(tabName);
                console.log("Load service tab " + tabName);
                $("#service-content-placeholder").html(data);
            },
            error: function () {
                loadUnderConstruction();
            }
        });
    }

    $(document).ready(function () {
        loadServiceTab("general");
    })
</script>
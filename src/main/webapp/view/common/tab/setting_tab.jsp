<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="general-setting-tab" class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Основные</a></div>
    </div>
    <div id="profile-setting-tab" class="horizontal-menu-child" selected="false">
        <div class="horizontal-menu-text"><a href="#">Профиль</a></div>
    </div>
    <div id="personalization-setting-tab" class="horizontal-menu-child" selected="false">
        <div class="horizontal-menu-text"><a href="#">Персонализация</a></div>
    </div>
    <div id="security-setting-tab" class="horizontal-menu-child" selected="false">
        <div class="horizontal-menu-text"><a href="#">Безопасность</a></div>
    </div>
    <div id="notification-setting-tab" class="horizontal-menu-child" selected="false">
        <div class="horizontal-menu-text"><a href="#">Уведомления</a></div>
    </div>
    <div id="payment-details-setting-tab" class="horizontal-menu-child" selected="false">
        <div class="horizontal-menu-text"><a href="#">Реквизиты</a></div>
    </div>
</div>

<div id="setting-content-placeholder" class="setting-content-placeholder">

</div>

<script src="../../../js/setting_tab.js"></script>
<script>
    function loadGeneralSetting() {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/settings/general",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#setting-content-placeholder").html(data);
            },
            error: function () {
                console.error("ERROR")
                //TODO Доделать ошибку
                // showWarnWrongLoginData();
            }
        });
    }

    function loadUnderConstruction() {
        $.ajax({
            type: "get",
            url: "/chikaboom/under_construction",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#setting-content-placeholder").html(data);
            },
            error: function () {
                console.error("ERROR")
                //TODO Доделать ошибку
                // showWarnWrongLoginData();
            }
        });
    }

    function loadProfileSetting() {
        loadUnderConstruction(); //TODO Заглушка
    }

    function loadPersonalizationSetting() {
       loadUnderConstruction(); //TODO Заглушка
    }

    function loadSecuritySetting() {
        loadUnderConstruction(); //TODO Заглушка
    }

    function loadNotificationSetting() {
        loadUnderConstruction(); //TODO Заглушка
    }

    function loadPaymentDetailsSetting() {
        loadUnderConstruction(); //TODO Заглушка
    }

    $(document).ready(function () {
        $("#general-setting-tab").click();
    })
</script>
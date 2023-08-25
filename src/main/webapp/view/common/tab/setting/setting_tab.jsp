<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--//TODO CLASS--%>
<div class="full-width-inline-box">
    <div class="news-data-box col-6">
        <div id="greeting-info-box" class="big-medium-black-text padding-0-0-10-px">
            Добро пожаловать,
        </div>
        <div class="smaller-medium-text">
            Отличного дня и хорошего настроения!
        </div>
        <div id="profile-filling-progress">
            <div class="diagram progress" data-percent="75.5">
                <div class="piece left"></div>
                <div class="piece right"></div>
                <div class="text">
                    <div>
                        <span id="progress-percent-placeholder">100%</span>
                    </div>
                </div>
            </div>
            <div id="profile-filling-text">
                <div class="small-black-text"><b>Прогресс заполнения</b></div>
                <div class="small-black-text">Заполненный профиль вызывает доверие клиента!</div>
            </div>
        </div>
    </div>
    <div class="col-6">
        <div class="menu-box-horizontal">
            <div id="general-setting-tab" class="horizontal-menu-child" selected="true">
                <div class="horizontal-menu-text"><a href="#">Основные</a></div>
            </div>
            <sec:authorize access="hasRole('ROLE_MASTER')">
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
            </sec:authorize>
        </div>

        <div id="setting-content-placeholder" class="setting-content-placeholder">

        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/css/suggestions.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/js/jquery.suggestions.min.js"></script>
<script src="/js/tab.js"></script>

<script>
    $(document).ready(function () {
        loadSettingTab("general", ${idAccount});
    })

    $("#general-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadSettingTab("general", ${idAccount});
    });

    $("#personalization-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadUnderConstruction();
    });

    $("#security-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadUnderConstruction();
    });

    $("#notification-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadUnderConstruction();
    });

    $("#payment-details-setting-tab").on("click", function () {
        selectCurrentTab(this);
        loadUnderConstruction();
    });
</script>
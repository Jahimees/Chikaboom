<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="menu-box-horizontal">
    <div id="outcome-appointment-tab" class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#outcome-appointment">Мои записи</a></div>
    </div>
    <sec:authorize access="hasRole('ROLE_MASTER')">
        <div id="income-appointment-tab" class="horizontal-menu-child" selected="false">
            <div class="horizontal-menu-text"><a href="#income-appointment">Записи ко мне</a></div>
        </div>
    </sec:authorize>
</div>

<div id="appointment-tab-placeholder" class="setting-content-placeholder">

</div>

<link href="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/css/suggestions.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/js/jquery.suggestions.min.js"></script>
<script src="/js/tab.js"></script>
<script src="/js/appointment.js"></script>

<script>
    $(document).ready(function () {
        console.log("loading")
        loadAppointmentConcreteTab("outcome", ${idAccount});
    })

    $("#outcome-appointment-tab").on("click", function () {
        selectCurrentTab(this);
        loadAppointmentConcreteTab("outcome", ${idAccount});
    })

    $("#income-appointment-tab").on("click", function () {
        selectCurrentTab(this);
        console.log("inc-ap-tab")
        loadAppointmentConcreteTab("income", ${idAccount});
    })
</script>
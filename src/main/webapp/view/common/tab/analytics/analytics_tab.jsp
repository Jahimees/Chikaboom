<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="menu-box-horizontal">
    <div id="appointment-analytics-tab" class="horizontal-menu-child" selected="false">
        <div class="horizontal-menu-text"><a href="#appointment_analytics">Записи</a></div>
    </div>
    <div id="client-analytics-tab" class="horizontal-menu-child" selected="false">
        <div class="horizontal-menu-text"><a href="#client_analytics">Клиенты</a></div>
    </div>
</div>

<div id="analytics-tab-placeholder" class="inner-content-placeholder">
</div>

<link href="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.css"
      rel="stylesheet">
<script src="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.js"></script>
<script src="/js/appointment.js"></script>
<script src="/js/service.js"></script>
<script src="/js/client.js"></script>

<script>
    $(document).ready(() => {
        selectCurrentTab($("#appointment-analytics-tab")[0]);
        loadAnalyticsConcreteTab("appointment", ${idAccount});
    })

    $("#appointment-analytics-tab").on("click", function () {
        selectCurrentTab(this);
        loadAnalyticsConcreteTab("appointment", ${idAccount});
    })

    $("#client-analytics-tab").on("click", function () {
        selectCurrentTab(this);
        loadAnalyticsConcreteTab("client", ${idAccount});
    })
</script>
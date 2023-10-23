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

<link href="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.css"
      rel="stylesheet">
<script src="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.js"></script>
<script src="/js/appointment.js"></script>
<script src="/js/service.js"></script>
<script src="/js/client.js"></script>

<script>
    $(document).ready(() => {
        loadAppointmentConcreteTab("outcome", ${idAccount});
    })

    $("#outcome-appointment-tab").on("click", function () {
        selectCurrentTab(this);
        loadAppointmentConcreteTab("outcome", ${idAccount});
    })

    $("#income-appointment-tab").on("click", function () {
        selectCurrentTab(this);
        loadAppointmentConcreteTab("income", ${idAccount});
    })
</script>
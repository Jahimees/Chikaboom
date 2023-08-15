<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Записи на мои услуги</div>

        <div class="d-block w-100">
            <div id="income-appointment-header" class="service-row row" style="background-color: #5f4e7d; color: white">
                <div class="col-2">Название услуги</div>
                <div class="col-2">Дата услуги</div>
                <div class="col-1">Время записи</div>
                <div class="col-1">Цена услуги</div>
                <div class="col-1">Время на услугу</div>
                <div class="col-2">Телефон клиента</div>
                <div class="col-2">Имя клиента</div>
            </div>
            <div id="appointment-placeholder">

            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        console.log("aaaa")
        loadIncomeAppointmentsData(${idAccount});
    })
</script>

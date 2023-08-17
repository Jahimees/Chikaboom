<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Мои записи на услуги</div>

        <div class="d-block w-100">
            <div class="service-row row" style="background-color: #523870; color: white">
                <div class="col-2">Название услуги</div>
                <div class="col-2">Дата услуги</div>
                <div class="col-1">Время записи</div>
                <div class="col-1">Цена услуги</div>
                <div class="col-1">Время на услугу</div>
                <div class="col-2">Телефон мастера</div>
                <div class="col-2">Имя мастера</div>
            </div>
            <div id="appointment-placeholder">

            </div>
        </div>

    </div>
</div>
<script>


    $(document).ready(function () {
        loadOutcomeAppointmentsData(${idAccount});
    })
</script>

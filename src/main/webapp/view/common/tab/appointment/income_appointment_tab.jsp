<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Записи на мои услуги</div>

        <div class="d-block w-100">
<%--            <div id="income-appointment-header" class="service-row row" style="background-color: #523870; color: white">--%>
<%--                <div class="col-2">Название услуги</div>--%>
<%--                <div class="col-2">Дата услуги</div>--%>
<%--                <div class="col-1">Время записи</div>--%>
<%--                <div class="col-1">Цена услуги</div>--%>
<%--                <div class="col-1">Время на услугу</div>--%>
<%--                <div class="col-2">Телефон клиента</div>--%>
<%--                <div class="col-2">Имя клиента</div>--%>
<%--            </div>--%>
<%--            <div id="appointment-placeholder">--%>

<%--            </div>--%>

            <table id="example" class="display" style="width: 100%">
                <thead style="background-color: #523870; color: white">
                <tr>
                    <th>Название услуги</th>
                    <th>Дата услуги</th>
                    <th>Время записи</th>
                    <th>Цена услуги</th>
                    <th>Время на услугу</th>
                    <th>Телефон клиента</th>
                    <th>Имя клиента</th>
                    <th>Удалить</th>
                </tr>
                </thead>
                <tbody id="example-tbody">

                </tbody>

            </table>
        </div>
    </div>
</div>
</div>

<script>
    $(document).ready(function () {
        loadIncomeAppointmentsData(${idAccount});
    })
</script>

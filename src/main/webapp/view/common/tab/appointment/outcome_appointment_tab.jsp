<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Мои записи на услуги</div>

        <div class="form-check form-switch padding-0-0-20-px">
            <input class="form-check-input" type="checkbox" id="past-appointment-toggle" checked>
            <label class="form-check-label small-black-text" for="past-appointment-toggle">
                Отображать прошедшие записи
            </label>
        </div>
        <div class="d-block w-100">
            <table id="appointment_table" class="display" style="width: 100%">
                <thead style="background-color: #523870; color: white">
                <tr>
                    <th>Название услуги</th>
                    <th>Дата услуги</th>
                    <th>Время записи</th>
                    <th>Цена услуги</th>
                    <th>Время на услугу</th>
                    <th>Телефон мастера</th>
                    <th>Имя мастера</th>
                    <th>Удалить</th>
                </tr>
                </thead>
                <tbody id="appointment_table-tbody">

                </tbody>

            </table>
        </div>
    </div>
</div>
<script>

    $(document).ready(function () {
        loadAppointmentsData(${idAccount}, false);

        $("#default_table_wrapper, #past-appointment-toggle").on("click", function () {
            $("#appointment_table").DataTable().draw();
        })
    })
</script>

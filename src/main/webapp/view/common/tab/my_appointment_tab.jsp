<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Мои записи на услуги</div>

        <div id="appointment-placeholder" class="d-block w-100">
            <div class="service-row row" style="background-color: #5f4e7d; color: white">
                <div class="col-2">Название услуги</div>
                <div class="col-2">Дата услуги</div>
                <div class="col-1">Время записи</div>
                <div class="col-1">Цена услуги</div>
                <div class="col-1">Время на услугу</div>
                <div class="col-2">Телефон мастера</div>
                <div class="col-2">Имя мастера</div>
            </div>
            <c:forEach items="${appointmentList}" var="appointment">
                <div class="service-row row">
                    <c:set var="appointmentDate" value="${appointment.appointmentDate}"/>
                    <div class="col-2">${appointment.service.name}</div>
                    <div class="col-2">${appointmentDate.substring(8, 10)}.${appointmentDate.substring(5, 7)}.${appointmentDate.substring(0, 4)}</div>
                    <div class="col-1">${appointment.appointmentTime}</div>
                    <div class="col-1">${appointment.service.price} р.</div>
                    <div class="col-1">${appointment.service.time}</div>
                    <div class="col-2">
                        +${appointment.masterAccount.phoneCode.phoneCode} ${appointment.masterAccount.phone}</div>
                    <div class="col-2">${appointment.masterAccount.username}</div>
                        <%--                    TODO небезопасно. Пользователь видит callConfirmDeletePopup(3) и может поменятЬ!--%>
                    <div class="col-1 edit-button" onclick="callConfirmDeletePopup(${appointment.clientAccount.idAccount},${appointment.idAppointment})"><img
                            src="/image/icon/cross_icon.svg" style="width: 15px;"></div>
                </div>
            </c:forEach>

        </div>

    </div>
</div>

<script>

    function callConfirmDeletePopup(idAccountClient, idAppointment) {
        repairDefaultMessagePopup();
        $("#decline-message-btn")[0].style.display = "block";
        $("#confirm-message-btn")[0].setAttribute("onclick", "deleteMyAppointment(" + idAccountClient + "," + idAppointment + ")");

        $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить запись на услугу?";
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
        openPopup("message-popup");
    }

    function deleteMyAppointment(idAccountClient, idAppointment) {
        $.ajax({
            type: "delete",
            url: "/chikaboom/appointment/" + idAccountClient + "/" + idAppointment,
            contentType: "application/text",
            dataType: "text",
            success: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Запись успешно удалена!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удалено";
                openPopup('message-popup');
                $("#confirm-message-btn")[0].setAttribute("onclick", "closePopup('message-popup'), loadAppointmentTab()");
            },
            error: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Невозможно удалить запись!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');

            }
        })
    }
</script>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Модальное окно -->
<div class="modal fade" id="appointmentModal" tabindex="-1" aria-labelledby="appointmentModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="appointmentModalLabel">Записаться к мастеру</h5>
                <button id="close-modal-btn" type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Закрыть"></button>
            </div>
            <label id="appointment-warn" class="invalid-field-label-popup small-text padding-0-5"
                   for="appointment-fields"
                   style="display: none">Все поля должны быть заполнены! Выберете услугу, день, а затем время!</label>
            <div id="appointment-fields" class="modal-body">
                <div>Услуга</div>
                <select class="middle-box w-80" style="display: block" id="services-select"></select>
                <div>Дата</div>
                <select class="middle-box w-80" style="display: block" id="working-day-select"></select>
                <div>Время</div>
                <select class="middle-box w-80" style="display: block" id="working-time-select"></select>
                <hr>
                <div id="service-time-placeholder"></div>
                <div id="service-cost-placeholder"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="popup-btn" data-bs-dismiss="modal">Закрыть</button>
                <button type="button" class="popup-btn" onclick="makeAppointment()">Записаться</button>
            </div>
        </div>
    </div>
</div>
<jsp:useBean id="objectMapper" class="com.fasterxml.jackson.databind.ObjectMapper"/>
<script>

    let clientId;
    let appointmentToSend;
    let client

    function makeAppointment() {
        clientId = ${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.idAccount != 0
                            ? sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.idAccount : 0} +0;
        let masterId = accountJson.idAccount;

        let workingDayVal = $("#working-day-select").val();
        let workingTimeVal = $("#working-time-select").val();


        if (workingTimeVal === '') {
            $("#appointment-warn").css("display", "block");
        } else if ($("#working-day-select").val() === null) {
            $("#close-modal-btn").click();

            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Не выбрана дата записи! Или, возможно, мастер ещё не настроил свой график работы!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись отклонена!";
            openPopup('message-popup');
        }
        else if (typeof clientId === 'undefined' || clientId === 0) {
            $("#close-modal-btn").click();

            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Сначала необходимо авторизоваться!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись отклонена!";
            openPopup('message-popup');
        } else if (clientId === masterId) {
            $("#close-modal-btn").click();

            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Нельзя записываться самому к себе на услуги!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись отклонена!";
            openPopup('message-popup');
        } else {
            let appointmentDateTime = new Date(workingDayVal);
            let splittedTime = workingTimeVal.split(":");
            appointmentDateTime.setHours(splittedTime[0]);
            appointmentDateTime.setMinutes(splittedTime[1]);

            $("#appointment-warn").css("display", "none");
            client = ${objectMapper.writeValueAsString(sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal)};
            let master = accountJson;

            let idService = parseInt($("#services-select")[0].value);
            let service
            servicesJson.forEach(function (serv) {
                if (serv.idService === idService) {
                    service = serv
                }
            })

            appointmentToSend = {
                clientAccount: client,
                masterAccount: master,
                service: service,
                appointmentDateTime: appointmentDateTime
            }

            $.ajax({
                method: "post",
                url: "/appointments",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(appointmentToSend),
                success: function () {
                    $("#close-modal-btn").click();

                    repairDefaultMessagePopup();
                    $("#popup-message-text")[0].innerText = "Вы успешно записались на услугу!"
                    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись оформлена!";
                    openPopup('message-popup');

                    masterAppointmentsJson = loadMastersAppointments(accountJson.idAccount);
                    calculateServiceTime();
                },
                error: function () {
                    repairDefaultMessagePopup();
                    $("#popup-message-text")[0].innerText = "Не удалось записаться на услугу!"
                    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Произошла ошибка!";
                    openPopup('message-popup');
                }
            })
        }

    }
</script>

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
                <select class="middle-box w-80" style="display: block" id="service-select-app"></select>
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
                <button type="button" class="popup-btn" onclick="makeOutcomeAppointment()">Записаться</button>
            </div>
        </div>
    </div>
</div>
<jsp:useBean id="objectMapper" class="com.fasterxml.jackson.databind.ObjectMapper"/>
<script>
    function makeOutcomeAppointment() {
        let client = ${objectMapper.writeValueAsString(sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal)};
        doMakeAppointment(client, accountJson);
    }
</script>

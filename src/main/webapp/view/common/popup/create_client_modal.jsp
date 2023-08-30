<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Модальное окно -->
<div class="modal fade" id="createClientModal" tabindex="-1" aria-labelledby="createClientModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createClientModalLabel">Создать нового клиента</h5>
                <button id="close-modal-btn" type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Закрыть"></button>
            </div>
            <label id="client-warn" class="invalid-field-label-popup small-text padding-0-5"
                   for="client-fields"
                   style="display: none">Все поля должны быть заполнены! Выберете услугу, день, а затем время!</label>
            <div id="client-fields" class="modal-body">
                <div>Имя</div>
                <input id="client-first-name-input">
                <div>Фамилия</div>
                <input id="client-last-name-input">
                <div>Телефон</div>
                <input id="client-phone-input">
                <div>Краткая информация</div>
                <input id="client-about-input">

                <hr>
            </div>
            <div class="modal-footer">
                <button type="button" class="popup-btn" data-bs-dismiss="modal">Закрыть</button>
<%--                <button type="button" class="popup-btn" onclick="makeAppointment()">Записаться</button>--%>
            </div>
        </div>
    </div>
</div>
<jsp:useBean id="objectMapper" class="com.fasterxml.jackson.databind.ObjectMapper"/>
<script src="/js/phonecode/countries.js"></script>
<script src="/js/phonecode/phonecode.js"></script>
<script>

    $(document).ready(function () {

    })

    function makeAppointment() {
       // doMakeAppointment(clientId, accountJson, client);
    }
</script>

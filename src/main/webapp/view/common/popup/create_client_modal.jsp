<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Модальное окно -->
<div class="modal fade" id="createClientModal" tabindex="-1" aria-labelledby="createClientModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createClientModalLabel">Создать нового клиента</h5>
                <button id="close-modal-btn" type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Закрыть"></button>
            </div>
            <div id="client-fields" class="modal-body">
                <div class="popup-input middle-box w-80">
                    <div>Имя</div>
                    <label id="first-name-invalid-label" class="invalid-field-label-popup">Имя может содержать
                        только
                        буквы и не должно быть пустым</label>
                    <input id="client-first-name-input" required>
                </div>
                <div class="popup-input middle-box w-80">
                    <div>Фамилия</div>
                    <label id="last-name-invalid-label" class="invalid-field-label-popup">Фамилия может содержать
                        только
                        буквы</label>
                    <input id="client-last-name-input">
                </div>
                <div class="popup-input middle-box w-80">
                    <div>Телефон</div>
                    <input id="client-phone-input" style="width: 100%">
                    <%--                    <span id="valid-msg-client-phone-input" class="hide"></span>--%>
                    <span id="error-msg-client-phone-input" class="hide"></span>
                </div>
                <div class="popup-input middle-box w-80">
                    <div>Краткая информация</div>
                    <label id="about-invalid-label" class="invalid-field-label-popup">Информация не должна превышать
                        300
                        символов!</label>
                    <textarea id="client-about-input" style="width: 100%"></textarea>
                </div>
                <hr>
            </div>
            <div class="modal-footer">
                <button type="button" class="popup-btn" data-bs-dismiss="modal">Закрыть</button>
                <button id="create-client-btn" type="button" class="popup-btn">Создать</button>
            </div>
        </div>
    </div>
</div>
<jsp:useBean id="objectMapper" class="com.fasterxml.jackson.databind.ObjectMapper"/>
<script src="/js/phonecode/countries.js"></script>
<script src="/js/phonecode/phonecode.js"></script>

<script>
    $(document).ready(() => {
        initPhoneCodeWidget("client-phone-input");
    })

    $("#create-client-btn").on("click", function () {
        createClient(${idAccount});
    })
</script>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Модальное окно -->
<div class="modal fade" id="sendMessageModal" tabindex="-1" aria-labelledby="sendMessageModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="sendMessageModalLabel">Написать мастеру</h5>
                <button id="close-modal-btn" type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Закрыть"></button>
            </div>
            <div id="message-fields" class="modal-body">
                <div>Сообщение мастеру:</div>
                <textarea id="message-text-input" class="simple-text-area"></textarea>
            </div>
            <div class="modal-footer">
                <button type="button" class="popup-btn" data-bs-dismiss="modal">Закрыть</button>
                <button type="button" class="popup-btn" onclick="processSendMsg()">Отправить</button>
            </div>
        </div>
    </div>
</div>
<jsp:useBean id="objectMapper" class="com.fasterxml.jackson.databind.ObjectMapper"/>
<script>
    $(document).ready(() => {
        connectToStomp();
    })

    function processSendMsg() {
        const messageText = secureCleanValue($("#message-text-input").val().trim());

        if (messageText.length === 0) {
            $("#sendMessageModal").modal('hide');
            callMessagePopup('Ошибка', 'Невозможно отправить пустое сообщение');
        }

        sendMessage(${objectMapper.writeValueAsString(sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.idAccount)},
            secureCleanValue(messageText));

        $("#sendMessageModal").modal('hide');
        callMessagePopup('Сообщение отправлено', 'Сообщение успешно отправлено мастеру');
    }
</script>

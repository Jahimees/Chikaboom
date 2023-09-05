<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Модальное окно -->
<div class="modal fade" id="clientInfoModal" tabindex="-1" aria-labelledby="clientInfoModalLabel"
     aria-hidden="true">
    <div class="modal-dialog max-w-80">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="clientInfoModalLabel">Информация о клиенте</h5>
                <button id="close-modal-btn" type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Закрыть"></button>
            </div>
            <div id="client-fields" class="modal-body">
                <div class="d-inline-flex col-6">
                    <div class="popup-input middle-box w-80">
                        <div>Имя</div>
                        <label id="first-name-invalid-label-upd" class="invalid-field-label-popup">Имя может содержать
                            только
                            буквы и не должно быть пустым</label>
                        <input id="client-first-name-input-upd" required>
                    </div>
                    <div class="popup-input middle-box w-80">
                        <div>Фамилия</div>
                        <label id="last-name-invalid-label-upd" class="invalid-field-label-popup">Фамилия может содержать
                            только
                            буквы</label>
                        <input id="client-last-name-input-upd">
                    </div>
                </div>
                <div class="popup-input middle-box w-80">
                    <div>Телефон</div>
                    <input id="client-phone-input-upd" style="width: 100%">
                    <span id="error-msg-client-phone-input-upd" class="hide"></span>
                </div>
                <div class="popup-input middle-box w-80">
                    <div>Краткая информация</div>
                    <label id="about-invalid-label-upd" class="invalid-field-label-popup">Информация не должна превышать 300
                        символов!</label>
                    <textarea id="client-about-input-upd" style="width: 100%"></textarea>
                </div>
                <div class="popup-input middle-box w-80">
                    <div id="client-visit-count-upd" style="width: 100%">Количество посещений:</div>
                </div>
                <div class="popup-input middle-box w-80">
                    <div id="client-last-visit-date-upd" style="width: 100%">Последнее посещение:</div>
                </div>
                <table id="client_appointments_table" class="display" style="width: 100%">
                    <thead style="background-color: #523870; color: white">
                    <tr>
                        <th>Услуга</th>
                        <th>Дата</th>
                        <th>Время</th>
                        <th>Продолжительность</th>
                        <th>Цена</th>
                    </tr>
                    </thead>
                    <tbody id="client_appointments_table-tbody">
                    </tbody>
                </table>
                <hr>
            </div>
            <div class="modal-footer">
                <button type="button" class="popup-btn" data-bs-dismiss="modal">Закрыть</button>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        initPhoneCodeWidget("client-phone-input-upd");
    });
</script>

<%@ page contentType="text/html;charset=UTF-8" %>

<div class="popup-bg"></div>

<div class="popup edit-popup">
    <div class="popup-title">
        <h3 id="popup-message-header">Редактирование</h3>
    </div>
    <div class="popup-body">
        <label class="invalid-field-label-popup" id="e-input-data-incorrect-label" style="display: none">
            Проверьте введенные данные</label>
        <div id="field-box-placeholder">
        </div>
        <div class="button-box">
            <div class="popup-btn" onclick="confirmEdit()">
                Подтвердить
            </div>
            <div class="popup-btn" onclick="closePopup('edit-popup')">
                Отменить
            </div>
        </div>
    </div>
</div>

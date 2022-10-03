<%@ page contentType="text/html;charset=UTF-8" %>

<div class="popup-bg"></div>

<div class="popup edit-popup">
    <div class="close-popup" onclick="closePopup('edit-popup')" tabindex="1">
        X
    </div>
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

<script rel="script" src="/js/jquery-ui-1.10.4.custom.min.js"></script>

<script src="/js/countries.js"></script>
<script src="/js/phonecode.js"></script>
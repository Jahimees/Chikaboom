<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup message-popup">
    <div class="close-message-popup" onclick="closePopup('message-popup')" tabindex="1">
        X
    </div>
    <div class="popup-title">
        <h3 id="popup-message-header">Sample text</h3>
    </div>
    <div class="popup-body">
        <div id="popup-message-text" class="message-text-popup">
            Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text
        </div>
        <div style="display: inline-flex">
            <button class="popup-btn" onclick="closePopup('message-popup')">
                Хорошо
            </button>
            <button class="popup-btn" onclick="closePopup('message-popup')" style="display: none">
                Нет
            </button>
        </div>
    </div>
</div>
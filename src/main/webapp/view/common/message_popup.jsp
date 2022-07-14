<%@ page contentType="text/html;charset=UTF-8" %>
<div class="message-popup">
    <div class="close-message-popup" tabindex="1">
        X
    </div>
    <div class="popup-title">
        <h3 id="popup-message-header">Sample text</h3>
    </div>
    <div class="popup-body">
        <div id="popup-message-text" class="message-text">
            Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text
        </div>
        <div style="display: inline-flex">
            <button class="btn btn-dark action-btn-close">
                Хорошо
            </button>
            <button class="btn btn-dark action-btn-close" style="display: none">
                Нет
            </button>
        </div>
    </div>
</div>
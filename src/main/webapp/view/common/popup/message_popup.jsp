<%@ page contentType="text/html;charset=UTF-8" %>

<div class="modal fade" id="messageModal" tabindex="-1" aria-labelledby="messageModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="messageModalLabel">Sample text</h5>
                <div class="close close-button" data-bs-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">X</span>
                </div>
            </div>
            <div class="modal-body">
                <div id="messageModalText" class="message-text-popup">
                    Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text
                </div>
            </div>
            <div class="modal-footer">
                <button id="confirm-message-btn" class="btn btn-primary" data-bs-dismiss="modal">
                    Хорошо
                </button>
                <button id="decline-message-btn" class="btn btn-secondary" data-bs-dismiss="modal"
                        style="display: none">
                    Нет
                </button>
            </div>
        </div>
    </div>
</div>

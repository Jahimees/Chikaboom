<%@ page contentType="text/html;charset=UTF-8" %>

<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editModalLabel">Редактирование</h5>
                <button type="button" class="close" data-bs-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <label class="invalid-field-label-popup" id="e-input-data-incorrect-label" style="display: none">
                    Проверьте введенные данные. Возможно, уже существует пользователь с такими данными
                </label>
                <div id="field-box-placeholder">
                </div>
            </div>
            <div class="modal-footer">
                <div class="btn btn-primary" onclick="confirmEdit()">
                    Подтвердить
                </div>
                <div class="btn btn-secondary" data-bs-dismiss="modal">
                    Отменить
                </div>
            </div>
        </div>
    </div>
</div>

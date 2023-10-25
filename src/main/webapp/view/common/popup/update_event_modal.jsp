<%@ page contentType="text/html;charset=UTF-8" %>

<div class="modal fade" id="updateEventModal" tabindex="-1" aria-labelledby="updateEventModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateEventModalLabel">Удалить запись</h5>
                <button type="button" class="close close-button" data-bs-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">X</span>
                </button>
            </div>
            <form>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="editTitle" class="form-control-label">Название</label>
                        <input type="text" class="form-control" id="editTitle" readonly>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger left" id="delete">Удалить событие</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отменить</button>
<%--                    <button type="button" class="btn btn-success" id="update">Обновить событие</button>--%>
                </div>
            </form>
        </div>
    </div>
</div>
<%@ page contentType="text/html;charset=UTF-8" %>

<div class="modal fade" id="createEventModal" tabindex="-1" aria-labelledby="createEventModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createEventModalLabel">Создать событие</h5>
                <button type="button" class="close close-button" data-bs-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">X</span>
                </button>
            </div>
            <form>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="service-select-ev" class="form-control-label">Название услуги</label>
                        <select class="form-control" id="service-select-ev"></select>
                        <label for="client-select-ev" class="form-control-label">Клиент</label>
                        <select class="form-control" id="client-select-ev"></select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отменить</button>
                    <button type="button" class="btn btn-success" id="submit">Создать событие</button>
                </div>
            </form>
        </div>
    </div>
</div>
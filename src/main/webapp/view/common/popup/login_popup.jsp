<%@ page contentType="text/html;charset=UTF-8" %>

<!-- Modal -->
<div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="loginModalLabel">Авторизация</h5>
                <div class="close close-button" data-bs-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">X</span>
                </div>
            </div>
            <div class="modal-body">
                <form id="login-form">
                    <div class="common-black-text">Имя пользователя</div>
                    <div class="popup-input middle-box w-80">
                        <input id="l-input-phone" class="l-popup-input-field" required
                               valid="false">
                        <span id="error-msg-l-input-phone" class="hide"></span>
                        <input id="l-hidden-input-phone" name="username" hidden="hidden">
                    </div>
                    <div class="common-black-text">Пароль</div>
                    <label class="invalid-field-label-popup" id="l-input-password-empty">Поле не может быть
                        пустым</label>
                    <div class="popup-input middle-box w-80">
                        <input id="l-input-password" class="l-popup-input-field" name="password" type="password"
                               required
                               placeholder="*****" valid="false">
                    </div>
                    <div>
                        <label class="invalid-field-label-popup" id="l-phone-or-password-incorrect">
                            Введенные данные некорректны, попробуйте ещё раз</label>
                    </div>
                    <hr>
                </form>
            </div>
            <a href="/chikaboom/under_construction" class="small-info-popup">Забыли пароль?</a>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                <button id="login-submit-btn" type="button" class="btn btn-dark btn-primary">
                    Войти
                </button>
            </div>
        </div>
    </div>
</div>



<script>
    $(document).ready(() => {
        initPhoneCodeWidget("l-input-phone");
    })
</script>

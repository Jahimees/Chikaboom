<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup-bg"></div>

<div class="popup login-popup">
    <div class="popup-title">
        <h3>Авторизация</h3>
    </div>
    <form id="login-form">
        <div class="popup-body">
            <div class="common-black-text">Имя пользователя</div>
            <div class="popup-input middle-box w-80">
                <input id="l-input-phone" class="popup-input-field" required
                       valid="false">
                <span id="error-msg-l-input-phone" class="hide"></span>
                <input id="l-hidden-input-phone" name="username" hidden="hidden">
            </div>
            <div class="common-black-text">Пароль</div>
            <label class="invalid-field-label-popup" id="l-input-password-empty">Поле не может быть пустым</label>
            <div class="popup-input middle-box w-80">
                <input id="l-input-password" class="popup-input-field" name="password" type="password" required
                       placeholder="*****" valid="false">
            </div>
            <div>
                <label class="invalid-field-label-popup" id="l-phone-or-password-incorrect">
                    Введенные данные некорректны, попробуйте ещё раз</label>
            </div>
            <hr>
            <a href="/chikaboom/under_construction" class="small-info-popup">Забыли пароль?</a>
            <button id="login-submit-btn" type="button" class="btn btn-dark confirm-popup-btn">
                Войти
            </button>
        </div>
    </form>
</div>

<script>
    $(document).ready(() => {
        initPhoneCodeWidget("l-input-phone");
    })
</script>

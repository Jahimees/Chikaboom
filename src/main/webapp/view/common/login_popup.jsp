<%@ page contentType="text/html;charset=UTF-8"%>
<div class="login-popup-bg"></div>

<div class="login-popup">
    <div class="close-login-popup" tabindex="1">
        <em class="fas fa-times"></em>
    </div>
    <div class="popup-title">
        <h3>Авторизация</h3>
    </div>
    <div class="popup-body">
        <form action="/chikaboom/authorization" method="get">
            <label class="invalid-field-label" id="l-input-email-incorrect">Некорректный email</label>
            <label class="invalid-field-label" id="l-input-email-empty">Поле не может быть пустым</label>
            <div class="image-input">
                <img class="small-popup-icon" src="../../image/icon/login_icon.png" alt="no_image">
                <input id="l-input-email" name="email" required placeholder="Введите email">
            </div>
            <label class="invalid-field-label" id="l-input-password-empty">Поле не может быть пустым</label>
            <div class="image-input">
                <img class="small-popup-icon" src="../../image/icon/password_icon.png" alt="no_image">
                <input id="l-input-password" name="password" type="password" required placeholder="Введите пароль">
            </div>

            <hr>

            <a href="#" class="small-info">Забыли пароль?</a>
            <button type="submit" id="confirm-login" class="btn btn-dark confirm-btn" disabled="true">
                Войти
            </button>
        </form>
    </div>
</div>

<%@ page contentType="text/html;charset=UTF-8"%>
<div class="login-popup-bg"></div>

<div class="login-popup">
    <div class="close-login-popup" tabindex="1">
        <i class="fas fa-times"></i>
    </div>
    <div class="popup-title">
        <h3>Авторизация</h3>
    </div>
    <div class="popup-body">
        <form action="/chikaboom?command=authorization" method="post">
            <label class="invalid-field-label" id="l-input-email-incorrect">Некорректный email</label>
            <label class="invalid-field-label" id="l-input-email-empty">Поле не может быть пустым</label>
            <div class="image-input">
                <img class="small-icon" src="../../image/login_icon.png">
                <input id="l-input-email" required placeholder="Введите email">
            </div>
            <label class="invalid-field-label" id="l-input-password-empty">Поле не может быть пустым</label>
            <div class="image-input">
                <img class="small-icon" src="../../image/password_icon.png">
                <input id="l-input-password" type="password" required placeholder="Введите пароль">
            </div>

            <hr>

            <a href="#" class="small-info">Забыли пароль?</a>
            <button type="submit" id="confirm-login" class="btn btn-dark confirm-btn" disabled="true">
                Войти
            </button>
        </form>
    </div>
</div>

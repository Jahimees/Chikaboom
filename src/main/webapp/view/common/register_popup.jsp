<%@ page contentType="text/html;charset=UTF-8" %>
<div class="register-popup-bg"></div>

<div class="register-popup">
    <div class="close-register-popup" tabindex="1">
        <i class="fas fa-times"></i>
    </div>
    <div class="popup-title">
        <h3>Регистрация</h3>
    </div>
    <div class="popup-body">
        <form action="/chikaboom/registration" method="post">
            <label class="invalid-field-label" id="r-input-email-incorrect">Некорректный email</label>
            <label class="invalid-field-label" id="r-input-email-empty">Поле не может быть пустым</label>
            <label class="invalid-field-label" id="r-input-email-short">Email должен быть длиннее 5 символов</label>
            <div class="image-input">
                <img class="small-popup-icon" src="../../image/icon/login_icon.png">
                <input id="r-input-email" name="email" required placeholder="Введите email">
            </div>

            <label class="invalid-field-label" id="r-input-password-empty">Поле не может быть пустым</label>
            <label class="invalid-field-label" id="r-input-password-short">Пароль должен быть длиннее 5 символов</label>
            <div class="image-input">
                <img class="small-popup-icon" src="../../image/icon/password_icon.png">
                <input id="r-input-password" name="password" type="password" required placeholder="Введите пароль">
            </div>

            <label class="invalid-field-label" id="r-input-confirm-password-empty">Поле не может быть пустым</label>
            <label class="invalid-field-label" id="r-input-confirm-password-different">Пароли не совпадают</label>
            <div class="image-input">
                <img class="small-popup-icon" src="../../image/icon/password_icon.png">
                <input id="r-input-confirm-password" type="password" required placeholder="Подтвердите пароль">
            </div>

            <button type="submit" id="confirm-register" class="btn btn-dark confirm-btn" disabled="true">
                Зарегистрироваться
            </button>
        </form>
    </div>
</div>

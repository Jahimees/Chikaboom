<%@ page contentType="text/html;charset=UTF-8"%>
<div class="register-popup-bg"></div>

<div class="register-popup">
    <div class="close-register-popup" tabindex="1">
        <i class="fas fa-times"></i>
    </div>
    <div class="popup-title">
        <h3>Регистрация</h3>
    </div>
    <div class="popup-body">
        <div class="image-input"><img class="small-icon" src="../../image/login_icon.png"><input placeholder="Введите логин"></div>
        <div class="image-input"><img class="small-icon" src="../../image/password_icon.png"><input placeholder="Введите пароль"></div>
        <div class="image-input"><img class="small-icon" src="../../image/password_icon.png"><input placeholder="Подтвердите пароль"></div>
        <input formaction="confirm" type="button" value="Зарегистрироваться" class="btn btn-dark confirm-btn">
    </div>
</div>

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
        <form action="/chikaboom" method="post">
            <div class="image-input"><img class="small-icon" src="../../image/login_icon.png"><input name="login" placeholder="Введите логин"></div>
            <div class="image-input"><img class="small-icon" src="../../image/password_icon.png"><input name="password" placeholder="Введите пароль"></div>
            <hr>
            <a class="small-info">Забыли пароль?</a>
            <button type="submit" name="command" value="authorization" class="btn btn-dark confirm-btn">Войти</button>
        </form>
    </div>
</div>

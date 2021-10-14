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
        <form action="/chikaboom" method="post">
            <div class="image-input"><img class="small-icon" src="../../image/login_icon.png"><input name="login" placeholder="Введите логин" required></div>
            <div class="image-input"><img class="small-icon" src="../../image/password_icon.png"><input type="password" name="password" placeholder="Введите пароль" required></div>
            <div class="image-input"><img class="small-icon" src="../../image/password_icon.png"><input type="password" name="repeat_password" placeholder="Подтвердите пароль" required></div>
            <button type="submit" name="command" value="registration" class="btn btn-dark confirm-btn">Зарегистрироваться</button>
        </form>
    </div>
</div>

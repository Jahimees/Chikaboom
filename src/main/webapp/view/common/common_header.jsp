<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--TODO Стили--%>
<div class="header">
    <div class="header-middle-box">
        <a style="text-align: center; width: 90px" href="/chikaboom/main">
            <img src="/image/logo_gradient.svg">
        </a>
        <div style="font-size: 50px">|</div>
        <a class="header-menu-link" href="/chikaboom/service">
            <b>Услуги</b>
        </a>
        <div style="font-size: 50px">|</div>
        <a class="header-menu-link">
            <b>Акции %</b>
        </a>
        <div style="font-size: 50px">|</div>

        <a class="header-menu-link">
            <b>О нас</b>
        </a>
        <div style="font-size: 50px">|</div>
        <a class="header-menu-link">
            <b>Мастера на карте</b>
        </a>
        <sec:authorize access="isAuthenticated()">
            <div style="font-size: 50px">|</div>
            <a href="/chikaboom/personality/<sec:authentication property="principal.idAccount"/>" class="header-menu-link">
                <b>Личный кабинет</b>
            </a>
        </sec:authorize>
    </div>
    <div class="right-header-box">
        <sec:authorize access="!isAuthenticated()">
            <div class="header-menu-link open-login-popup" onclick="openPopup('login-popup')">
                <b><a href="#login">Войти</a></b>
            </div>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <div class="header-menu-link"><a href="/logout">Выйти</a></div>
        </sec:authorize>

    </div>

</div>

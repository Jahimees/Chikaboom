<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--TODO Стили--%>
<div class="header">
    <div class="header-middle-box">
        <a class="header-menu-link" href="/chikaboom/service/">
            <b>Услуги</b>
        </a>
        <div style="font-size: 50px">|</div>
        <a class="header-menu-link">
            <b>Акции %</b>
        </a>
        <div style="font-size: 50px">|</div>
        <a style="text-align: center; width: 90px" href="/chikaboom/main">
            <img src="/image/logo_gradient.svg">
        </a>
        <div style="font-size: 50px">|</div>
        <a class="header-menu-link">
            <b>О нас</b>
        </a>
        <div style="font-size: 50px">|</div>
        <a class="header-menu-link">
            <b>Мастера на карте</b>
        </a>
    </div>
    <div class="right-header-box">
        <c:choose>
            <c:when test="${sessionScope.idAccount == null}">
                <div class="header-menu-link open-login-popup" onclick="openPopup('login-popup')">
                    <b><a href="#">Войти</a></b>
                </div>
            </c:when>
            <c:otherwise>
                <div class="header-menu-link"><a href="/chikaboom/logout">Выйти</a></div>
            </c:otherwise>
        </c:choose>

    </div>

</div>

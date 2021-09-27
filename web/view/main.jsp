<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/bootstrap/bootstrap.css">
</head>
<body>
<div class="wrapper">
    <div class="mainhead">
        <div class="headmenu">
            <img src="../image/logo.jpg" class="logo">
            <a>Услуги</a>
            <a>Мастера на карте</a>
            <a>О нас</a>
            <a>Избранное</a>
            <a>Мои записи</a>
            <a style="margin-left: auto">Войти</a>
        </div>
        <div>
            <img src="../image/mainbigbanner.jpg" width="100%">
        </div>
    </div>
    <div class="content">
        <div class="leftbar">
            <a class="btn btn-dark">Быстрая запись</a>
            <a class="btn btn-dark">Я мастер</a>
            <a class="btn btn-dark">Курсы</a>
            <input type="button" Value="Быстрая запись"><br>
            <input type="button" Value="Я мастер"><br>
            <input type="button" Value="Курсы"><br>
        </div>
        <jsp:useBean id="accountDAO" scope="session" class="net.chikaboom.dao.AccountDAO"/>
        <c:set var="accountList" value="${accountDAO.findAll()}"/>

        <div class="rightbar">
            <h1>Наши лучшие мастера</h1>
            <div style="display:flex; width:100%">
                <c:forEach var="account" items="${accountList}">
                    <div>${account.idAccount}</div>
                    <div>${account.name}</div>
                    <div>${account.surname}</div>
                    <div>${account.login}</div>
                </c:forEach>
            </div>
            <h1>Талантливые новички</h1>
            <div style="display:flex; width:100%">
                <img src="../image/2sl.jpg">
                <img src="../image/2sl.jpg">
                <img src="../image/2sl.jpg">
                <img src="../image/2sl.jpg">
                <img src="../image/2sl.jpg">
            </div>
        </div>
    </div>
</div>
</body>
</html>	
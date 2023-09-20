<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link href='/css/fullcalendar/fullcalendar.css' rel='stylesheet'/>
<link href='/css/fullcalendar/style.css' rel='stylesheet'/>

<div>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <p class="navbar-brand" id="todaysDate"></p>
        </div>
    </nav>

    <div class="container-fluid row">
        <div id='calendar1' class='calendar col-md-8'></div>
        <div id='calendar2' class='calendar col-md-4'></div>
    </div>
</div>

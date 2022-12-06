<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset='utf-8'/>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href='/css/fullcalendar/bootstrap.min.css' rel='stylesheet' />
    <link href='/css/fullcalendar/fullcalendar.css' rel='stylesheet' />
    <link href='/css/fullcalendar/style.css' rel='stylesheet' />

    <script src='/js/fullcalendar/vendor/jquery.min.js'></script>
    <script src='/js/fullcalendar/vendor/moment.min.js'></script>
    <script src='/js/fullcalendar/vendor/bootstrap.min.js'></script>
    <script src='/js/fullcalendar/vendor/fullcalendar.js'></script>
    <script src='/js/fullcalendar/events.js'></script>
    <script src='/js/fullcalendar/calendar.js'></script>

    <link href="/css/vendor/bootstrap-ie7.css" rel="stylesheet">

</head>

<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <p class="navbar-brand" id="todaysDate"></p>
    </div>
</nav>

<div class="container-fluid row">
    <div id='calendar1' class='calendar col-md-8'></div>
    <div id='calendar2' class='calendar col-md-4'></div>
</div>

<div class="modal fade" id="newEvent" role="dialog" aria-labelledby="eventFormLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="newEvent">Новая запись</h4>
            </div>
            <form>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="title" class="form-control-label">Название</label>
                        <input type="text" class="form-control" id="title">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Отменить</button>
                    <button type="button" class="btn btn-success" id="submit">Создать событие</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="editEvent" role="dialog" aria-labelledby="eventFormLabel" aria-hidden="true" data-persist="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="editEvent">Обновить запись</h4>
            </div>
            <form>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="title" class="form-control-label">Название</label>
                        <input type="text" class="form-control" id="editTitle">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger left" id="delete">Удалить событие</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Отменить</button>
                    <button type="button" class="btn btn-success" id="update">Обновить событие</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>

</html>


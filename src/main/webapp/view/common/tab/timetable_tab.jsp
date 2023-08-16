<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src='/js/fullcalendar/vendor/jquery.min.js'></script>
<script src='/js/fullcalendar/vendor/moment.min.js'></script>
<script src='/js/fullcalendar/vendor/bootstrap.min.js'></script>
<script src='/js/fullcalendar/vendor/fullcalendar.js'></script>
<script src='/js/fullcalendar/events.js'></script>
<script src='/js/fullcalendar/calendar.js'></script>
<body>

<div id="timetable-placeholder" style="background-color: white">

</div>
<div class="medium-text">Определите ваш рабочий день</div>
<div id="current-working-day-duration" class="medium-text"></div>
<label id="working-day-warn" class="invalid-field-label-popup small-text"
       style="display: none">Начало рабочего дня не может быть позже конца рабочего дня!</label>
<div class="row medium-text">
    <div class="col-4">
        <label for="working-day-start">Начало рабочего дня</label>
        <label id="working-day-start-warn" class="invalid-field-label-popup small-text" for="working-day-start"
               style="display: none">Не соответствует
            шаблону!</label>
        <input id="working-day-start" type="text" placeholder="9:00">
    </div>
    <div class="col-4">
        <label for="working-day-end">Конец рабочего дня</label>
        <label id="working-day-end-warn" class="invalid-field-label-popup small-text" for="working-day-end"
               style="display: none">Не соответствует
            шаблону!</label>
        <input id="working-day-end" type="text" placeholder="17:00">
    </div>
    <input id="save-work-time-btn" type="button" class="button" value="Сохранить">
</div>
</body>

<script>
    var workingDays;

    $(document).ready(function () {
        loadWorkingDaysData(${idAccount});
    });
</script>

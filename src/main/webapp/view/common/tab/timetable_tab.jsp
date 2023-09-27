<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src='/js/fullcalendar/vendor/moment.min.js'></script>
<script src='/js/fullcalendar/vendor/fullcalendar.js'></script>
<script src='/js/fullcalendar/events.js'></script>
<script src='/js/fullcalendar/calendar.js'></script>
<script src='/js/appointment.js'></script>
<script src='/js/service.js'></script>
<script src='/js/client.js'></script>

<div id="timetable-placeholder" style="background-color: white; position: sticky">
</div>

<script>
    $(document).ready(function () {
        loadWorkingDaysDataAndLoadCalendar(${idAccount});
        reloadWorkingDayDuration(accountJson.accountSettings);
    });
</script>

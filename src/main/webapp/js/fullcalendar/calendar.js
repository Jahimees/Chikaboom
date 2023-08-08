$(function () {
    showTodaysDate();
    loadMasterAppointments();
    // initializeCalendar();

});

var initializeCalendar = function (appointmentsForCalendar) {
    $('.calendar').fullCalendar({
        editable: true,
        eventLimit: true, // allow "more" link when too many events
        // create events
        events: appointmentsForCalendar,
        firstDay: 1,
        defaultTimedEventDuration: '00:30:00',
        forceEventDuration: true,
        eventBackgroundColor: '#5F4E7D',
        editable: false,
        height: screen.height - 160,
        timezone: 'Russia/Moscow',
    });
}

var obj;
var obj1;

var loadMasterAppointments = function () {
    let appointmentsForCalendar = [];
    $.ajax({
        type: "get",
        url: "/chikaboom/appointment/" + accountJson.idAccount,
        contentType: "application/json",
        dataType: "json",
        data: {},
        success: function (masterAppointments) {
            masterAppointments.forEach(function (masterAppointment) {
                let title = masterAppointment.service.name + " - " + masterAppointment.clientAccount.username;
                let appointmentDate = new Date(masterAppointment.appointmentDate);

                let splittedAppointmentTime = masterAppointment.appointmentTime.split(":");
                appointmentDate.setHours(splittedAppointmentTime[0]);
                appointmentDate.setMinutes(splittedAppointmentTime[1]);

                let serviceTime = masterAppointment.service.time;
                let serviceDurationTime = serviceTime.replace(' минут', '').split(' час');
                let duration;

                if (serviceDurationTime.length === 1) {
                    duration = 1;
                } else {
                    serviceDurationTime[1] = serviceDurationTime[1].replace('а', '');

                    duration = serviceDurationTime[0] * 60;
                    duration += serviceDurationTime[1] === '' ? 0 : 30;
                }

                let appointmentEnd = new Date(masterAppointment.appointmentDate);
                appointmentEnd.setHours(splittedAppointmentTime[0]);
                appointmentEnd.setMinutes(splittedAppointmentTime[1] + duration);

                let appointmentObj = {
                    id: masterAppointment.idAppointment,
                    title: title,
                    start: appointmentDate,
                    end: appointmentEnd
                }
                appointmentsForCalendar.push(appointmentObj)
            })

            initializeCalendar(appointmentsForCalendar);
            getCalendars();
            initializeRightCalendar();
            initializeLeftCalendar();
            disableEnter();
        }
    })

    // return appointmentsForCalendar;
}


/* --------------------------initialize calendar-------------------------- */

var initializeRightCalendar = function () {
    $cal2.fullCalendar('changeView', 'agendaDay');

    $cal2.fullCalendar('option', {
        firstDay: 1,
        slotEventOverlap: false,
        allDaySlot: false,
        header: {
            right: 'prev,next today'
        },
        selectable: true,
        selectHelper: true,
        select: function (start, end) {
            newEvent(start, end);
        },
        eventClick: function (calEvent, jsEvent, view) {
            obj = calEvent;
            editEvent(calEvent);
        },
    });
}

//Значения из базы о рабочих днях
// var workingDays = [];

function addOrRemoveWorkingDate(date) {
    var flag = true;

    workingDays.workingDays.forEach(function (workDate) {
        var currentDate = new Date(Date.parse(JSON.parse(JSON.stringify(date))))
        var workingDate = new Date(Date.parse(JSON.parse(JSON.stringify(workDate))))
        if (currentDate.getDate() === workingDate.getDate()
            && currentDate.getMonth() === workingDate.getMonth()
            && currentDate.getFullYear() === workingDate.getFullYear()) {

            var index = workingDays.workingDays.indexOf(workDate);
            workingDays.workingDays.splice(index, 1);
            flag = false;
        }
    })

    if (flag) {
        var currentDateObj = new Date(date);
        workingDays.workingDays.push(currentDateObj);
    }

    saveWorkingDays();

    initializeLeftCalendar();
}

function saveWorkingDays() {
    workingDays.workingDays = JSON.stringify(workingDays.workingDays);

    $.ajax({
        type: "POST",
        url: "/chikaboom/personality/" + accountJson.idAccount + "/timetable",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(workingDays),
        error: function () {
            alert("Данные не сохранены в базу данных. Неизвестная ошибка");
        }
    });

    workingDays.workingDays = JSON.parse(workingDays.workingDays);
}


/* -------------------manage cal1 (left pane)------------------- */
var initializeLeftCalendar = function () {
    $cal1.fullCalendar('option', {

        firstDay: 1,
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek'
        },
        navLinks: false,
        dayClick: function (date) {
            cal2GoTo(date);
        },
        eventClick: function (calEvent) {
            cal2GoTo(calEvent.start);
        },
        dayRender: function (date, cell) {
            if (checkDate(date)) {
                cell.css("background-color", "white");
            } else {
                cell.css("background-color", "#F4F4F4");
            }
        },

    });

}

function checkDate(date) {
    var flag = false;
    workingDays.workingDays.forEach(function (workDate) {
        var currentDate = new Date(Date.parse(JSON.parse(JSON.stringify(date))))
        var workingDate = new Date(Date.parse(JSON.parse(JSON.stringify(workDate))))

        if (currentDate.getDate() === workingDate.getDate()
            && currentDate.getMonth() === workingDate.getMonth()
            && currentDate.getFullYear() === workingDate.getFullYear()) {
            flag = true;
        }
    })
    return flag;
}

var getCalendars = function () {
    $cal = $('.calendar');
    $cal1 = $('#calendar1');
    $cal2 = $('#calendar2');
}

var cal2GoTo = function (date) {
    $cal2.fullCalendar('gotoDate', date);
}


var newEvent = function (start, nd) {
    $('input#title').val("");
    $('#newEvent').modal('show');
    $('#submit').unbind();
    $('#submit').on('click', function () {
        var title = $('input#title').val();

        if (title) {
            var eventData = {
                title: title,
                start: JSON.parse(JSON.stringify(start)),
                end: nd
            };
            $cal.fullCalendar('renderEvent', eventData, true);
            $('#newEvent').modal('hide');
        } else {
            alert("Название не может быть пустым")
        }
    });
}

var editEvent = function (calEvent) {
    $('input#editTitle').val(calEvent.title);
    $('#editEvent').modal('show');
    $('#update').unbind();
    $('#update').on('click', function () {
        var title = $('input#editTitle').val();
        $('#editEvent').modal('hide');
        var eventData;
        if (title) {
            calEvent.title = title
            $cal.fullCalendar('updateEvent', calEvent);
        } else {
            alert("Title can't be blank. Please try again.")
        }
    });
    $('#delete').on('click', function () {
        $('#delete').unbind();
        if (calEvent._id.includes("_fc")) {
            $cal1.fullCalendar('removeEvents', [getCal1Id(calEvent._id)]);
            $cal2.fullCalendar('removeEvents', [calEvent._id]);
        } else {
            $cal.fullCalendar('removeEvents', [calEvent._id]);
        }
        $('#editEvent').modal('hide');
    });
}

/* --------------------------load date in navbar-------------------------- */
var showTodaysDate = function () {
    var n = new Date();
    var y = n.getFullYear();
    var m = n.getMonth() + 1;
    var d = n.getDate();
    $("#todaysDate").html("Сегодня " + d + "/" + m + "/" + y);
};

/* full calendar gives newly created given different ids in month/week view
    and day view. create/edit event in day (right) view, so correct for
    id change to update in month/week (left)
  */
var getCal1Id = function (cal2Id) {
    var num = cal2Id.replace('_fc', '') - 1;
    var id = "_fc" + num;
    return id;
}

var disableEnter = function () {
    $('body').bind("keypress", function (e) {
        if (e.keyCode == 13) {
            e.preventDefault();
            return false;
        }
    });
}

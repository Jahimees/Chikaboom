<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Записи на мои услуги</div>

        <div id="appointment-placeholder" class="d-block w-100">
            <div id="income-appointment-header" class="service-row row" style="background-color: #5f4e7d; color: white">
                <div class="col-2">Название услуги</div>
                <div class="col-2">Дата услуги</div>
                <div class="col-1">Время записи</div>
                <div class="col-1">Цена услуги</div>
                <div class="col-1">Время на услугу</div>
                <div class="col-2">Телефон клиента</div>
                <div class="col-2">Имя клиента</div>
            </div>
        </div>
    </div>
</div>

<script>

    function loadIncomeAppointmentsData() {
        $.ajax({
            method: "get",
            url: "/accounts/${idAccount}/income-appointments",
            contentType: "application/json",
            dataType: "json",
            success: function (json) {
                devAppointments = JSON.parse(JSON.stringify(json));
                fillAppointmentsTable(json)
            }
        })
    }

    $(document).ready(function () {
       loadIncomeAppointmentsData();
    })

    function fillAppointmentsTable(appointmentsJSON) {
        appointmentsJSON.forEach(function (appointment) {
            let row = document.createElement("div");
            row.setAttribute("class", "service-row row");
            row.setAttribute("style", "color: black");

            let serviceNameDiv = document.createElement("div");
            serviceNameDiv.setAttribute("class", "col-2")
            serviceNameDiv.innerHTML = appointment.service.name;

            let dateDiv = document.createElement("div");
            dateDiv.setAttribute("class", "col-2");
            dateDiv.innerHTML = appointment.appointmentDate.substring(8, 10) + "." + appointment.appointmentDate.substring(5, 7) + "." + appointment.appointmentDate.substring(0, 4)

            let appointmentTimeDiv = document.createElement("div");
            appointmentTimeDiv.setAttribute("class", "col-1");
            appointmentTimeDiv.innerHTML = appointment.appointmentTime;

            let priceDiv = document.createElement("div");
            priceDiv.setAttribute("class", "col-1");
            priceDiv.innerHTML = appointment.price;

            let timeDiv = document.createElement("div");
            timeDiv.setAttribute("class", "col-1");
            timeDiv.innerHTML = appointment.service.time;

            let clientPhoneDiv = document.createElement("div");
            clientPhoneDiv.setAttribute("class", "col-2")
            clientPhoneDiv.innerHTML = appointment.clientAccount.phoneCode.phoneCode + " " + appointment.clientAccount.phone;

            let clientNameDiv = document.createElement("div");
            clientNameDiv.setAttribute("class", "col-2")
            clientNameDiv.innerHTML = appointment.clientAccount.username;

            let deleteBtnDiv = document.createElement("div");
            deleteBtnDiv.setAttribute("class", "col-1")
            deleteBtnDiv.setAttribute("onclick",
                "callConfirmDeletePopup(" + appointment.masterAccount.idAccount + "," + appointment.idAppointment + ")")

            let img = document.createElement("img");
            img.setAttribute("src", "/image/icon/cross_icon.svg");
            img.setAttribute("style", "width: 15px");

            deleteBtnDiv.appendChild(img);

            row.appendChild(serviceNameDiv);
            row.appendChild(dateDiv);
            row.appendChild(appointmentTimeDiv);
            row.appendChild(priceDiv);
            row.appendChild(timeDiv);
            row.appendChild(clientPhoneDiv);
            row.appendChild(clientNameDiv);
            row.appendChild(deleteBtnDiv);

            $("#income-appointment-header")[0].appendChild(row);
        })
    }

    function callConfirmDeletePopup(idAccountMaster, idAppointment) {
        repairDefaultMessagePopup();
        $("#decline-message-btn")[0].style.display = "block";
        $("#confirm-message-btn")[0].setAttribute("onclick", "deleteAppointment(" + idAccountMaster + "," + idAppointment + ")");

        $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить запись на услугу?";
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
        openPopup("message-popup");
    }

    function deleteAppointment(idAccountMaster, idAppointment) {
        $.ajax({
            type: "delete",
            url: "/chikaboom/appointment/" + idAccountMaster + "/" + idAppointment,
            contentType: "application/text",
            dataType: "text",
            success: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Запись успешно удалена!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удалено";
                openPopup('message-popup');
                $("#confirm-message-btn")[0].setAttribute("onclick", "closePopup('message-popup'), loadIncomeAppointmentsData()");
            },
            error: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Невозможно удалить запись!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');

            }
        })
    }
</script>

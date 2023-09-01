<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="menu-box-horizontal">
    <sec:authorize access="hasRole('ROLE_MASTER')">
        <div id="client-tab" class="horizontal-menu-child" selected="false">
            <div class="horizontal-menu-text"><a href="#client">Клиенты</a></div>
        </div>
    </sec:authorize>
</div>

<div id="client-tab-placeholder" class="setting-content-placeholder">

    <div class="content">
        <div>
            <div class="big-text">Мои клиенты</div>
            <div id="create-client-btn" class="violet-button margin-10px-0" data-bs-toggle="modal"
                 data-bs-target="#createClientModal">+Новый клиент
            </div>
            <div class="d-block w-100">
                <table id="default_table" class="display" style="width: 100%">
                    <thead style="background-color: #523870; color: white">
                    <tr>
                        <th>Имя пользователя</th>
                        <th>Номер телефона</th>
                        <th>Количество посещений</th>
                        <th>Последняя запись</th>
                    </tr>
                    </thead>
                    <tbody id="default_table-tbody">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.js"></script>
<script>
    $(document).ready(function () {
        selectCurrentTab($("#client-tab")[0])

        setTimeout(function () {
            initDataTable("appointment-client");
            initDataTable("client-appointments", 'client_appointments_table')
            loadClientInformation(${idAccount});
        }, 100); //TODO Плохо. Но не придумал по другому. вставка скрипта datatables должна быть тут.

        $("#create-client-btn").on("click", function () {
            $(function () {
                $('#client-phone-input').phonecode({
                    preferCo: 'by',
                    id: 'client-create'
                })
            });
        });
    });
</script>

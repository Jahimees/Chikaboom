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

            <div class="d-block w-100">
                <table id="default_table" class="display" style="width: 100%">
                    <thead style="background-color: #523870; color: white">
                    <tr>
                        <th>Имя пользователя</th>
                        <th>Номер телефона</th>
                        <th>Количество посещений</th>
                        <th>Последнее посещение</th>
                    </tr>
                    </thead>
                    <tbody id="client_table-tbody">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.js"></script>
<script src="/js/client.js"></script>
<script src="/js/tab.js"></script>

<script>
    $(document).ready(function () {
        setTimeout(function () {
            initDataTable();
            loadClientInformation(${idAccount});
        }, 10)
    })
</script>

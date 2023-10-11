<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="content">
        <sec:authorize access="hasRole('ROLE_MASTER')">
            <div class="input-box">
                <div class="common-black-text padding-0-0-20-px">
                    Рабочий график
                </div>

                <div class="small-pink-text">
                    Время рабочего дня по умолчанию:
                </div>
                <input id="default-working-time-placeholder" onclick="openEditDefaultWorkingTimePopup()"
                       class="small-black-text bordered-text setting-placeholder-text-decorator"
                       placeholder="" readonly>


            </div>
        </sec:authorize>

</div>


<script>
    $(document).ready(function () {
        $("#default-working-time-placeholder")
            .attr("placeholder", accountFacadeJson.accountSettingsFacade.defaultWorkingDayStart +
                " - " + accountFacadeJson.accountSettingsFacade.defaultWorkingDayEnd);
    });
</script>

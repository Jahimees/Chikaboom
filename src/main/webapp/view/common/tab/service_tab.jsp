<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="general-setting-tab"  class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Услуги</a></div>
    </div>
</div>

<div id="service-type-content-placeholder" class="setting-content-placeholder">
</div>

<script src="../../../../js/service.js"></script>
<script>

    $(document).ready(function () {
        loadConcreteServiceTab("general", "${idAccount}");
    })
</script>
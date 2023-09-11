<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="general-setting-tab"  class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Услуги</a></div>
    </div>
</div>

<div id="service-type-content-placeholder" class="setting-content-placeholder">

</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/css/suggestions.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/suggestions-jquery@21.12.0/dist/js/jquery.suggestions.min.js"></script>
<script src="/js/service.js"></script>
<script>

    $(document).ready(function () {
        loadConcreteServiceTab("general", "${idAccount}");
        loadServiceSubtypes();
    })
</script>
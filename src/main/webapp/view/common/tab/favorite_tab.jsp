<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="favorite-tab"  class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Избранное</a></div>
    </div>
</div>

<div id="favorite-content-placeholder" class="inner-content-placeholder">
    <div class="content">
        <div class="big-medium-black-text">Избранные мастера</div>
        <table id="favorites_table" class="display" style="width: 100%">
            <thead style="background-color: #523870; color: white">
            <tr>
                <th>Фото</th>
                <th>Имя</th>
                <th>Номер телефона</th>
                <th>Удалить</th>
            </tr>
            </thead>
            <tbody id="favorites_table-tbody">
            </tbody>
        </table>
    </div>
</div>

<link href="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.css" rel="stylesheet"/>
<link href="../../../../css/favorite.css" rel="stylesheet"/>
<script src="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.js"></script>
<script src="../../../../js/favorite.js"></script>
<script>
    $(document).ready(() => {
        setTimeout(function () {
            loadFavoritesTable(${idAccount});
        }, 100);
    })
</script>
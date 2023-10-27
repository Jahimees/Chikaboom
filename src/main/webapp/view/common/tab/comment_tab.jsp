<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="favorite-tab" class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Избранное</a></div>
    </div>
</div>

<div id="favorite-content-placeholder" class="inner-content-placeholder">
    <div class="content">
        <div class="big-medium-black-text">Оценки и комментарии</div>
        <table id="comments_table" class="display" style="width: 100%">
            <thead style="background-color: #523870; color: white">
            <tr>
                <th>Имя</th>
                <th>Дата</th>
                <th>Текст</th>
                <th>Оценка</th>
            </tr>
            </thead>
            <tbody id="comments_table-tbody">
            </tbody>
        </table>
    </div>
</div>

<script src="https://kit.fontawesome.com/1fc4ea1c6a.js"></script>
<link href="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.css" rel="stylesheet"/>
<script src="https://cdn.datatables.net/v/dt/dt-1.13.6/fh-3.4.0/sb-1.5.0/sp-2.2.0/datatables.min.js"></script>

<script>
    $(document).ready(() => {
        const commentsJson = doLoadComments();

        setTimeout(() => {
                initCommentsDataTable(commentsJson)
            }, 100
        )
    })

    function doLoadComments() {
        let commentsJson;
        $.ajax({
            method: "get",
            url: "/accounts/" + ${idAccount} +"/comments",
            contentType: "application/json",
            async: false,
            success: (data) => {
                commentsJson = data;
            },
            error: () => {
                callMessagePopup("Ошибка", "Невозможно загрузить комментарии")
            }
        })

        return commentsJson;
    }

    function initCommentsDataTable(commentsJson) {
        const tableName = "comments"
        const $dataTable = $("#comments_table");

        destroyAndInitDataTable(tableName, $dataTable);

        commentsJson.forEach(comment => {
            if (typeof comment.accountClientFacade != "undefined"
                && typeof comment.accountClientFacade.userDetailsFacade != "undefined") {
                let firstName = comment.accountClientFacade.userDetailsFacade.firstName;
                let lastName = comment.accountClientFacade.userDetailsFacade.lastName;
                let totalName = (typeof firstName != "undefined" ? firstName.trim() + " " : "") +
                    (typeof lastName != "undefined" ? lastName.trim() : "");
                totalName = secureCleanValue(totalName !== "" ? totalName : comment.accountClientFacade.username);

                const date = new Date(comment.date).toLocaleDateString('ru');
                const text = secureCleanValue(comment.text);
                const isGood = comment.good ? "<i class='fas fa-thumbs-up table-like'></i>" :
                    "<i class='fas fa-thumbs-down table-dislike'></i>";

                const rowNode = $dataTable.DataTable().row.add([
                    totalName,
                    date,
                    text,
                    isGood
                ])
                    .draw()
                    .node();
            }
        })

    }
</script>

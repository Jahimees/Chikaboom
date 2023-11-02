<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="menu-box-horizontal">
    <div id="favorite-tab" class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Избранное</a></div>
    </div>
</div>

<div id="favorite-content-placeholder" class="inner-content-placeholder">
    <div class="content">
        <div class="big-medium-black-text">Чаты</div>
        <div id="chats-placeholder">
            <div style="
    display: flex;
    width: 500px;
    background-color: white;
    border-radius: 15px;
    padding: 10px;
margin-top: 20px">
                <div><img src="../../../image/user/13/avatar.jpeg" style="width: 100px;
    height: 100px;
    object-fit: cover;
    border-radius: 100px;
"/></div>
                <div style="margin: auto;
    font-size: 20px;">Имя петушымя +1
                </div>
            </div>
            <div style="
    display: flex;
    width: 500px;
    background-color: white;
    border-radius: 15px;
    padding: 10px;margin-top: 20px
">
                <div><img src="../../../image/user/13/avatar.jpeg" style="width: 100px;
    height: 100px;
    object-fit: cover;
    border-radius: 100px"/></div>
                <div style="margin: auto;
    font-size: 20px;">Имя петушымя +1
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(() => {
        let messages;
        $.ajax({
            method: "get",
            url: "/accounts/${idAccount}/messages",
            contentType: "application/json",
            async: false,
            success: (data) => {
                messages = data;
                console.log(data)
            },
            error: () => {
                callMessagePopup("Ошибка", "Невозможно загрузить сообщения")
            }
        })

        const chatAccs = new Map;
        const chats = new Map;
        messages.forEach((message) => {
            if (message.senderFacade.idAccount != accountFacadeJson.idAccount) {
                if (!chatAccs.get(message.senderFacade.idAccount)) {
                    chatAccs.set(message.senderFacade.idAccount, message.senderFacade);
                    chats.set(message.senderFacade.idAccount, 0)
                }

                if (typeof message.messageStatusFacade != "undefined" &&
                    message.messageStatusFacade.name === "not_viewed") {
                    chats.set(message.senderFacade.idAccount, 1 + chats.get(message.senderFacade.idAccount));
                }
            } else {
                if (!chatAccs.get(message.recipientFacade)) {
                    chatAccs.set(message.recipientFacade.idAccount, message.recipientFacade);
                    chats.set(message.recipientFacade.idAccount, 0);
                }
            }
        })

        for (let i = 0; i < chats.size; i++) {
            let div
        }

    })
</script>



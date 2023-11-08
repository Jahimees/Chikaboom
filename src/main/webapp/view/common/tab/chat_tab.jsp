<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="../../../css/chat.css">
<script src='../../../../js/client.js'></script>
<script src='../../../../js/favorite.js'></script>

<div class="menu-box-horizontal">
    <div id="chat-tab" class="horizontal-menu-child" selected="true">
        <div class="horizontal-menu-text"><a href="#">Чаты</a></div>
    </div>
</div>

<div id="chat-content-placeholder" style="height: 100%" class="inner-content-placeholder">
    <div class="content" style="height: 100%">
        <div class="big-medium-black-text">Чаты</div>
        <div class="full-width-inline-box" style="height: 80%">
            <div id="chats-placeholder" style="overflow: auto">

            </div>
            <div id="messages-placeholder" current-chat="" class="messages-placeholder">
                <div class="messages-container"></div>
                <div id="user-text-placeholder" style="position: relative; height: 100px">
                    <textarea class='message-textarea'></textarea>
                    <img class='send-btn' src='../../../image/test/send.png'/>

                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(() => {
      loadMessages(${idAccount});
      initChats();
    })
</script>



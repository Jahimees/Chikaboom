{
    let messageCount = 0;
    let messagesCache;

    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/subscription'
    });

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/accounts/' + accountFacadeJson.idAccount + '/queue/messages', (greeting) => {

            const $messagesContainer = $(".messages-container");

            let messageJson = JSON.parse(greeting.body);

            if (+$("#messages-placeholder").attr('current-chat') === messageJson.senderFacade.idAccount) {
                $messagesContainer.append(
                    getMessageHtml(extractTotalName(messageJson.senderFacade), messageJson.message,
                        true, messageJson.dateTime));
                $messagesContainer[0].scrollBy(0, Number.MAX_SAFE_INTEGER)
            }

            messagesCache.push(messageJson);
            recountNotViewedMessages(accountFacadeJson.idAccount)
        });
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    function connect() {
        stompClient.activate();
    }

    function disconnect() {
        stompClient.deactivate();
        console.log("Disconnected");
    }

    function sendMessage(idRecipient, message) {
        stompClient.publish({
            destination: "/app/chat/" + idRecipient,
            body: JSON.stringify({
                senderFacade: {
                    idAccount: accountFacadeJson.idAccount
                },
                recipientFacade: {
                    idAccount: idRecipient
                },
                message: message,
                dateTime: new Date(),
            })
        });

        // ??? add new ,message to cache
        $(".messages-container").append(
            getMessageHtml(extractTotalName(accountFacadeJson), message, false));
    }

    function loadMessages(idAccount) {
        $.ajax({
            method: "get",
            url: "/accounts/" + idAccount + "/messages",
            contentType: "application/json",
            async: false,
            success: (data) => {
                messagesCache = data;
            },
            error: () => {
                callMessagePopup("Ошибка", "Невозможно загрузить сообщения")
            }
        })

        recountNotViewedMessages(idAccount);
    }

    function initChats() {
        const chatAccs = new Map;
        const chats = new Map;
        messagesCache.forEach((message) => {
            if (message.senderFacade.idAccount !== accountFacadeJson.idAccount) {
                if (!chatAccs.get(message.senderFacade.idAccount)) {
                    chatAccs.set(message.senderFacade.idAccount, message.senderFacade);
                    chats.set(message.senderFacade.idAccount, 0)
                }

                if (typeof message.messageStatusFacade != "undefined" &&
                    message.messageStatusFacade.name === "not_viewed") {

                    chats.set(message.senderFacade.idAccount, 1 + chats.get(message.senderFacade.idAccount));
                }
            } else {
                if (!chatAccs.get(message.recipientFacade.idAccount)) {
                    chatAccs.set(message.recipientFacade.idAccount, message.recipientFacade);
                    chats.set(message.recipientFacade.idAccount, 0);
                }
            }
        })


        const $chatsPlaceholder = $("#chats-placeholder");
        $chatsPlaceholder.html('');
        chatAccs.forEach(acc => {
            let totalName = extractTotalName(acc);

            const divChatCard = $("<div id='chat-" + acc.idAccount + "' class='chat-card'></div>");
            const divAvatar = $("<div><img src='../../../image/user/" + acc.idAccount + "/avatar.jpeg' " +
                "class='smallest-avatar-image'/></div>");
            const divName = $("<div style='margin: auto; font-size: 20px'>" + totalName +
                "<b> +" + chats.get(acc.idAccount) + "</b></div>")

            divChatCard.append(divAvatar);
            divChatCard.append(divName);

            $chatsPlaceholder.append(divChatCard);


            //////////////INIT DIALOG
            const $currentChat = $("#chat-" + acc.idAccount);

            ////////BIND DIALOG
            $currentChat.unbind()
            $currentChat.on('click', function () {
                const $messagesContainer = $(".messages-container");
                $("#messages-placeholder").attr('current-chat', acc.idAccount);

                $messagesContainer.html('');
                console.log(messagesCache)
                messagesCache.forEach((message) => {
                        const totalName = extractTotalName(message.senderFacade);
                        if (message.senderFacade.idAccount === acc.idAccount
                            || message.recipientFacade.idAccount === acc.idAccount) {

                            const isLeft = message.senderFacade.idAccount === acc.idAccount
                            $messagesContainer.append(
                                getMessageHtml(totalName, message.message,
                                    isLeft,
                                    message.dateTime)
                            );
                        }
                    }
                )

                const $sendBtn = $(".send-btn");
                const $messageTextarea = $(".message-textarea");
                $sendBtn.unbind();
                $sendBtn.on('click', () => {
                    const text = $messageTextarea.val();

                    if (text.trim() !== '' &&
                        text.trim().length <= 500) {
                        sendMessage(acc.idAccount, text);
                    }
                    $messageTextarea.val('');
                    $messagesContainer[0].scrollBy(0, Number.MAX_SAFE_INTEGER)
                })

                $messageTextarea.unbind()
                $messageTextarea.on('keyup', (ev) => {
                    if (ev.originalEvent.keyCode === 13) {
                        $sendBtn.click()
                    }
                })

                $messagesContainer[0].scrollBy(0, Number.MAX_SAFE_INTEGER)
            })
        })
    }

    function getMessageHtml(totalName, message, isLeft, dateTime) {
        let date = typeof dateTime === "undefined" ? new Date() : new Date(dateTime);
        return "<div class='message-text-" + (isLeft ? "left" : "right") + "'><st>" + totalName + ": " +
            date.toLocaleString('ru', {
                hour: '2-digit',
                minute: '2-digit',
                day: '2-digit',
                month: '2-digit',
                year: '2-digit'
            }) +
            "</st> " + message + "</div>"
    }

    function recountNotViewedMessages(idAccount) {
        messageCount = 0;
        messagesCache.forEach(message => {
            if (message.senderFacade.idAccount != idAccount
            && message.messageStatusFacade.name === 'not_viewed') {
                messageCount++;
            }
        })

        $("#messages-btn > div > a").text('Сообщения +' + messageCount);
    }
}
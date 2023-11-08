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

            const messageCountPlaceholder = $("#chat-" + messageJson.senderFacade.idAccount + " > div > b");
            const messageCount = 1 + +messageCountPlaceholder.text();
            messageCountPlaceholder.text(" +" + messageCount);
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
        let messageObjToSend = {
            senderFacade: {
                idAccount: accountFacadeJson.idAccount
            },
            recipientFacade: {
                idAccount: idRecipient
            },
            message: message,
            dateTime: new Date()
        }

        stompClient.publish({
            destination: "/app/chat/" + idRecipient,
            body: JSON.stringify(messageObjToSend)
        });

        messageObjToSend.senderFacade = accountFacadeJson;

        messagesCache.push(messageObjToSend)
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

        const clientsData = loadClients(accountFacadeJson.idAccount);
        clientsData.forEach(client => {
            if (typeof client.masterOwnerFacade === "undefined") {
                let account;
                $.ajax({
                    method: "get",
                    url: "/accounts?idUserDetails=" + client.idUserDetails,
                    contentType: "application/json",
                    async: false,
                    success: (accountData) => {
                        account = accountData;
                    },
                    error: () => {
                        callMessagePopup("Ошибка", "Невозможно загрузить чаты клиентов");
                    }
                })

                if (!chatAccs.get(account.idAccount)) {
                    chatAccs.set(account.idAccount, account);
                    chats.set(account.idAccount, 0);
                }
            }
        })

        let favoriteAccounts = loadFavorites(accountFacadeJson.idAccount);
        favoriteAccounts.forEach(favorite => {
            if (!chatAccs.get(favorite.favoriteMasterFacade.idAccount)) {
                chatAccs.set(favorite.favoriteMasterFacade.idAccount, favorite.favoriteMasterFacade);
                chats.set(favorite.favoriteMasterFacade.idAccount, 0);
            }
        })

        const $chatsPlaceholder = $("#chats-placeholder");
        $chatsPlaceholder.html('');
        chatAccs.forEach(acc => {
            let totalName = extractTotalName(acc);

            let messageCount = chats.get(acc.idAccount) !== 0 ? "+" + chats.get(acc.idAccount) : '';

            const divChatCard = $("<div id='chat-" + acc.idAccount + "' class='chat-card'></div>");
            const divAvatar = $("<div><img src='../../../image/user/" + acc.idAccount + "/avatar.jpeg' " +
                "onerror=\"this.src='../../../image/user/no_photo.jpg'\"" +
                "class='smallest-avatar-image'/></div>");
            const divName = $("<div style='margin: auto; font-size: 20px'>" + totalName +
                "<b> " + messageCount + "</b></div>")

            divChatCard.append(divAvatar);
            divChatCard.append(divName);

            $chatsPlaceholder.append(divChatCard);

            //////////////INIT DIALOG
            const $currentChat = $("#chat-" + acc.idAccount);

            ////////BIND DIALOG
            $currentChat.unbind()
            $currentChat.on('click', function () {
                $("#chats-placeholder > div").css('background-color', '#fff')
                $currentChat.css('background-color', '#ddd')

                const $messagesContainer = $(".messages-container");
                $("#messages-placeholder").attr('current-chat', acc.idAccount);

                $messagesContainer.html('');
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
                            message.messageStatusFacade = {
                                idMessageStatus: 2,
                                name: 'viewed'
                            }
                        }
                    }
                )

                $.ajax({
                    method: "patch",
                    url: "/accounts/" + accountFacadeJson.idAccount + "/messages/chat/" + acc.idAccount,
                    contentType: "application/json",
                    success: () => {
                        $("#chat-" + acc.idAccount + " > div > b").text('');
                        recountNotViewedMessages(accountFacadeJson.idAccount)
                    },
                    error: () => {
                        callMessagePopup("Ошибка", "Ошибка прочтения сообщений")
                    }
                })

                const $sendBtn = $(".send-btn");
                const $messageTextarea = $(".message-textarea");
                $sendBtn.unbind();
                $sendBtn.on('click', () => {
                    const text = secureCleanValue($messageTextarea.val());

                    if (text.trim() !== '' &&
                        text.trim().length <= 500) {
                        sendMessage(acc.idAccount, text);
                    } else {
                        callMessagePopup('Ошибка', 'Нельзя отправить пустое сообщение')
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
            if (message.senderFacade.idAccount !== idAccount
                && message.messageStatusFacade.name === 'not_viewed') {
                messageCount++;
            }
        })

        const messagesText = messageCount !== 0 ? 'Сообщения +' + messageCount : 'Сообщения';
        $("#messages-btn > div > a").text(messagesText);
    }
}
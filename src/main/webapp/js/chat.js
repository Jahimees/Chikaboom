{
    let messageCount = 0;

    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/subscription'
    });

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/accounts/' + accountFacadeJson.idAccount + '/queue/messages', (greeting) => {
            messageCount++;
            $("#messages-btn > div > a").text('Сообщения +' + messageCount);
            showGreeting(JSON.parse(greeting.body));
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
    }

    function showGreeting(message) {
        console.log(message)
    }

    $(function () {
        $("form").on('submit', (e) => e.preventDefault());
        $("#connect").click(() => connect());
        $("#disconnect").click(() => disconnect());
        $("#send").click(() => sendMessage());
    });
}
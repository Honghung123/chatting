import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
const stompClientUrl = process.env.NEXT_PUBLIC_URL_STOMP_CLIENT;

// Socket client
const stompClient = new Client({
    webSocketFactory: () => new SockJS(stompClientUrl + "/ws"),
    onConnect: function () {
        console.log("Successfully connected to STOMP client.");
    },
    onDisconnect: function () {
        console.log("Disconnected from STOMP client.");
    },
    onWebSocketError: function (event: any) {
        stompClient.deactivate();
    },
});

export { stompClient };

// todoSlice.js
import { createSlice, Slice } from "@reduxjs/toolkit";
import { Client } from "@stomp/stompjs";

const stompClientSlice: Slice<any> = createSlice({
    name: "stompClient",
    initialState: {
        value: null,
    },
    reducers: {
        updateStompClient(state, action) {
            const client = action.payload as Client;
            state.value = client;
            console.log(client);
            console.log(action.payload.connected);
        },
    },
});
export const { updateStompClient } = stompClientSlice.actions;
export default stompClientSlice.reducer;

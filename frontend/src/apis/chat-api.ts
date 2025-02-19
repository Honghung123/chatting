import { ErrorResponseType, SuccessResponseType } from "@/schema/types/common";
import { UserListType, UserType } from "@/schema/user.schema";
import { myAxios } from "@/apis/constants";
import { ConversationType, MessageChatType } from "@/schema/chat.schema";

export const callGetNewConversationId = async (
    userId: string,
    otherUserId: string
): Promise<string | ErrorResponseType> => {
    try {
        const payload = {
            userId: userId,
            otherUserId: otherUserId,
            conversationType: "DIRECT",
        };
        const response = await myAxios.post(`/chat/new-conversation`, payload, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        if (response.status !== 200) return response.data as ErrorResponseType;
        const result = response.data as SuccessResponseType<string, null>;
        return result.data;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

export const callGetConversationList = async (): Promise<ConversationType[] | ErrorResponseType> => {
    try {
        console.log("Trigger fetch conversation list.");
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken"))
            return {
                statusCode: 401,
                message: "Unauthorized",
                timestamp: new Date().toISOString(),
                path: "/user/info",
                errorCode: "not-authorized",
            };
        const response = await myAxios.get(`/account/conversations`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        // console.log(response);
        const result = response.data as SuccessResponseType<ConversationType[], null>;
        return result.data;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

export const callGetFriendList = async (): Promise<ConversationType[] | ErrorResponseType> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken"))
            return {
                statusCode: 401,
                message: "Unauthorized",
                timestamp: new Date().toISOString(),
                path: "/user/info",
                errorCode: "not-authorized",
            };
        const response = await myAxios.get(`/account/friends-not-in-conversation`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        // console.log(response);
        const result = response.data as SuccessResponseType<ConversationType[], null>;
        return result.data;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

export const getChatHistoryList = async (
    conversationId: string | null
): Promise<MessageChatType[] | ErrorResponseType> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken"))
            return {
                statusCode: 401,
                message: "Unauthorized",
                timestamp: new Date().toISOString(),
                path: "/user/info",
                errorCode: "not-authorized",
            };
        if (!conversationId) return [];
        const response = await myAxios.get(`/chat/messages/${conversationId}`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        // console.log(response);
        const result = response.data as SuccessResponseType<MessageChatType[], null>;
        return result.data;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

"use client";
import { useQuery } from "@tanstack/react-query";
import { commonOptions } from "@/lib/react-query/options";
import { UserType } from "@/schema/auth.schema";
import { UserInfoRequestDTO } from "@/schema/user.schema";
import { callGettingUserInfoRequest } from "@/apis/user-api";
import { callGetConversationList, callGetFriendList } from "@/apis/chat-api";

export function useCachedConversationListQuery() {
    return useQuery({
        queryKey: ["user-conversation-list"],
        queryFn: () => {
            return callGetConversationList();
        },
        throwOnError: true,
        refetchInterval: 60000,
        ...commonOptions,
    });
}

export function useCachedFriendListQuery() {
    return useQuery({
        queryKey: ["user-friend-list"],
        queryFn: () => {
            return callGetFriendList();
        },
        throwOnError: true,
        refetchInterval: 60000,
        ...commonOptions,
    });
}

export function useCachedUserInfo() {
    return useQuery({
        queryKey: ["user-profile"],
        queryFn: () => {
            return callGettingUserInfoRequest();
        },
        throwOnError: true,
        ...commonOptions,
    });
}

// export function useCachedUserList(currentPage: number, perPage: number = 5, keyword: string = "", role: string = "") {
//     return useQuery({
//         queryKey: ["user-list", currentPage, perPage, keyword, role],
//         queryFn: () => {
//             return getUserList(currentPage, perPage, keyword, role);
//         },
//         throwOnError: true,
//         ...commonOptions,
//     });
// }

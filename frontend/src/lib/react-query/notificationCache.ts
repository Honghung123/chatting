import { getChatHistoryList } from "@/apis/chat-api";
import { commonOptions } from "@/lib/react-query/options";
import { useQuery } from "@tanstack/react-query";

export function useCachedChatHistories(conversationId: string | null) {
    return useQuery({
        queryKey: [`chat-history-${conversationId}`, conversationId],
        queryFn: () => {
            return getChatHistoryList(conversationId);
        },
        throwOnError: true,
        ...commonOptions,
    });
}

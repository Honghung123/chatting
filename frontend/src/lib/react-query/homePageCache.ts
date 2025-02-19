import { getChatHistoryList } from "@/apis/chat-api";
import { callGetAllStoryRequest } from "@/apis/story-api";
import { commonOptions } from "@/lib/react-query/options";
import { useQuery } from "@tanstack/react-query";

export function useCachedGetAllStory(page: number, size: number) {
    return useQuery({
        queryKey: [`stories`, page, size],
        queryFn: () => {
            return callGetAllStoryRequest(page, size);
        },
        throwOnError: true,
        ...commonOptions,
    });
}

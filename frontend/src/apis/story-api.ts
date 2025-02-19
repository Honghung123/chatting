import { myAxios } from "@/apis/constants";
import { AttachmentRequestType } from "@/schema/media.schema";
import { StoryType } from "@/schema/story.schema";
import { PaginationType, PaginationV2Type, SuccessResponseType } from "@/schema/types/common";

export const callGetAllStoryRequest = async (page: number, size: number): Promise<PaginationV2Type<StoryType>> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken")) {
            const response = await myAxios.get(`/story/get-all?page=${page}&size=${size}`);
            const data = response.data as SuccessResponseType<PaginationV2Type<StoryType>, null>;
            return data.data;
        }
        const response = await myAxios.get(`/story/get-all?page=${page}&size=${size}`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        const data = response.data as SuccessResponseType<PaginationV2Type<StoryType>, null>;
        return data.data;
    } catch (error: any) {
        return {
            page: 0,
            pageSize: 0,
            totalElements: 0,
            totalPages: 0,
            sorted: false,
            data: [],
        };
    }
};

export const callAddANewStoryRequest = async (
    caption: string,
    media: AttachmentRequestType[]
): Promise<StoryType | null> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken")) return null;
        const payload = {
            caption: caption,
            media: media,
        };
        const response = await myAxios.post(`/story/new`, payload, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        const data = response.data as SuccessResponseType<StoryType, null>;
        return data.data;
    } catch (error: any) {
        return null;
    }
};

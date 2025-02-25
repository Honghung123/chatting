import { myAxios } from "@/apis/constants";
import { AttachmentRequestType } from "@/schema/media.schema";
import { PostType } from "@/schema/post.schema";
import { ErrorResponseType, PaginationType, PaginationV2Type, SuccessResponseType } from "@/schema/types/common";

export const callGetAllPostRequest = async (page: number, size: number): Promise<PaginationV2Type<PostType>> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken")) {
            const response = await myAxios.get(`/post/get-all?page=${page}&size=${size}`);
            const data = response.data as SuccessResponseType<PaginationV2Type<PostType>, null>;
            return data.data;
        }
        const response = await myAxios.get(`/post/get-all?page=${page}&size=${size}`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        const data = response.data as SuccessResponseType<PaginationV2Type<PostType>, null>;
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

export const callAddANewPostRequest = async (
    caption: string,
    media: AttachmentRequestType[]
): Promise<PostType | null> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken")) return null;
        const payload = {
            caption: caption,
            media: media,
        };
        const response = await myAxios.post(`/post/new`, payload, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        const data = response.data as SuccessResponseType<PostType, null>;
        return data.data;
    } catch (error: any) {
        return null;
    }
};

export const callDeletePostRequest = async (postId: string): Promise<{ deleted: boolean } | ErrorResponseType> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken"))
            return {
                statusCode: 401,
                message: "Unauthorized",
                timestamp: new Date().toISOString(),
                path: "/",
                errorCode: "invalid_token",
            };
        await myAxios.delete(`/post/${postId}`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        return {
            deleted: true,
        };
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

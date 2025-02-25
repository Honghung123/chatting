import axios, { AxiosResponse } from "axios";
import { BASE_API } from "./constants";
import { SuccessResponse } from "@/schema/http.schema";
import { NotificationType } from "@/schema/notification.shema";
import { PaginationType } from "@/schema/types/common";
/* Khiem workspace - Call notification apis */

export async function getNotificationList(
    currentPage: number = 1,
    perPage: number = 3
): Promise<PaginationType<NotificationType>> {
    const defaultResult = {
        page: 0,
        perPage: 0,
        total: 0,
        totalPages: 0,
        data: [],
    } as PaginationType<NotificationType>;
    if (!localStorage.getItem("accessToken")) return defaultResult;
    try {
        const result = (
            await axios.get(`${BASE_API}/notification/all?page=${currentPage}&size=${perPage}`, {
                headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
            })
        ).data as SuccessResponse<PaginationType<NotificationType>>;
        return result.data;
    } catch (error: any) {
        return defaultResult;
    }
}

export async function callMarkAsReadNotificationRequest(id: string): Promise<any> {
    if (!localStorage.getItem("accessToken")) return null;
    try {
        const result = (
            await axios.put(
                `${BASE_API}/notification/mark-as-read/${id}`,
                {},
                {
                    headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
                }
            )
        ).data as SuccessResponse<any>;
        return result;
    } catch (error: any) {
        return null;
    }
}

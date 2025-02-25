import { UserQuickInfoType } from "@/schema/user.schema";

/* Khiem workspace - Define notification types */
export type NotificationListType = {
    data: Notification[];
    total: number;
    totalPage: number;
    page: number;
    perPage: number;
};
export type NotificationType = {
    id: string;
    user: UserQuickInfoType;
    type: string;
    title: string;
    content: string;
    createdAt: string;
    isRead: boolean;
    targetId: string;
};
export type BranchRegisteredData = {
    brandId: string;
    brandName: string;
};
export type BrandEventCreatedData = {
    eventId: string;
    eventName: string;
    brandId: string;
    brandName: string;
};
export type EventApprovedData = {
    brandId: string;
    eventId: string;
    eventName: string;
};

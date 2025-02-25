"use client";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import Link from "next/link";
import { NotificationType } from "@/schema/notification.shema";
import { useEffect, useState } from "react";
import { callMarkAsReadNotificationRequest, getNotificationList } from "@/apis/notification-api";
import { BellRing } from "lucide-react";
import { printDate } from "@/lib/utils";
import { UserType } from "@/schema/user.schema";
import { stompClient } from "@/app/(chat)/chat/socket";
import { useRouter } from "next/navigation";

export default function MyNotification({ children, user }: { children: React.ReactNode; user: UserType }) {
    // Define state, actions here
    const [notifications, setNotifications] = useState<NotificationType[]>([]);
    const router = useRouter();
    useEffect(() => {
        const getNotifications = async () => {
            const result = await getNotificationList();
            console.log(result.data);
            setNotifications(result.data);
        };
        getNotifications();
    }, []);
    useEffect(() => {
        stompClient.activate();
        stompClient.onConnect = () => {
            stompClient.subscribe(`/queue/notifications/${user.id}`, function (message: any) {
                const notification = JSON.parse(message.body) as NotificationType;
                console.log(notification);
                setNotifications((prev) => [...prev, notification]);
            });
            stompClient.subscribe(`/queue/notifications`, function (message: any) {
                const notification = JSON.parse(message.body) as NotificationType;
                console.log(notification);
                setNotifications((prev) => [...prev, notification]);
            });
        };
    }, []);

    const determineUrl = (type: string, targetId: string) => {
        switch (type) {
            case "follow":
                return `/profile/${targetId}`;
            case "like":
                return `/post/${targetId}`;
            case "comment":
                return `/post/${targetId}`;
            case "upload_post":
                return `#post-${targetId}`;
            default:
                return `#`;
        }
    };

    const handleMarkAsReadAndRedirect = async (notification: NotificationType) => {
        if (!notification.isRead) {
            const result = await callMarkAsReadNotificationRequest(notification.id);
            if (result && result.statusCode == 200) {
                const currentNotifications = [...notifications];
                const notificationIndex = currentNotifications.findIndex((n) => n.id === notification.id);
                currentNotifications[notificationIndex].isRead = true;
                setNotifications(currentNotifications);
            }
        }
        const determinedUrl = determineUrl(notification.type, notification.targetId);
        console.log(determinedUrl);
        router.push(determinedUrl);
    };

    const wasAllNotificationsRead = notifications.every((notification) => notification.isRead == true);

    return (
        <div className="p-0">
            <Popover>
                <PopoverTrigger className="flex items-center">
                    <div className="relative">
                        {children}
                        {!wasAllNotificationsRead && (
                            <div className="absolute top-0 right-0">
                                <span className="relative flex h-2 w-2">
                                    <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-rose-500 opacity-75"></span>
                                    <span className="relative inline-flex rounded-full h-2 w-2 bg-rose-600"></span>
                                </span>
                            </div>
                        )}
                    </div>
                </PopoverTrigger>
                {/* Render the two newest notifications below */}
                <PopoverContent className="min-w-[20rem] p-0 border border-gray-200">
                    <p className="text-center font-semibold pt-1 border-b border-b-gray-200">Notification</p>
                    <ul className="py-1">
                        {notifications.length == 0 && <p className="text-center">You dont have any notification yet</p>}
                        {notifications.length > 0 &&
                            notifications.map((notification, index) => (
                                <div
                                    key={index}
                                    className={`flex gap-2 hover:bg-gray-200 dark:hover:bg-slate-700 py-2 px-3 cursor-pointer ${
                                        !notification.isRead ? "bg-gray-200 dark:bg-slate-700" : ""
                                    }`}
                                    onClick={() => handleMarkAsReadAndRedirect(notification)}
                                >
                                    <div className="flex items-center justify-center w-8 md:w-12 dark:bg-slate-800 rounded-sm">
                                        <BellRing className="w-12 h-8 md:h-10 rounded-md object-cover m-auto" />
                                    </div>
                                    <div className="content flex-1">
                                        <div className="flex justify-between items-center">
                                            <h1 className="line-clamp-1 text-sm">{notification.title}</h1>
                                            <span className="text-[.75rem]">
                                                {printDate(new Date(notification.createdAt))}
                                            </span>
                                        </div>
                                        <p className="line-clamp-2 text-xs text-justify">{notification.content}</p>
                                    </div>
                                </div>
                            ))}
                    </ul>
                    <div className="flex justify-center p-1 border-t border-t-gray-200">
                        <Link
                            href="/counterpart/notification"
                            className="text-sm text-sky-500 hover:text-sky-400 hover:underline"
                        >
                            View All
                        </Link>
                    </div>
                </PopoverContent>
            </Popover>
        </div>
    );
}

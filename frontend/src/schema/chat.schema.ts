import { AttachmentType } from "@/schema/media.schema";
import { UserQuickInfoType } from "@/schema/user.schema";

export type MessageChatItemType = {
    content: string;
    recipient: string;
    sender: string;
    type: string;
};

export type MessageChatType = {
    messageId: number;
    conversationId: string;
    sender: UserQuickInfoType;
    // recipient: UserQuickInfoType;
    content: string;
    messageContentType: string;
    attachment: AttachmentType;
    status: string;
    createdAt: string;
};

export type ConversationType = {
    conversationId: string | null;
    sender: UserQuickInfoType | null;
    user: UserQuickInfoType;
    latestMessage: string | null;
    conversationType: string | null;
    messageContentType: string | null;
    online: boolean;
    createdAt: string | null;
};

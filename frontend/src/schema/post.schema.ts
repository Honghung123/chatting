import { AttachmentType } from "@/schema/media.schema";
import { UserQuickInfoType } from "@/schema/user.schema";

export type PostType = {
    id: string;
    caption: string;
    createdAt: string;
    updatedAt: string;
    user: UserQuickInfoType;
    attachment: AttachmentType[];
    totalLikes: number;
    totalComments: number;
    totalShares: number;
};

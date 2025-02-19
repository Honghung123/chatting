"use client";
import CreatePostModal from "@/app/(mainlayout)/create-post-modal";
import { UserType } from "@/schema/user.schema";

const CreatePostBox: React.FC<{ user: UserType | null }> = ({ user }) => {
    if (!user) return <></>;
    return <></>;
};

export default CreatePostBox;

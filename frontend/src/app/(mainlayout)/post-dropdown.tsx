"use client";

import { callDeletePostRequest } from "@/apis/post-api";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { toast } from "@/hooks/use-toast";
import { PostType } from "@/schema/post.schema";
import { UserType } from "@/schema/user.schema";
import { BookMarked, Heart, Star, LogOut, Settings, Trash2, Flag } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function PostDropdown({
    children,
    post,
    updateAfterRemovingPost,
}: {
    children: React.ReactNode;
    post: PostType;
    updateAfterRemovingPost: (postId: string) => void;
}) {
    const [open, setOpen] = useState(false);
    const handleDeletePost = async () => {
        const result = await callDeletePostRequest(post.id);
        if (result && !result.hasOwnProperty("errorCode")) {
            toast({
                title: "Success",
                description: "Post was deleted successfully",
                className: "bg-lime-500 text-white",
            });
            setOpen(false);
            updateAfterRemovingPost(post.id);
        } else {
            toast({
                title: "Error",
                description: "Post was deleted successfully",
                className: "bg-red-500 text-white",
            });
        }
        setOpen(false);
    };

    const handleEditPost = async () => {
        toast({
            title: "Info",
            description: "This feature is not available yet",
            className: "bg-sky-500 text-white",
        });
        setOpen(false);
    };
    const handleReportPost = async () => {
        toast({
            title: "Success",
            description: "Post was reported successfully",
            className: "bg-lime-500 text-white",
        });
        setOpen(false);
    };

    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger className="flex items-center">{children}</PopoverTrigger>
            <PopoverContent className="w-[9rem] border border-gray-200 !p-0">
                <div
                    className="cursor-pointer bg-main-hover hover:bg-slate-200  flex gap-3 items-center p-2"
                    onClick={handleReportPost}
                >
                    <Flag size={20} />
                    <span className="block w-full h-full">Report</span>
                </div>
                <div
                    className="cursor-pointer bg-main-hover hover:bg-slate-200  flex gap-3 items-center p-2"
                    onClick={handleEditPost}
                >
                    <Flag size={20} />
                    <span className="block w-full h-full">Edit</span>
                </div>
                <div
                    className="cursor-pointer bg-main-hover hover:bg-slate-200 flex gap-3 items-center p-2"
                    onClick={handleDeletePost}
                >
                    <Trash2 size={20} />
                    <span className="block w-full h-full">Delete</span>
                </div>
            </PopoverContent>
        </Popover>
    );
}

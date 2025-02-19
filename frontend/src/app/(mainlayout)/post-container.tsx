"use client";
import { callGetAllPostRequest } from "@/apis/post-api";
import CreatePostModal from "@/app/(mainlayout)/create-post-modal";
import { printDateTime } from "@/lib/utils";
import { PostType } from "@/schema/post.schema";
import { PaginationV2Type } from "@/schema/types/common";
import { UserType } from "@/schema/user.schema";
import { Heart, MessageSquareText, Share, Share2, ThumbsUp } from "lucide-react";
import { useEffect, useState } from "react";
const Post: React.FC<{ user: UserType | null }> = ({ user }) => {
    const [postList, setPostList] = useState<PaginationV2Type<PostType>>({
        page: 0,
        pageSize: 0,
        totalElements: 0,
        totalPages: 0,
        sorted: false,
        data: [],
    });
    const fetchPosts = async () => {
        const result = await callGetAllPostRequest(1, 5);
        setPostList(result);
    };
    // const { data: postList, isLoading, isFetching } = useCachedGettingAllPostQuery();
    useEffect(() => {
        fetchPosts();
    }, []);
    return (
        <>
            <CreatePostModal user={user} refetchData={fetchPosts} />
            {postList.data.map((post, index) => {
                return (
                    <div className="h-auto w-full rounded-md bg-white shadow dark:bg-neutral-800" key={index}>
                        <div className="flex items-center space-x-2 p-2.5 px-4">
                            <div className="h-10 w-10">
                                <img
                                    src={
                                        post.user.avatarUrl ||
                                        "https://cdn.imagecomics.com/assets/i/releases/1058882/bug-wars-1-of-6_07d80c1f01.jpg"
                                    }
                                    className="h-full w-full rounded-full"
                                    alt="dp"
                                />
                            </div>
                            <div className="flex flex-grow flex-col">
                                <p className="text-sm font-semibold text-gray-700 dark:text-gray-300">
                                    {post.user.name}
                                </p>
                                <span className="text-xs font-thin text-gray-400">
                                    {printDateTime(new Date(post.createdAt))}
                                </span>
                            </div>
                            <div className="h-8 w-8">
                                <button className="h-full w-full rounded-full text-gray-400 hover:bg-gray-100 focus:outline-none dark:text-gray-500 dark:hover:bg-neutral-700">
                                    <i className="fas fa-ellipsis-h"></i>
                                </button>
                            </div>
                        </div>
                        {post.caption && (
                            <div className="mb-1">
                                <p className="max-h-10 truncate px-3 text-sm text-gray-700 dark:text-gray-300">
                                    {post.caption}
                                </p>
                            </div>
                        )}
                        {post.attachment && post.attachment.length == 1 && (
                            <>
                                <img
                                    key={index}
                                    src={post.attachment[0].fileUrl}
                                    alt="Post image"
                                    className="h-76 max-h-100 w-full object-cover"
                                />
                            </>
                        )}
                        {post.attachment && post.attachment.length > 1 && (
                            <div className="grid grid-cols-2 gap-1">
                                {post.attachment.map((attachment, index) => (
                                    <img
                                        key={index}
                                        src={attachment.fileUrl}
                                        alt="Post image"
                                        className="h-76 max-h-100 w-full object-cover"
                                    />
                                ))}
                            </div>
                        )}

                        <div className="flex w-full flex-col p-2 px-4 space-y-1">
                            <div className="flex items-center justify-between border-b border-gray-300 text-sm dark:border-neutral-700">
                                <div className="flex items-center">
                                    <div className="flex items-center -space-x-1">
                                        <span className="flex h-4 w-4 items-center justify-center rounded-full bg-sky-600 text-white">
                                            <ThumbsUp size={9} fill="white" />
                                        </span>
                                        <span className="flex h-4 w-4 items-center justify-center rounded-full bg-rose-600 text-white">
                                            <Heart size={9} fill="white" />
                                        </span>
                                    </div>
                                    <div className="ml-1">
                                        <p className="text-gray-500 dark:text-gray-300">1K</p>
                                    </div>
                                </div>
                                <div className="flex items-center space-x-3">
                                    {post.totalComments > 0 && (
                                        <span className="text-gray-500 dark:text-gray-300">
                                            {post.totalComments} {post.totalComments == 1 ? "comment" : "comments"}
                                        </span>
                                    )}
                                    {post.totalShares > 0 && (
                                        <span className="text-gray-500 dark:text-gray-300">
                                            {post.totalShares} {post.totalShares == 1 ? "share" : "shares"}
                                        </span>
                                    )}
                                </div>
                            </div>
                            <div className="flex text-sm font-thin text-gray-500">
                                <button className="flex h-8 flex-1 items-center justify-center space-x-2 rounded-md hover:bg-gray-100 focus:bg-gray-200 focus:outline-none dark:text-gray-300 dark:hover:bg-neutral-700 dark:focus:bg-neutral-700">
                                    <ThumbsUp size={16} />
                                    <p className="font-semibold">Like</p>
                                </button>
                                <button className="flex h-8 flex-1 items-center justify-center space-x-2 rounded-md hover:bg-gray-100 focus:bg-gray-200 focus:outline-none dark:text-gray-300 dark:hover:bg-neutral-700 dark:focus:bg-neutral-700">
                                    <MessageSquareText size={16} />
                                    <p className="font-semibold">Comment</p>
                                </button>
                                <button className="flex h-8 flex-1 items-center justify-center space-x-2 rounded-md hover:bg-gray-100 focus:bg-gray-200 focus:outline-none dark:text-gray-300 dark:hover:bg-neutral-700 dark:focus:bg-neutral-700">
                                    <Share2 size={16} />
                                    <p className="font-semibold">Share</p>
                                </button>
                            </div>
                        </div>
                    </div>
                );
            })}
        </>
    );
};

export default Post;

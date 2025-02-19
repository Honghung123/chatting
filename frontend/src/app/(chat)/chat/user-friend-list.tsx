"use client";

import ChatAreaSection from "@/app/(chat)/chat/chat-area-section";
import { GameInfoSkeleton } from "@/app/(subsystem)/admin/skeletons";
import ErrorPage from "@/app/error";
import { useCachedFriendListQuery } from "@/lib/react-query/userCache";
import { ConversationType } from "@/schema/chat.schema";
import { UserType } from "@/schema/user.schema";
import { useState } from "react";

export default function UserFriendsSection({
    setCurrentConversation,
}: {
    setCurrentConversation: (cons: ConversationType | null) => void;
}) {
    const { data: friends, isLoading, isFetching, isError, refetch } = useCachedFriendListQuery(); // Get query status
    if (isError) return <ErrorPage />;
    if (isLoading || isFetching) return <GameInfoSkeleton />; // isLoading is true when api in queryFn was calling and data doesn't exist in cache

    if (!isLoading && !isFetching && (!friends || Object.keys(friends!).includes("errorCode")))
        return <div>There is an error to display friend list</div>;
    const allNewFriendConversations = friends as ConversationType[];
    const handleSetCurrentConversation = (conversation: ConversationType) => setCurrentConversation(conversation);
    return (
        <>
            <h2 className="text-lg font-bold">Your friends</h2>
            <div className="contacts p-2 flex-1 overflow-y-scroll">
                {allNewFriendConversations!.map((conversation, index) => (
                    <div
                        className="flex justify-between items-center p-3 hover:bg-gray-300 rounded-lg relative cursor-pointer group"
                        key={index}
                        onClick={() => handleSetCurrentConversation(conversation)}
                    >
                        <div className="w-16 h-16 relative flex flex-shrink-0">
                            <img
                                className="shadow-md rounded-full w-full h-full object-cover"
                                src={conversation.user.avatarUrl || "https://randomuser.me/api/portraits/men/97.jpg"}
                                alt={conversation.user.name}
                            />
                            {/* <div className="absolute bg-gray-900 p-1 rounded-full bottom-0 right-0">
                                <div className="bg-green-500 rounded-full w-3 h-3"></div>
                            </div> */}
                        </div>
                        <div className="flex-auto min-w-0 ml-4 mr-6 hidden md:block group-hover:block">
                            <p className="font-bold">{conversation.user.name}</p>
                            {/* <div className="flex items-center text-sm font-bold">
                                <div className="min-w-0">
                                    <p className="truncate">Hey, Are you there?</p>
                                </div>
                                <p className="ml-2 whitespace-no-wrap">10min</p>
                            </div> */}
                        </div>
                        <div className="bg-blue-700 w-3 h-3 rounded-full flex flex-shrink-0 md:block group-hover:block"></div>
                    </div>
                ))}
            </div>
        </>
    );
}

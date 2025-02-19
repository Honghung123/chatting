"use client";

import ChatAreaSection from "@/app/(chat)/chat/chat-area-section";
import UserFriendsSection from "@/app/(chat)/chat/user-friend-list";
import UserListOnlineSection from "@/app/(chat)/chat/user-list-online";
import UserStorySection from "@/app/(chat)/chat/user-stories";
import UnauthenticatedPage from "@/app/unauthenticated";
import { useCachedUserInfo } from "@/lib/react-query/userCache";
import { ConversationType } from "@/schema/chat.schema";
import { ErrorResponseType } from "@/schema/types/common";
import { UserType } from "@/schema/user.schema";
import { useState } from "react";

export default function ChatPage() {
    const [currentConversation, setCurrentConversation] = useState<ConversationType | null>(null);
    const { data: user, isLoading, isFetching } = useCachedUserInfo();
    if (isLoading || isFetching) return <div>Loading...</div>;
    if (!isLoading && !isFetching && (!user || Object.keys(user!).includes("errorCode"))) {
        const error = user as ErrorResponseType;
        if (error.statusCode === 401)
            return <UnauthenticatedPage statusCode={error.statusCode} message={error.message} />;
        return <UnauthenticatedPage />;
    }
    const currentUser = user as UserType;
    return (
        <div>
            <div className="h-screen w-full flex antialiased overflow-hidden">
                <div className="flex-1 flex flex-col">
                    {/* <div className="border-b-2 border-gray-800 p-2 flex flex-row z-20">
                        <div className="bg-red-600 w-3 h-3 rounded-full mr-2"></div>
                        <div className="bg-yellow-500 w-3 h-3 rounded-full mr-2"></div>
                        <div className="bg-green-500 w-3 h-3 rounded-full mr-2"></div>
                    </div> */}
                    <main className="flex-grow flex flex-row min-h-0">
                        <section className="flex flex-col flex-none overflow-auto w-24 group lg:max-w-sm md:w-2/5 ">
                            <div className="header p-4 flex flex-row justify-between items-center flex-none">
                                <div className="w-16 h-16 relative flex flex-shrink-0">
                                    <img
                                        className="rounded-full w-full h-full object-cover"
                                        alt="ravisankarchinnam"
                                        src="https://avatars3.githubusercontent.com/u/22351907?s=60"
                                    />
                                </div>
                                <p className="text-md font-bold hidden md:block group-hover:block">Messenger</p>
                                <a href="#" className="rounded-full w-10 h-10 p-2 hidden md:block group-hover:block">
                                    <svg viewBox="0 0 24 24" className="w-full h-full fill-current">
                                        <path d="M6.3 12.3l10-10a1 1 0 0 1 1.4 0l4 4a1 1 0 0 1 0 1.4l-10 10a1 1 0 0 1-.7.3H7a1 1 0 0 1-1-1v-4a1 1 0 0 1 .3-.7zM8 16h2.59l9-9L17 4.41l-9 9V16zm10-2a1 1 0 0 1 2 0v6a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V6c0-1.1.9-2 2-2h6a1 1 0 0 1 0 2H4v14h14v-6z" />
                                    </svg>
                                </a>
                            </div>
                            <div className="search-box p-4 flex-none">
                                <form onSubmit={(e) => e.preventDefault()}>
                                    <div className="relative">
                                        <label>
                                            <input
                                                className="rounded-full py-2 pr-6 pl-10 w-full border border-gray-800 focus:border-gray-700 bg-gray-800 focus:bg-gray-900 focus:outline-none text-gray-200 focus:shadow-md transition duration-300 ease-in"
                                                type="text"
                                                defaultValue=""
                                                placeholder="Search Messenger"
                                            />
                                            <span className="absolute top-0 left-0 mt-2 ml-3 inline-block">
                                                <svg viewBox="0 0 24 24" className="w-6 h-6">
                                                    <path
                                                        fill="#bbb"
                                                        d="M16.32 14.9l5.39 5.4a1 1 0 0 1-1.42 1.4l-5.38-5.38a8 8 0 1 1 1.41-1.41zM10 16a6 6 0 1 0 0-12 6 6 0 0 0 0 12z"
                                                    />
                                                </svg>
                                            </span>
                                        </label>
                                    </div>
                                </form>
                            </div>
                            {/* <UserStorySection /> */}
                            <UserListOnlineSection setCurrentConversation={setCurrentConversation} />
                            <UserFriendsSection setCurrentConversation={setCurrentConversation} />
                        </section>
                        {currentConversation && (
                            <ChatAreaSection conversation={currentConversation} currentUser={currentUser!} />
                        )}
                    </main>
                </div>
            </div>
        </div>
    );
}

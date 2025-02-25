"use client";
import ChatWindowPopup from "@/app/(mainlayout)/chat-window";
import ErrorPage from "@/app/error"; 
import { useCachedConversationListQuery } from "@/lib/react-query/userCache"; 
import { ConversationType, MessageChatType } from "@/schema/chat.schema"; 
import { UserType } from "@/schema/user.schema";
import { Client } from "@stomp/stompjs"; 
import { useState } from "react";
export default function UserContacts({ currentUser, stompClient }: { currentUser: UserType; stompClient: Client }) {
    const [myConversations, setMyConversations] = useState<ConversationType[]>([]);
    const { data: friends, isLoading, isFetching, isError } = useCachedConversationListQuery(); // Get query status
    if (isError) return <ErrorPage />;
    if (isLoading) return <div>Loading your contacts...</div>; // isLoading is true when api in queryFn was calling and data doesn't exist in cache
    if (!isLoading && !isFetching && (!friends || Object.keys(friends!).includes("errorCode")))
        return <div className="p-2">Please login to see your friends</div>;
    const allNewFriendConversations = friends as ConversationType[];

    const handleSetCurrentConversation = (conversation: ConversationType) => {
        if (myConversations.some((chat) => chat.user.id === conversation.user.id)) return;
        if (myConversations.length < 3) {
            setMyConversations([...myConversations, conversation]);
        } else {
            const [oldest, ...others] = myConversations;
            setMyConversations([...others, conversation]);
        }
    };

    const closeChat = (userId: string) => {
        setMyConversations(myConversations.filter((chat) => chat.user.id !== userId));
    };

    return (
        <>
            <div className="fixed bottom-2 right-20 flex flex-col items-end space-y-2 z-[99999]">
                <div className="flex space-x-2">
                    {myConversations.map((myConversation) => (
                        <ChatWindowPopup
                            key={myConversation.user.id}
                            currentUser={currentUser}
                            stompClient={stompClient}
                            myConversation={myConversation}
                            closeConversation={closeChat}
                        />
                    ))}
                </div>
            </div>
            <div className="contacts p-2 py-0 flex-1">
                {allNewFriendConversations!.map((conversation, index) => (
                    <div
                        className="flex justify-between items-center p-3 py-2 hover:bg-gray-300 rounded-lg relative cursor-pointer group"
                        key={index}
                        onClick={() => handleSetCurrentConversation(conversation)}
                    >
                        <div className="w-9 h-9 relative flex flex-shrink-0">
                            <img
                                className="shadow-md rounded-full w-full h-full object-cover"
                                src={conversation.user.avatarUrl || "https://randomuser.me/api/portraits/men/97.jpg"}
                                alt={conversation.user.name}
                            />
                            {conversation.online && (
                                <div className="absolute bg-white p-[.1rem] rounded-full bottom-0 right-0">
                                    <div className="bg-green-500 rounded-full w-2 h-2"></div>
                                </div>
                            )}
                        </div>
                        <div className="flex-auto min-w-0 ml-4 mr-6 hidden md:block group-hover:block">
                            <p className="font-bold">{conversation.user.name}</p>
                        </div>
                    </div>
                ))}
            </div>
        </>
    );
}

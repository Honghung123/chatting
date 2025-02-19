"use client";

import { ChatBubble, ChatBubbleAvatar, ChatBubbleMessage } from "@/components/ui/chat/chat-bubble";
import { ChatInput } from "@/components/ui/chat/chat-input";
import { ChatMessageList } from "@/components/ui/chat/chat-message-list";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useAppDispatch } from "@/lib/redux/hooks";
import { Bot, SendHorizontal, Star, Trash2, X } from "lucide-react";
import Link from "next/link";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useLayoutEffect, useRef, useState } from "react";
import { useCachedUserInfo } from "@/lib/react-query/userCache";
import ClearChatMessages from "@/components/shared/chatbox/clear-chat-msg-dialog";
import { defaultAvatar } from "@/lib/utils";
import { MessageChatItemType } from "@/schema/chat.schema";
import { Client } from "@stomp/stompjs";
import { stompClient as myStompClient } from "@/app/(chat)/chat/socket";
import Loading from "@/app/loading";
import { UserType } from "@/schema/user.schema";

export default function MainChatBox({ user }: { user: UserType | null }) {
    const [isOpen, setIsOpen] = useState(false);
    const [valueInput, setValueInput] = useState("Hung yeu Hoamg");
    const messagesEndRef = useRef<HTMLDivElement | null>(null);
    const router = useRouter();
    const searchParams = useSearchParams();
    const dispatch = useAppDispatch();
    const [messages, setMessages] = useState<MessageChatItemType[]>([]);
    // Websocket
    const [stompClient, setStompClient] = useState<Client | undefined>(undefined);

    const handleAddNewMessage = (oldMessages: MessageChatItemType[], message: MessageChatItemType) => {
        setMessages([...oldMessages, message]);
    };
    // Establishing connection
    useEffect(() => {
        if (!user) return;
        myStompClient.activate();
        const currentUser = user as UserType;
        console.log(currentUser.id);
        myStompClient!.onConnect = () => {
            myStompClient!.subscribe(`/queue/${currentUser.id}/topic/self`, function (message: any) {
                const newMessage = JSON.parse(message.body);
                console.log("New message: " + newMessage.content);
                handleAddNewMessage(messages, newMessage);
            });
        };
        setStompClient(myStompClient);
        return () => {
            myStompClient.deactivate();
        };
    }, [messages, user]);

    // Sending a message
    const sendSelfChatMessage = (message: string, senderId: string, recipientId: string) => {
        if (!stompClient) return;
        const payload: MessageChatItemType = {
            content: message,
            sender: senderId,
            recipient: recipientId,
            type: "CHAT",
        };
        stompClient!.publish({
            destination: "/app/self",
            body: JSON.stringify(payload),
        });
    };

    // const { data: currentUser } = useCachedUserQuery();
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useLayoutEffect(() => {
        scrollToBottom();
    }, [messages]);

    const toggleChatbox = () => {
        setIsOpen(!isOpen);
    };

    const handleSubmitRequest = async () => {
        if (valueInput.trim() === "" || !stompClient) return;
        const currentUser = user as UserType;
        // console.log(`/user/${currentUser.id}/topic/self`);
        // stompClient!.onConnect = () => {
        //     stompClient!.subscribe(`/user/${currentUser.id}/topic/self`, function (message: any) {
        //         const newMessage = JSON.parse(message.body);
        //         console.log("New message: " + newMessage.content);
        //         handleAddNewMessage(messages, newMessage);
        //     });
        // };
        sendSelfChatMessage(valueInput, currentUser.id, currentUser.id);
        // setValueInput("");
    };

    const clearChatMessages = () => {
        setMessages([]);
        setValueInput("");
    };

    return (
        <div className="relative">
            <div
                className="fixed bg-white border border-gray-600 rounded-full p-2 cursor-pointer select-none bottom-4 right-4"
                onClick={toggleChatbox}
            >
                <div className="absolute animate-ping top-2 right-2 bg-gray-700 rounded-full z-0 opacity-45">
                    <Bot size={30} fill="aqua" />
                </div>
                <Bot size={30} fill="aqua" z={1} />
            </div>
            {isOpen && (
                <div className="fixed bottom-8 right-16 lg:right-20 w-[300px] h-[450px] sm:w-[400px] bg-white border border-gray-600 rounded-lg z-[1000]">
                    <div className="p-2 flex items-center justify-center border-b border-gray-700 relative">
                        <strong>Chat Support</strong>
                        <X size={20} className="absolute right-2 cursor-pointer" onClick={() => setIsOpen(false)} />
                    </div>
                    {messages.length > 1 && (
                        <ClearChatMessages handleClearChatMessges={clearChatMessages}>
                            <div
                                className="border border-gray-700 rounded-full p-2 cursor-pointer w-9 h-9 absolute bottom-16 right-4 z-[9999]"
                                title="Clear chat"
                            >
                                <Trash2 />
                            </div>
                        </ClearChatMessages>
                    )}
                    <ScrollArea className="h-[350px]">
                        <ChatMessageList smooth className="h-full !pb-0">
                            {messages.map((message, index) => {
                                const variant = message.sender === "client" ? "sent" : "received";
                                return (
                                    <ChatBubble key={index} variant={variant}>
                                        <ChatBubbleAvatar
                                            fallback={variant === "sent" ? "US" : "AI"}
                                            className="border border-gray-600 w-8 h-8"
                                        />
                                        <ChatBubbleMessage className="text-sm w-full !p-2 navigate-link">
                                            {!message ? (
                                                <span className="text-red-500">An error occurred</span>
                                            ) : (
                                                <>
                                                    {message.content}
                                                    {/* {message.link && (
                                                        <Link
                                                            href={message.link}
                                                            className="text-blue-500 hover:underline pl-1"
                                                        >
                                                            {message.title}
                                                        </Link>
                                                    )} */}
                                                </>
                                            )}
                                        </ChatBubbleMessage>
                                    </ChatBubble>
                                );
                            })}
                            <div ref={messagesEndRef}></div>
                        </ChatMessageList>
                    </ScrollArea>
                    <div className="border-t py-1 relative ">
                        <div className="w-[98%] mx-auto rounded-lg h-[50px] overflow-hidden bg-slate-200">
                            <ChatInput
                                placeholder="Type your message here..."
                                className="h-full rounded-sm bg-slate-200 border-0 pr-10 shadow-none focus-visible:ring-0 scroller"
                                value={valueInput}
                                onChange={(e: any) => setValueInput(e.target.value)}
                                // disabled={messages[messages.length - 1].isLoading}
                            />
                            <SendHorizontal
                                className="size-6 absolute top-[27%] right-5 cursor-pointer"
                                fill="aqua"
                                stroke="blue"
                                onClick={handleSubmitRequest}
                            />
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

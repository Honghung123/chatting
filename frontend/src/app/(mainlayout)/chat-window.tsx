"use client";

import { Fragment, memo, useEffect, useLayoutEffect, useRef, useState } from "react";
import {
    X,
    MessageSquare,
    CirclePlus,
    Paperclip,
    SendHorizontal,
    FileCheck,
    SmilePlus,
    EllipsisVertical,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { UserType } from "@/schema/user.schema";
import { ConversationType, MessageChatType } from "@/schema/chat.schema";
import { ScrollArea } from "@/components/ui/scroll-area";
import { ChatMessageList } from "@/components/ui/chat/chat-message-list";
import { ChatBubble, ChatBubbleAvatar, ChatBubbleMessage } from "@/components/ui/chat/chat-bubble";
import Link from "next/link";
import { Client } from "@stomp/stompjs";
import { AttachmentRequestType, AttachmentType, FileUploadResponseType } from "@/schema/media.schema";
import { toast } from "@/hooks/use-toast";
import { callGetNewConversationId, getChatHistoryList } from "@/apis/chat-api";
import FileUploadModal from "@/components/shared/chatbox/file-upload-modal";
import { defaultAvatar, printDateTime } from "@/lib/utils";
import { AutosizeTextarea } from "@/components/ui/chat/autosize-textarea";
import {
    Menubar,
    MenubarContent,
    MenubarItem,
    MenubarMenu,
    MenubarSeparator,
    MenubarTrigger,
} from "@/components/ui/menubar";

function ChatWindowPopup({
    currentUser,
    myConversation,
    closeConversation,
    stompClient,
}: {
    currentUser: UserType;
    myConversation: ConversationType;
    closeConversation: (userId: string) => void;
    stompClient: Client;
}) {
    const [chatInput, setChatInput] = useState<string>("Hello!");
    const [listChatMessages, setListChatMessages] = useState<MessageChatType[]>([]);
    const chatMessagesRef = useRef(listChatMessages);

    // Subscribe topic to listen incomming message from the server when user open a conversation
    const handleSubscribeMessage = (conId: string) => {
        const receiveMessageTopic = stompClient!.subscribe(`/topic/direct/${conId}`, function (message: any) {
            const newMessage = JSON.parse(message.body) as MessageChatType;
            setListChatMessages((prev) => [...prev, newMessage]);
        });

        const deleteMessageTopic = stompClient!.subscribe(`/topic/deleted-message/${conId}`, function (message: any) {
            const deletedMessageId = JSON.parse(message.body) as number;
            console.log("Deleted message has id: " + deletedMessageId);
            if (deletedMessageId == -1) return;
            console.log(chatMessagesRef.current);
            const currentListChatMessages = chatMessagesRef.current;
            const deletedMessageIndex = currentListChatMessages.findIndex((m) => m.messageId === deletedMessageId);
            const messages = [...currentListChatMessages];
            messages.splice(deletedMessageIndex, 1);
            setListChatMessages(messages);
        });

        return [receiveMessageTopic, deleteMessageTopic];
    };

    // Fetch messages and subscribe topic when user open a conversation
    useLayoutEffect(() => {
        // console.log("-----------------> Fetching messages... <-----------------------");
        if (!myConversation.conversationId) return;
        const fetchMessages = async () => {
            const messages = await getChatHistoryList(myConversation.conversationId);
            if (messages.hasOwnProperty("errorCode")) return;
            setListChatMessages(messages as MessageChatType[]);
        };
        fetchMessages();
        const subscribedTopics = handleSubscribeMessage(myConversation.conversationId);
        scrollToBottom();
        return () => {
            // console.log("-----------------> Unsubscribing... <-----------------------");
            if (!myConversation) return;
            for (const topic of subscribedTopics) {
                topic.unsubscribe();
            }
        };
    }, [myConversation]);

    // Scroll to top when new message is added
    const messagesEndRef = useRef<HTMLDivElement | null>(null);
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
        return true;
    };
    useEffect(() => {
        chatMessagesRef.current = listChatMessages;
        scrollToBottom();
    }, [listChatMessages]);

    // Get conversation id from the selected conversation. Create new one if they havent started a conversation and store it to the current session
    const handleGetSessionConversationId = (userId: string) => {
        const storedConversationId =
            sessionStorage.getItem(`conversationId-${userId}-${currentUser.id}`) ||
            sessionStorage.getItem(`conversationId-${currentUser.id}-${userId}`) ||
            null;
        return storedConversationId;
    };
    const handleSetSessionConversationId = (userId: string, conversationId: string) => {
        sessionStorage.setItem(`conversationId-${currentUser.id}-${userId}`, conversationId as string);
    };
    const establishConnection = async (userId: string): Promise<string | null> => {
        const conversationId = (await callGetNewConversationId(currentUser.id, userId)) as string;
        return conversationId || null;
    };
    const getConversationIdOrCreateNewIfNotExist = async (myConversation: ConversationType) => {
        const userId = myConversation.user.id;
        let conversationId: string | null = myConversation.conversationId || handleGetSessionConversationId(userId);
        if (!conversationId) {
            console.log("Establishing connection...");
            conversationId = await establishConnection(userId);
            if (!conversationId) return;
            handleSetSessionConversationId(userId, conversationId);
            if (!stompClient.connected) {
                stompClient.activate();
                stompClient!.onConnect = () => {
                    handleSubscribeMessage(conversationId!);
                };
            } else {
                handleSubscribeMessage(conversationId!);
            }
        }
        return conversationId;
    };

    // Send message to the server
    const handleSendMessage = async () => {
        if (chatInput.trim() === "") return;
        const conversationId = await getConversationIdOrCreateNewIfNotExist(myConversation);
        if (!conversationId) return;
        stompClient!.publish({
            destination: `/app/direct`,
            body: JSON.stringify({
                conversationId: conversationId,
                content: chatInput,
                sender: currentUser.id,
                // recipient: conversation.user.id,
                messageContentType: "TEXT",
            }),
        });
        setChatInput("");
    };

    // Send media files to the server
    const determineFileType = (fileFormat: string) => {
        if (["png", "jpg", "jpeg"].includes(fileFormat)) return "IMAGE";
        if (["mp4", "mkv"].includes(fileFormat)) return "VIDEO";
        if (["mp3", "wav"].includes(fileFormat)) return "AUDIO";
        return "FILE";
    };
    const handleSendingMediaFiles = async (files: FileUploadResponseType[]) => {
        const conversationId = await getConversationIdOrCreateNewIfNotExist(myConversation);
        if (!conversationId) return;
        files.forEach((file) => {
            const attachment: AttachmentRequestType = {
                fileId: file.publicId,
                fileName: file.displayName,
                fileUrl: file.url,
                format: file.format,
            };
            stompClient!.publish({
                destination: `/app/direct`,
                body: JSON.stringify({
                    conversationId: conversationId,
                    content: "",
                    sender: currentUser.id,
                    // recipient: conversation.user.id,
                    messageContentType: determineFileType(file.format),
                    attachment: attachment,
                }),
            });
        });
    };

    // Delete a message of the conversation
    const handleDeleteMessage = (message: MessageChatType) => {
        if (currentUser.id !== message.sender.id) return;
        console.log(message);
        stompClient!.publish({
            destination: `/app/message/delete`,
            body: JSON.stringify({
                conversationId: message.conversationId,
                messageId: message.messageId,
                senderId: message.sender.id,
                fileId: message?.attachment?.fileId || null,
            }),
        });
    };

    // Download a specified file
    const handleDownloadAttachmentFileByFetching = async (attachment: AttachmentType) => {
        toast({
            title: "Downloading...",
            description: "The file is downloading, please wait ...",
            duration: 1000000,
            className: "bg-sky-500 text-white",
        });
        try {
            const response = await fetch(attachment.fileUrl, {
                method: "GET",
                mode: "cors",
            });
            if (!response.ok) throw new Error("Download failed");
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", attachment.fileName);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            toast({
                title: "Error",
                description: "Download failed. Please try again",
                duration: 3000,
                className: "bg-error-500 text-white",
            });
        }
        toast({
            title: "Success",
            description: "Download successfully",
            duration: 2000,
            className: "bg-lime-500 text-white",
        });
    };

    // Print time of messages of the conversation
    const printTimeIfNotToday = (index: number) => {
        if (index == 0) return true;
        const prevMessage = listChatMessages[index - 1];
        const prevMessageDate = new Date(prevMessage.createdAt).toDateString();
        const currentMessageDate = new Date(listChatMessages[index].createdAt).toDateString();
        return prevMessageDate !== currentMessageDate;
    };

    return (
        <Card className="w-[22rem] shadow-lg !rounded-md overflow-hidden">
            <div className="flex !justify-between !flex-row !items-center bg-white p-2">
                <div className="flex gap-2 items-center">
                    <div className="w-8 h-8 relative flex flex-shrink-0">
                        <img
                            className="rounded-full w-full h-full object-cover"
                            src={myConversation.user.avatarUrl || "https://randomuser.me/api/portraits/men/97.jpg"}
                            alt={myConversation.user.name}
                        />
                        {myConversation.online && (
                            <div className="absolute bg-white p-[.1rem] rounded-full bottom-0 right-0">
                                <div className="bg-green-500 rounded-full w-2 h-2"></div>
                            </div>
                        )}
                    </div>
                    <CardTitle>{myConversation.user.name}</CardTitle>
                </div>
                <div
                    className="p-2 hover:bg-slate-300 rounded-full cursor-pointer"
                    onClick={() => closeConversation(myConversation.user.id!)}
                >
                    <X size={16} />
                </div>
            </div>
            <CardContent className="h-80 overflow-auto bg-gray-50 !p-0">
                <ScrollArea className="h-full">
                    <ChatMessageList smooth className="h-full !p-2">
                        {listChatMessages.length == 0 && !myConversation.conversationId && (
                            <p className="text-center text-gray-400">Start a conversation</p>
                        )}
                        {listChatMessages.map((message, index) => {
                            const variant = message.sender.id === currentUser.id ? "sent" : "received";
                            return (
                                <Fragment key={index}>
                                    {printTimeIfNotToday(index) && (
                                        <p className="p-1 text-center text-xs text-gray-500">
                                            {printDateTime(new Date(message.createdAt))}
                                        </p>
                                    )}
                                    <ChatBubble key={index} variant={variant}>
                                        <ChatBubbleAvatar
                                            fallback={variant === "sent" ? "Y" : "F"}
                                            src={
                                                (variant === "sent"
                                                    ? currentUser.avatarUrl
                                                    : myConversation.user.avatarUrl) || defaultAvatar
                                            }
                                            className="w-5 h-5"
                                        />
                                        <ChatBubbleMessage
                                            isLoading={false}
                                            className="text-sm w-full !px-2 !py-1 navigate-link bg-black text-white cursor-pointer"
                                            title={printDateTime(new Date(message.createdAt))}
                                        >
                                            {!message ? (
                                                <span className="text-red-500">An error occurred</span>
                                            ) : (
                                                <>
                                                    {message.messageContentType === "TEXT" && (
                                                        <p className={`bg-black text-white`}>{message.content}</p>
                                                    )}
                                                    {message.messageContentType === "IMAGE" && (
                                                        <img
                                                            src={message.attachment.fileUrl}
                                                            alt="Image"
                                                            fetchPriority="high"
                                                            className="w-full"
                                                        />
                                                    )}
                                                    {message.messageContentType === "VIDEO" && (
                                                        // <video src={message.fileUrl} className="max-w-xs" />
                                                        <video controls className="max-w-xs rounded-lg shadow-lg">
                                                            <source src={message.attachment.fileUrl} type="video/mp4" />
                                                            Your browser does not support the video tag.
                                                        </video>
                                                    )}
                                                    {message.messageContentType === "AUDIO" && (
                                                        <audio className="max-w-xs rounded-md" controls>
                                                            <source
                                                                src={message.attachment.fileUrl}
                                                                type="audio/mpeg"
                                                            />
                                                        </audio>
                                                    )}
                                                    {message.messageContentType === "FILE" && (
                                                        <div
                                                            className="w-full h-10 px-1 border flex items-center justify-center rounded-md gap-1 cursor-pointer bg-gray-600 line-clamp-2"
                                                            onClick={() =>
                                                                handleDownloadAttachmentFileByFetching(
                                                                    message.attachment
                                                                )
                                                            }
                                                        >
                                                            <FileCheck />
                                                            <p className="hover:text-sky-600 hover:underline text-xs">
                                                                {message.attachment.fileName}
                                                            </p>
                                                        </div>
                                                    )}
                                                </>
                                            )}
                                        </ChatBubbleMessage>
                                        <Menubar className="border-none bg-transparent !p-0 outline-none shadow-none h-7">
                                            <MenubarMenu>
                                                <MenubarTrigger className="!border-none outline-none border-0 p-0">
                                                    <div className="text-gray-800 relative w-6 h-6 hover:bg-gray-500 rounded-full hover:text-white cursor-pointer">
                                                        <EllipsisVertical
                                                            className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2"
                                                            size={15}
                                                        />
                                                    </div>
                                                </MenubarTrigger>
                                                <MenubarContent className="!min-w-[100px]">
                                                    {message.sender.id === currentUser.id && (
                                                        <MenubarItem onClick={() => handleDeleteMessage(message)}>
                                                            Delete message
                                                        </MenubarItem>
                                                    )}
                                                    <MenubarSeparator />
                                                    <MenubarItem>Share</MenubarItem>
                                                    <MenubarSeparator />
                                                    <MenubarItem>Print</MenubarItem>
                                                </MenubarContent>
                                            </MenubarMenu>
                                        </Menubar>
                                    </ChatBubble>
                                </Fragment>
                            );
                        })}
                        <div ref={messagesEndRef}></div>
                    </ChatMessageList>
                </ScrollArea>
            </CardContent>
            <CardFooter className="p-2">
                <div className="flex flex-row items-center gap-3 w-full">
                    <div className="flex items-center gap-3">
                        <CirclePlus stroke="blue" className="w-6 h-6 cursor-pointer" />
                        {/* <Camera stroke="blue" className="w-6 h-6 cursor-pointer" /> */}
                        <FileUploadModal sendMediaFiles={handleSendingMediaFiles}>
                            <Paperclip stroke="blue" className="w-6 h-6 cursor-pointer" />
                            {/* <ImageUp stroke="blue" className="w-6 h-6 cursor-pointer" /> */}
                        </FileUploadModal>
                    </div>
                    <div className="relative flex-grow">
                        <label>
                            <AutosizeTextarea
                                className="rounded-md w-full bg-gray-800 text-gray-200 !pr-8 p-[.4rem]"
                                value={chatInput}
                                onChange={(e) => setChatInput(e.target.value)}
                                placeholder="Enter your message"
                            />
                            <button
                                type="button"
                                className="absolute bottom-3 -right-1 mt-2 mr-3 flex flex-shrink-0 focus:outline-none text-blue-600 hover:text-blue-700 w-5 h-5"
                            >
                                <SmilePlus stroke="aqua" />
                            </button>
                        </label>
                    </div>
                    <button
                        type="button"
                        className="flex flex-shrink-0 focus:outline-none text-blue-600 hover:text-blue-700 w-6 h-6"
                        onClick={handleSendMessage}
                    >
                        <SendHorizontal fill="aqua" />
                    </button>
                </div>
            </CardFooter>
        </Card>
    );
}

export default ChatWindowPopup;

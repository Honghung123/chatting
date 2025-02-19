"use client";

import { callGetNewConversationId, getChatHistoryList } from "@/apis/chat-api";
import { stompClient } from "@/app/(chat)/chat/socket";
import ChatUpload from "@/components/shared/chatbox/chat-upload-file";
import FileUploadModal from "@/components/shared/chatbox/file-upload-modal";
import { Input } from "@/components/ui/input";
import {
    Menubar,
    MenubarContent,
    MenubarItem,
    MenubarMenu,
    MenubarSeparator,
    MenubarShortcut,
    MenubarTrigger,
} from "@/components/ui/menubar";
import { toast } from "@/hooks/use-toast";
import { useCachedChatHistories } from "@/lib/react-query/notificationCache";
import { ConversationType, MessageChatType } from "@/schema/chat.schema";
import { AttachmentRequestType, AttachmentType, FileUploadResponseType } from "@/schema/media.schema";
import { UserType } from "@/schema/user.schema";
import {
    Camera,
    CirclePlus,
    EllipsisVertical,
    FileCheck,
    ImageUp,
    Info,
    Paperclip,
    PhoneCall,
    SendHorizontal,
    Video,
} from "lucide-react";
import { useEffect, useLayoutEffect, useRef, useState } from "react";

export default function ChatAreaSection({
    conversation,
    currentUser,
}: {
    conversation: ConversationType;
    currentUser: UserType;
}) {
    const [chatInput, setChatInput] = useState<string>("Hello!");
    const [messageType, setMessageType] = useState<string>("TEXT");

    const [listChatMessages, setListChatMessages] = useState<MessageChatType[]>([]);
    const chatMessagesRef = useRef(listChatMessages);
    const messagesEndRef = useRef<HTMLDivElement | null>(null);
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    const handleSubscribeMessage = (conId: string) => {
        stompClient!.subscribe(`/topic/direct/${conId}`, function (message: any) {
            const newMessage = JSON.parse(message.body) as MessageChatType;
            console.log(newMessage);
            setListChatMessages((prev) => [...prev, newMessage]);
        });
        stompClient!.subscribe(`/topic/deleted-message/${conId}`, function (message: any) {
            const deletedMessageId = JSON.parse(message.body) as number;
            if (deletedMessageId == -1) return;
            const currentListChatMessages = chatMessagesRef.current;
            const deletedMessageIndex = currentListChatMessages.findIndex((m) => m.messageId === deletedMessageId);
            const messages = [...currentListChatMessages];
            messages.splice(deletedMessageIndex, 1);
            setListChatMessages(messages);
        });
    };

    useEffect(() => {
        stompClient.activate();
        if (!conversation.conversationId) {
            setListChatMessages([]);
            return;
        }
        const fetchMessages = async () => {
            const messages = await getChatHistoryList(conversation.conversationId);
            if (messages.hasOwnProperty("errorCode")) return;
            setListChatMessages(messages as MessageChatType[]);
        };
        fetchMessages();
        if (!stompClient.connected) {
            stompClient.activate();
            stompClient!.onConnect = async () => {
                handleSubscribeMessage(conversation.conversationId!);
            };
        } else {
            handleSubscribeMessage(conversation.conversationId!);
        }
        return () => {
            stompClient.deactivate();
        };
    }, [conversation]);

    useEffect(() => {
        scrollToBottom();
    }, []);

    useEffect(() => {
        chatMessagesRef.current = listChatMessages;
        scrollToBottom();
    }, [listChatMessages]);

    const handleGetSessionConversationId = () => {
        const storedConversationId =
            sessionStorage.getItem(`conversationId-${conversation.user.id}-${currentUser.id}`) ||
            sessionStorage.getItem(`conversationId-${currentUser.id}-${conversation.user.id}`) ||
            null;
        return storedConversationId;
    };

    const handleSetSessionConversationId = (conversationId: string) => {
        if (typeof conversationId === "string") {
            sessionStorage.setItem(
                `conversationId-${currentUser.id}-${conversation.user.id}`,
                conversationId as string
            );
            return conversationId as string;
        }
        return null;
    };

    const establishConnection = async (): Promise<string | null> => {
        const conversationId = (await callGetNewConversationId(currentUser.id, conversation.user.id)) as string;
        if (!conversationId) return null;
        console.log("Created new conversation: " + conversationId);
        return conversationId;
    };

    const getConversationIdOrCreateNewIfNotExist = async () => {
        let conversationId: string | null = conversation.conversationId || handleGetSessionConversationId();
        if (!conversationId) {
            console.log("Establishing connection...");
            conversationId = await establishConnection();
            if (!conversationId) return;
            handleSetSessionConversationId(conversationId);
            if (!stompClient.connected) {
                stompClient.activate();
                stompClient!.onConnect = async () => {
                    handleSubscribeMessage(conversationId!);
                };
            } else {
                handleSubscribeMessage(conversationId!);
            }
        }
        return conversationId;
    };

    const handleSendMessage = async () => {
        if (chatInput.trim() === "") return;
        const conversationId = await getConversationIdOrCreateNewIfNotExist();
        stompClient!.publish({
            destination: `/app/direct`,
            body: JSON.stringify({
                conversationId: conversationId,
                content: chatInput,
                sender: currentUser.id,
                // recipient: conversation.user.id,
                messageContentType: messageType,
            }),
        });
        setChatInput("");
        setMessageType("TEXT");
    };

    const determineFileType = (fileFormat: string) => {
        if (["png", "jpg", "jpeg"].includes(fileFormat)) return "IMAGE";
        if (["mp4", "mkv"].includes(fileFormat)) return "VIDEO";
        if (["mp3", "wav"].includes(fileFormat)) return "AUDIO";
        return "FILE";
    };

    const handleSendingMediaFiles = async (files: FileUploadResponseType[]) => {
        const conversationId = await getConversationIdOrCreateNewIfNotExist();
        files.forEach((file) => {
            const attachment: AttachmentRequestType = {
                fileId: file.publicId,
                fileName: file.displayName,
                fileUrl: file.url,
                format: file.format,
            };
            console.log(attachment);
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

    const handleDeleteMessage = (message: MessageChatType) => {
        if (currentUser.id !== message.sender.id) return;
        console.log(message);
        stompClient!.publish({
            destination: `/app/message/delete`,
            body: JSON.stringify({
                conversationId: message.conversationId,
                messageId: message.messageId,
                senderId: message.sender.id,
                fileId: message?.attachment.fileId || null,
            }),
        });
    };

    const handleDownloadAttachmentFile = (attachment: AttachmentType) => {
        const link = document.createElement("a");
        link.href = attachment.fileUrl;
        link.download = attachment.fileName; // Tên file sẽ được lưu về máy
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

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

    let lastUserIndex = listChatMessages.findLastIndex((message) => message.sender.id === currentUser.id);
    let lastOtherIndex = listChatMessages.findLastIndex((message) => message.sender.id === conversation.user.id);
    return (
        <>
            <section className="flex flex-col flex-auto border-l">
                <div className="chat-header px-6 py-4 flex flex-row flex-none justify-between items-center shadow">
                    <div className="flex">
                        <div className="w-12 h-12 mr-4 relative flex flex-shrink-0">
                            <img
                                className="shadow-md rounded-full w-full h-full object-cover"
                                src={conversation.user.avatarUrl || "https://randomuser.me/api/portraits/women/34.jpg"}
                                alt={conversation.user.name}
                            />
                        </div>
                        <div className="text-sm">
                            <p className="font-bold">{conversation.user.name}</p>
                            <p>Active 1h ago</p>
                        </div>
                    </div>

                    <div className="flex gap-3">
                        <PhoneCall stroke="blue" />
                        <Video stroke="blue" />
                        <Info stroke="blue" />
                    </div>
                </div>

                <div className="chat-body p-4 flex-1 overflow-y-scroll space-y-2">
                    {listChatMessages.length == 0 && !conversation.conversationId && (
                        <p className="text-center text-gray-400">Start a conversation</p>
                    )}
                    {listChatMessages.map((message, index) => (
                        <div
                            className={`flex gap-2 group ${
                                message.sender.id === currentUser.id ? "justify-end" : "justify-start"
                            }`}
                            key={index}
                        >
                            <div
                                className={`flex gap-2 ${
                                    message.sender.id === currentUser.id ? "flex-row-reverse" : "flex-row"
                                }`}
                            >
                                <div className="w-6 h-6 relative flex flex-shrink-0">
                                    {(index == lastOtherIndex || index == lastUserIndex) && (
                                        <img
                                            className="shadow-md rounded-full w-full h-full object-cover"
                                            src={
                                                message.sender?.avatarUrl ||
                                                `https://randomuser.me/api/portraits/women/33.jpg`
                                            }
                                            alt={message.sender.name}
                                        />
                                    )}
                                </div>
                                <div className="messages text-sm text-gray-700 grid grid-flow-row gap-2">
                                    <div
                                        className={`flex items-center ${
                                            message.sender.id === currentUser.id ? "flex-row-reverse" : "flex-row"
                                        }`}
                                    >
                                        {message.messageContentType === "TEXT" && (
                                            <p
                                                className={`px-3 py-1 bg-gray-800 max-w-xs lg:max-w-md text-gray-200 cursor-pointer ${
                                                    message.sender.id === currentUser.id
                                                        ? "rounded-b-full rounded-l-full"
                                                        : "rounded-t-full rounded-r-full"
                                                }`}
                                            >
                                                {message.content}
                                            </p>
                                        )}
                                        {message.messageContentType === "IMAGE" && (
                                            <img src={message.attachment.fileUrl} alt="Image" className="max-w-xs" />
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
                                                <source src={message.attachment.fileUrl} type="audio/mpeg" />
                                            </audio>
                                        )}
                                        {message.messageContentType === "FILE" && (
                                            <div
                                                className="w-full h-10 px-4 text-xs border flex items-center justify-center rounded-md gap-3 cursor-pointer"
                                                onClick={() =>
                                                    handleDownloadAttachmentFileByFetching(message.attachment)
                                                }
                                            >
                                                <FileCheck />
                                                <p className="hover:text-sky-600 hover:underline">
                                                    {message.attachment.fileName}
                                                </p>
                                            </div>
                                        )}

                                        <div className="block">
                                            <Menubar>
                                                <MenubarMenu>
                                                    <MenubarTrigger className="!border-none outline-none border-0">
                                                        <div className="text-gray-800 relative w-6 h-6 hover:bg-gray-600 rounded-full hover:text-white">
                                                            <EllipsisVertical
                                                                className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2"
                                                                size={18}
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
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                    {/* <p className="p-4 text-center text-sm text-gray-500">12:40 PM</p> */}
                    <div ref={messagesEndRef}></div>
                </div>
                <div className="chat-footer flex-none">
                    <div className="flex flex-row items-center p-4 gap-3">
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
                                <Input
                                    className="rounded-full py-2 pl-3 pr-10 w-full border border-gray-800 focus:border-gray-700 bg-gray-800 focus:bg-gray-900 focus:outline-none text-gray-200 focus:shadow-md transition duration-300 ease-in"
                                    value={chatInput}
                                    onChange={(e) => setChatInput(e.target.value)}
                                    placeholder="Aa"
                                />
                                <button
                                    type="button"
                                    className="absolute top-0 right-0 mt-2 mr-3 flex flex-shrink-0 focus:outline-none block text-blue-600 hover:text-blue-700 w-6 h-6"
                                >
                                    <svg viewBox="0 0 20 20" className="w-full h-full fill-current">
                                        <path d="M10 20a10 10 0 1 1 0-20 10 10 0 0 1 0 20zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zM6.5 9a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm7 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm2.16 3a6 6 0 0 1-11.32 0h11.32z" />
                                    </svg>
                                </button>
                            </label>
                        </div>
                        <button
                            type="button"
                            className="flex flex-shrink-0 focus:outline-none mx-2 block text-blue-600 hover:text-blue-700 w-6 h-6"
                            onClick={handleSendMessage}
                        >
                            <SendHorizontal fill="aqua" />
                        </button>
                    </div>
                </div>
            </section>
        </>
    );
}

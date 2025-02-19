"use client";

import { callUploadImage } from "@/apis/media-api";
import ChatUpload from "@/components/shared/chatbox/chat-upload-file";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { FileUploadResponseType, FileUploadType } from "@/schema/media.schema";
import { useState } from "react";

export default function FileUploadModal({
    children,
    sendMediaFiles,
}: {
    children: React.ReactNode;
    sendMediaFiles: (files: FileUploadResponseType[]) => void;
}) {
    const [open, setOpen] = useState(false);
    const [isSending, setIsSending] = useState(false);
    const [files, setFiles] = useState<FileUploadType[]>([]);

    const handleSendFiles = async () => {
        if (isSending) return;
        setIsSending(true);
        const result = await callUploadImage(files);
        setIsSending(false);
        if (result && result.statusCode == 200) {
            console.log(result.data);
            sendMediaFiles(result.data as FileUploadResponseType[]);
            setOpen(false);
        }
    };

    return (
        <>
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>{children}</DialogTrigger>
                <DialogContent className="max-w-[80vw] sm:max-w-[60vw] lg:max-w-[40vw] max-h-[88vh] border border-gray-300 overflow-y-auto py-4 rounded-lg">
                    <DialogHeader>
                        <DialogTitle className="text-center text-2xl text-gradient">Upload file</DialogTitle>
                    </DialogHeader>
                    <div className="h-full w-full">
                        <ChatUpload onUpload={setFiles} />
                        <div className="flex justify-center pt-3">
                            <Button className="bg-lime-500 text-white" onClick={handleSendFiles}>
                                {isSending ? (
                                    <>
                                        Sending <span className="animate-spin">❄️</span>
                                    </>
                                ) : (
                                    <span>Send</span>
                                )}
                            </Button>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>
        </>
    );
}

"use client";
import { callAddANewPostRequest } from "@/apis/post-api";
import { callAddANewStoryRequest } from "@/apis/story-api";
import FileUploadModal from "@/components/shared/chatbox/file-upload-modal";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { toast } from "@/hooks/use-toast";
import { AttachmentType, FileUploadResponseType } from "@/schema/media.schema";
import { Paperclip } from "lucide-react";
import { useState } from "react";

export default function CreateStoryModal({
    children,
    refetchData,
}: {
    children: React.ReactNode;
    refetchData: () => void;
}) {
    const [mediaFiles, setMediaFiles] = useState<FileUploadResponseType[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [caption, setCaption] = useState("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
    const [open, setOpen] = useState(false);
    const handleAddNewStory = async () => {
        if (isLoading) return;
        setIsLoading(true);
        const payload = {
            caption: caption,
            mediaFiles: mediaFiles.map((media) => {
                return {
                    fileId: media.publicId,
                    fileName: media.displayName,
                    fileUrl: media.url,
                    format: media.format,
                } as AttachmentType;
            }),
        };
        const result = await callAddANewStoryRequest(payload.caption, payload.mediaFiles);
        refetchData();
        if (result != null) {
            toast({
                description: "Post new story successfully",
                duration: 3000,
                className: "bg-lime-500 text-white",
            });
        } else {
            toast({
                description: "Post new story failed. Please try again",
                duration: 3000,
                className: "bg-red-500 text-white",
            });
        }
        setIsLoading(false);
        setOpen(false);
    };
    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>{children}</DialogTrigger>
            <DialogContent className="!w-[90vw] min-h-[40vh] border border-gray-300 overflow-y-auto py-4 rounded-lg">
                <DialogHeader>
                    <DialogTitle className="text-center text-2xl text-gradient">Add New Story</DialogTitle>
                </DialogHeader>
                <div className="h-full w-full flex flex-col">
                    <Textarea value={caption} onChange={(e) => setCaption(e.target.value)} />
                    {mediaFiles.length > 0 && (
                        <div className="flex flex-wrap gap-2">
                            {mediaFiles.map((media, index) => (
                                <img src={media.url} key={index} className="w-40 object-cover" />
                            ))}
                        </div>
                    )}
                    <div className="justify-between p-2 flex">
                        <FileUploadModal sendMediaFiles={setMediaFiles}>
                            <Paperclip stroke="blue" className="w-6 h-6 cursor-pointer" />
                            {/* <ImageUp stroke="blue" className="w-6 h-6 cursor-pointer" /> */}
                        </FileUploadModal>
                        <Button onClick={handleAddNewStory}>Post</Button>
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
}

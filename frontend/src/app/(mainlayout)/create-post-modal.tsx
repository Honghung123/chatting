"use client";
import { callAddANewPostRequest } from "@/apis/post-api";
import FileUploadModal from "@/components/shared/chatbox/file-upload-modal";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { toast } from "@/hooks/use-toast";
import { AttachmentType, FileUploadResponseType } from "@/schema/media.schema";
import { UserType } from "@/schema/user.schema";
import { Paperclip, Images, SmilePlus, TvMinimalPlay } from "lucide-react";
import { useState } from "react";

export default function CreatePostModal({ user, refetchData }: { user: UserType | null; refetchData: () => void }) {
    const [mediaFiles, setMediaFiles] = useState<FileUploadResponseType[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [caption, setCaption] = useState("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
    const [open, setOpen] = useState(false);
    const handleAddNewPost = async () => {
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
        console.log(payload);
        const result = await callAddANewPostRequest(payload.caption, payload.mediaFiles);
        refetchData();
        if (result != null) {
            toast({
                description: "Create new post successfully",
                duration: 3000,
                className: "bg-lime-500 text-white",
            });
        } else {
            toast({
                description: "Create new post failed. Please try again",
                duration: 3000,
                className: "bg-red-500 text-white",
            });
        }
        setIsLoading(false);
        setOpen(false);
    };
    if (!user) return <></>;
    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <div className="flex flex-col rounded-lg bg-white p-3 px-4 shadow dark:bg-neutral-800">
                    <div className="mb-2 flex items-center space-x-2 border-b pb-3 dark:border-neutral-700">
                        <div className="h-10 w-10">
                            <img
                                src={user.avatarUrl || "https://random.imagecdn.app/200/200"}
                                className="h-full w-full rounded-full"
                                alt="dp"
                            />
                        </div>
                        <button className="h-10 flex-grow rounded-full bg-gray-100 pl-5 text-left text-gray-500 hover:bg-gray-200 focus:bg-gray-300 focus:outline-none dark:bg-neutral-700 dark:text-gray-300 dark:hover:bg-neutral-600 dark:focus:bg-neutral-700">
                            What&apos;s on your mind, {user.name}?
                        </button>
                    </div>
                    <div className="-mb-1 flex space-x-3 text-sm font-thin">
                        <button className="flex h-8 flex-1 items-center justify-center gap-2 rounded-md text-gray-600 hover:bg-gray-100 focus:bg-gray-200 focus:outline-none dark:text-gray-300 dark:hover:bg-neutral-700 dark:focus:bg-neutral-700">
                            <TvMinimalPlay className="h-4 w-4" />
                            <p className="font-semibold">Create Video</p>
                        </button>
                        <button className="flex h-8 flex-1 items-center justify-center space-x-2 rounded-md text-gray-600 hover:bg-gray-100 focus:bg-gray-200 focus:outline-none dark:text-gray-300 dark:hover:bg-neutral-700 dark:focus:bg-neutral-700">
                            <Images className="h-4 w-4" />
                            <p className="font-semibold">Photos/Video</p>
                        </button>
                        <button className="flex h-8 flex-1 items-center justify-center space-x-2 rounded-md text-gray-600 hover:bg-gray-100 focus:bg-gray-200 focus:outline-none dark:text-gray-300 dark:hover:bg-neutral-700 dark:focus:bg-neutral-700">
                            <SmilePlus className="h-4 w-4" />
                            <p className="font-semibold">Feeling/Activity</p>
                        </button>
                    </div>
                </div>
            </DialogTrigger>
            <DialogContent className="!w-[90vw] min-h-[40vh] border border-gray-300 overflow-y-auto py-4 rounded-lg">
                <DialogHeader>
                    <DialogTitle className="text-center text-2xl text-gradient">Add New Post</DialogTitle>
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
                        <Button onClick={handleAddNewPost}>Post</Button>
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
}

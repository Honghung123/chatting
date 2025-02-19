"use client";

import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { toast } from "@/hooks/use-toast";
import { useState } from "react";

export default function ClearChatMessages({
    handleClearChatMessges,
    children,
}: {
    handleClearChatMessges: () => void;
    children: React.ReactNode;
}) {
    const [openModal, setOpenModal] = useState(false);
    const alertActionIsProcessing = (message: string) => {
        toast({
            title: "Info",
            description: message,
            className: "bg-sky-500 text-white",
            duration: 12000,
        });
    };
    const handleRemoveChatMsg = async () => {
        alertActionIsProcessing("Clearing your chat");
        handleClearChatMessges();
        setOpenModal(false);
        toast({
            title: "Success",
            description: "Clear your chat successfully",
            className: "text-white bg-lime-500",
            duration: 3000,
        });
    };

    return (
        <>
            <Dialog open={openModal} onOpenChange={setOpenModal}>
                <DialogTrigger asChild>{children}</DialogTrigger>
                <DialogContent className="h-[24vh] max-w-[90vw] sm:max-w-[50vw] lg:max-w-[35vw] !p-1 bg-gray-200 rounded-lg z-[99999]">
                    <DialogTitle className="hidden"></DialogTitle>
                    <div className="p-4 px-10">
                        <h1 className="text-2xl font-semibold text-center">Clear chat</h1>
                        <div className="pt-2 pb-2 space-y-2">
                            <p className="text-center text-gray-700">Are you sure you want clear the entire chat?</p>
                        </div>
                        <div className="flex items-center mt-6">
                            <Button type="button" className="mx-auto" onClick={handleRemoveChatMsg}>
                                Clear
                            </Button>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>
        </>
    );
}

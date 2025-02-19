"use client";

import { callActivateAccountRequest } from "@/apis/user-api";
import Custom404PageNotFound from "@/app/not-found";
import { toast } from "@/hooks/use-toast";
import { LoginResponseDTO } from "@/schema/auth.schema";
import { ErrorResponseType, SuccessResponseType } from "@/schema/types/common";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useEffect } from "react";

export default function ActivateAccount() {
    const params = useSearchParams();
    const token = params.get("token");
    const { replace } = useRouter();
    useEffect(() => {
        const activateAcc = async () => {
            const result = await callActivateAccountRequest(token!);
            if (result.statusCode === 200) {
                const response = result as SuccessResponseType<LoginResponseDTO, null>;
                toast({
                    title: "Success",
                    description: response.message,
                    className: "bg-green-500 text-white",
                    duration: 2000,
                });
                localStorage.setItem("accessToken", response.data.accessToken);
                localStorage.setItem("refreshToken", response.data.refreshToken);
                replace("/");
            } else {
                const response = result as ErrorResponseType;
                let errorMessage = "An error occurred! Please try again.";
                if (response.statusCode != 500) {
                    errorMessage = response.message;
                }
                toast({
                    title: "Error",
                    description: errorMessage,
                    className: "bg-red-500 text-white",
                    duration: 2000,
                });
            }
        };
        if (!token) return;
        activateAcc();
    });
    if (!token) return <Custom404PageNotFound />;
    return (
        <div className="w-[100vw] h-[100vh] flex justify-center items-center">
            <div className="mt-4">
                <h2 className="text-4xl">We are verifying your account</h2>
                <p className="text-4xl text-center">Please wait!</p>
                <div className="flex justify-center py-3">
                    <div className="loader-dot"></div>
                </div>
            </div>
        </div>
    );
}

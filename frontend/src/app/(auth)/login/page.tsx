"use client";
import { BASE_API } from "@/apis/constants";
import { LoginForm } from "@/app/(auth)/login/login-form";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { redirect } from "next/navigation";
import GoogleIcon from "@/assets/images/google.svg";
import Image from "next/image";

export default function Login() {
    const handleGoogleLogin = (e: any) => {
        redirect(`${BASE_API}/oauth2/authorization/google`);
    };
    return (
        <div className="rounded-xl w-[330px] sm:min-w-[420px] relative p-[3px] overflow-hidden">
            <div className="bg-gradient-to-r from-sky-400 to-fuchsia-400 z-0 absolute w-[200%] h-[200%] top-[-50%] left-[-50%] animate-spin"></div>
            <div className="bg-white dark:bg-gray-800 relative z-10 rounded-lg p-4">
                <p className="text-5xl font-bold text-center dynamic-text pb-2">Login</p>
                <LoginForm />
                <div className="">
                    <div className="relative flex py-1 items-center">
                        <div className="flex-grow border-t border-gray-300"></div>
                        <span className="flex-shrink mx-4 text-gray-300"> or </span>
                        <div className="flex-grow border-t border-gray-300"></div>
                    </div>
                    <div className="flex justify-center gap-2">
                        <p className="text-center">Already have an account?</p>
                        <Link href="/register" className="text-sky-400 hover:underline">
                            Register
                        </Link>
                    </div>
                    <div className="flex justify-center flex-col gap-3 pt-4">
                        <Button
                            className="flex items-center bg-white dark:bg-gray-900 border border-gray-300 rounded-lg shadow-md px-6 py-2 text-sm font-medium text-gray-800 dark:text-white hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400"
                            onClick={handleGoogleLogin}
                        >
                            <Image priority src={GoogleIcon} alt="Google" width={20} />
                            <span>Continue with Google</span>
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}

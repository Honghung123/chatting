"use client";

import { callLogoutRequest } from "@/apis/user-api";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { UserType } from "@/schema/user.schema";
import { BookMarked, Heart, Star, LogOut } from "lucide-react";
import Link from "next/link";

export default function UserProfileDropdown({ children, user }: { children: React.ReactNode; user: UserType }) {
    const handleLogout = async () => {
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("accessToken");
        await callLogoutRequest();
        window.location.reload();
    };
    return (
        <Popover>
            <PopoverTrigger className="flex items-center">{children}</PopoverTrigger>
            <PopoverContent className="min-w-[20rem] border border-gray-200 !p-0">
                <div className="cursor-pointer hover:bg-slate-200 border-b p-3 pb-2">
                    <Link href="/profile">
                        <div className="flex items-center gap-3">
                            <img className="h-8 w-8 rounded-full" src={user?.avatarUrl ?? ""} alt="user image" />
                            <p>{user?.name || "Unknown"}</p>
                        </div>
                        <p className="text-xs text-sky-400 hover:text-sky-500">View profile</p>
                    </Link>
                </div>

                <Link href="/watchlist">
                    <div className="cursor-pointer bg-main-hover hover:bg-slate-200  flex gap-3 items-center p-2">
                        <BookMarked size={20} />
                        <span className="block w-full h-full">My watchlist</span>
                    </div>
                </Link>
                <div
                    className="cursor-pointer bg-main-hover hover:bg-slate-200 flex gap-3 items-center p-2"
                    onClick={handleLogout}
                >
                    <LogOut size={20} />
                    <span className="block w-full h-full">Logout</span>
                </div>
            </PopoverContent>
        </Popover>
    );
}

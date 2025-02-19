"use client";

import { callLogoutRequest } from "@/apis/user-api";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { toast } from "@/hooks/use-toast";
import { defaultAvatar } from "@/lib/utils";
import { UserType } from "@/schema/user.schema";
import { BookMarked, Heart, LogOut, Star } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function Profile({ user }: { user: UserType }) {
    const router = useRouter();
    const handleLogout = async () => {
        toast({
            title: "Logout",
            description: "You are logging out",
            className: "bg-sky-500 text-white",
            duration: 20000,
        });
        const result = await callLogoutRequest();
        if (result !== 200 && result !== 204) return;
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("accessToken");
        toast({
            title: "Success",
            description: "You have logged out!",
            className: "bg-lime-500 text-white",
            duration: 2000,
        });
        window.location.reload();
    };
    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild className="cursor-pointer">
                <Avatar className="h-8 w-8">
                    <AvatarImage src={user?.avatarUrl ?? defaultAvatar} alt="user image" />
                </Avatar>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-48 mt-1 mr-6 lg:mr-0">
                <DropdownMenuLabel className="cursor-pointer hover:bg-slate-200 border-b">
                    <Link href="/profile">
                        <p>{user?.name || "Unknown"}</p>
                        <p className="text-xs text-sky-400 hover:text-sky-500">View profile</p>
                    </Link>
                </DropdownMenuLabel>
                {/* <DropdownMenuSeparator /> */}
                <Link href="/watchlist">
                    <DropdownMenuItem className="cursor-pointer bg-main-hover">
                        <BookMarked size={16} />
                        <span className="block w-full h-full">My watchlist</span>
                    </DropdownMenuItem>
                </Link>
                <Link href="/favorite">
                    <DropdownMenuItem className="cursor-pointer bg-main-hover">
                        <Heart size={16} />
                        <span className="block w-full h-full">My favorite movies</span>
                    </DropdownMenuItem>
                </Link>
                <Link href="/rating">
                    <DropdownMenuItem className="cursor-pointer bg-main-hover border-b">
                        <Star size={16} />
                        <span className="block w-full h-full">My ratings</span>
                    </DropdownMenuItem>
                </Link>
                <DropdownMenuItem className="cursor-pointer bg-main-hover" onClick={handleLogout}>
                    <LogOut size={16} />
                    <span className="block w-full h-full">Logout</span>
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    );
}

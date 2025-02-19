"use client";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Bell } from "lucide-react";
import Link from "next/link";

export default function Notification() {
    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild className="cursor-pointer">
                <Bell size={24} strokeWidth={3} />
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-56 mt-2">
                <DropdownMenuLabel className="text-center text-md">Notifications</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuGroup>
                    <p className="cursor-pointer text-sm text-gray-700 px-3 py-1">A message notification</p>
                </DropdownMenuGroup>
                <DropdownMenuSeparator />
                <div className="cursor-pointer">
                    <Link href="/notification">
                        <p className="text-center text-sm text-blue-500 underline">View all</p>
                    </Link>
                </div>
            </DropdownMenuContent>
        </DropdownMenu>
    );
}

"use client";
import Notification from "@/components/shared/header/notification";
import Profile from "@/components/shared/header/profile";
// import SearchMovie from "@/components/shared/header/search";
import { useCachedUserInfo } from "@/lib/react-query/userCache";
import { UserType } from "@/schema/user.schema";
import Link from "next/link";

export default function RightHeader() {
    const { data: currentUser, isFetching, isLoading } = useCachedUserInfo();
    if (isFetching || isLoading) {
        return (
            <div className="flex items-center gap-6 text-white font-semibold">
                {/* <ChangeLanguageDropdownMenu />
                <SearchMovie /> */}
            </div>
        );
    }
    if (!isFetching && !isLoading && !currentUser) {
        return (
            <>
                <Link href="/login">Login</Link>
                <Link href="/register">Register</Link>
            </>
        );
    }
    if (!isFetching && !isLoading && currentUser && currentUser.hasOwnProperty("errorCode")) {
        return (
            <>
                <Link href="/login">Login</Link>
                <Link href="/register">Register</Link>
            </>
        );
    }
    // console.log(currentUser);
    return (
        <div className="flex items-center gap-6 text-white font-semibold">
            {/* <ChangeLanguageDropdownMenu /> */}
            {currentUser ? (
                <>
                    <Notification />
                    <Profile user={currentUser as UserType} />
                </>
            ) : (
                <>
                    <Link href="/login">Login</Link>
                    <Link href="/register">Register</Link>
                </>
            )}
            {/* <SearchMovie /> */}
        </div>
    );
}

"use client";
import { callSuggestFriendInfoRequest } from "@/apis/user-api";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel";
import { defaultAvatar } from "@/lib/utils";
import { PaginationV2Type } from "@/schema/types/common";
import { UserType } from "@/schema/user.schema";
import { UserRoundPlus } from "lucide-react";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function SuggestFriends({ user }: { user: UserType | null }) {
    const [suggestFriendList, setSuggestFriendList] = useState<UserType[]>([]);
    useEffect(() => {
        const fetchFriendList = async () => {
            const result = await callSuggestFriendInfoRequest();
            if (result.hasOwnProperty("errorCode")) return;
            const suggestFriends = result as PaginationV2Type<UserType>;
            setSuggestFriendList(suggestFriends.data);
        };
        if (user) {
            fetchFriendList();
        }
    }, []);
    if (!user) return <></>;

    return (
        <div className="select-none">
            <h1 className="text-lg md:text-2xl font-semibold">Other friends you may know:</h1>
            <Carousel
                opts={{
                    align: "center",
                }}
                className="w-full"
            >
                <CarouselContent>
                    {suggestFriendList.map((friend, index) => (
                        <CarouselItem key={index} className="basis-1/2 md:basis-1/3 lg:basis-1/4">
                            <FriendCard friend={friend} />
                        </CarouselItem>
                    ))}
                </CarouselContent>
                <CarouselPrevious className="-left-4" />
                <CarouselNext className="-right-4" />
            </Carousel>
        </div>
    );
}

function FriendCard({ friend }: { friend: UserType }) {
    return (
        <div className="flex flex-col bg-white rounded-md">
            <div className="h-40 rounded-md">
                <Link href={`/profile/${friend.id}`}>
                <img
                    src={friend.avatarUrl || defaultAvatar}
                    alt={friend.name}
                    className="w-full h-full object-cover object-center rounded-t-md border-b"
                    />
                </Link>
            </div>
            <div className="px-2 pb-2">
                <p className="font-semibold line-clamp-1 pb-1">{friend.name}</p>
                <div className="flex items-center justify-center">
                    <button className="flex items-center justify-center gap-1 px-3 py-1 bg-black text-white rounded-lg">
                        <UserRoundPlus size={17} />
                        <span className="text-sm">Add friend</span>
                    </button>
                </div>
            </div>
        </div>
    );
}

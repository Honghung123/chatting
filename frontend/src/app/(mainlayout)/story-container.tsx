"use client";

import CreateStoryModal from "@/app/(mainlayout)/create-story-modal";
import Story from "@/app/(mainlayout)/story";
import { Carousel, CarouselContent, CarouselItem } from "@/components/ui/carousel";
import { useCachedGetAllStory } from "@/lib/react-query/homePageCache";
import { StoryType } from "@/schema/story.schema";
import { PaginationV2Type } from "@/schema/types/common";
import { UserType } from "@/schema/user.schema";
import { Plus } from "lucide-react";
import { useState } from "react";

export default function StoryContainer({ user }: { user: UserType | null }) {
    if (!user) return <></>;
    const { data: storiesData, isLoading, isFetching, refetch } = useCachedGetAllStory(1, 10);
    if (isLoading || isFetching) return <p className="py-4 px-2">Loading. . .</p>;
    if (!isLoading && !isFetching && !storiesData)
        return <p className="py-4 px-2">There is an error to display stories</p>;
    const stories = storiesData! as PaginationV2Type<StoryType>;
    return (
        <div className="h-50 my-6 flex w-full cursor-pointer items-center justify-center space-x-2 overflow-hidden">
            <CreateStoryModal refetchData={refetch}>
                <div
                    className="relative h-48 w-40 rounded-xl shadow"
                    style={{
                        backgroundImage: `url("${user.avatarUrl}")`,
                        backgroundSize: "cover", // Đảm bảo ảnh không bị méo
                        backgroundPosition: "center", // Căn giữa ảnh
                        backgroundRepeat: "no-repeat", // Không lặp lại ảnh
                    }}
                >
                    <div className="absolute flex w-full justify-center" style={{ bottom: "11%" }}>
                        <button className="z-40 h-10 w-10 rounded-full border-4 border-white bg-sky-500 focus:outline-none dark:border-neutral-800 flex justify-center items-center">
                            <Plus className="text-white" strokeWidth={4} />
                        </button>
                    </div>
                    <div className="absolute bottom-0 z-30 h-auto w-full rounded-b-lg bg-white p-2 pt-4 text-center dark:bg-neutral-800">
                        <p className="text-xs font-semibold text-gray-500 dark:text-gray-200">Create Story</p>
                    </div>
                </div>
            </CreateStoryModal>
            <Carousel
                opts={{
                    align: "center",
                }}
                className="w-full pl-2"
            >
                <CarouselContent>
                    {stories.data.map((story, index) => (
                        <CarouselItem key={index} className="basis-1/2 md:basis-1/3 lg:basis-1/4">
                            <Story story={story} />
                        </CarouselItem>
                    ))}
                </CarouselContent>
            </Carousel>
        </div>
    );
}

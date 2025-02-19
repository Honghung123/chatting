"use client";
import { stompClient } from "@/app/(chat)/chat/socket";
import LeftSidebar from "@/app/(mainlayout)/left-sidebar";
import PostContainer from "@/app/(mainlayout)/post-container";
import RightSidebar from "@/app/(mainlayout)/right-sidebar";
import StoryContainer from "@/app/(mainlayout)/story-container";
import SuggestFriends from "@/app/(mainlayout)/suggest-friends";
import Loading from "@/app/loading";
import Navbar from "@/components/shared/header/navbar";
import { useCachedUserInfo } from "@/lib/react-query/userCache";
import { useAppDispatch } from "@/lib/redux/hooks";
import { updateStompClient } from "@/lib/redux/slices/stompClientSlice";
import { UserType } from "@/schema/user.schema";
import Head from "next/head";
import { useEffect, useLayoutEffect, useRef } from "react";

export default function Home() {
    const { data: user, isLoading, isFetching } = useCachedUserInfo();
    useEffect(() => {
        stompClient.activate();
        if (user && !user.hasOwnProperty("errorCode")) {
            const currentUser = user as UserType;
            stompClient!.onConnect = () => {
                console.log("Set user online!");
                // stompClient!.subscribe(`/topic/online/${currentUser.id}`, function (message: any) {
                //     const newMessage = JSON.parse(message.body);
                //     console.log("New message: " + newMessage.content);
                // });
                stompClient!.publish({
                    destination: `/app/online/${currentUser.id}`,
                    body: JSON.stringify(null),
                });
            };
            // stompClient!.onDisconnect = () => {
            //     console.log("Set user offline!");
            //     stompClient!.publish({
            //         destination: `/app/offline/${currentUser.id}`,
            //         body: JSON.stringify(null),
            //     });
            // };
        }
        return () => {
            console.log("Clear func socket call");
            stompClient.deactivate();
        };
    }, [user]);
    if (isLoading || isFetching) return <Loading />;
    const currentUser = user && !user.hasOwnProperty("errorCode") ? (user as UserType) : null;
    return (
        <>
            <Head>
                <title>Home page</title>
            </Head>
            <div className="my-section bg-gradient-to-r from-sky-100 to-fuchsia-100 dark:from-sky-950 dark:to-fuchsia-950">
                <div className="flex min-h-full w-full flex-col">
                    <Navbar user={currentUser} />
                    <div className="mt-14 min-h-[calc(100%-4rem)]">
                        <div className="flex">
                            <LeftSidebar user={currentUser} />
                            <div className="flex-1">
                                <div className="m-auto w-[42.5rem]">
                                    <div className="mt-6 h-full w-full pb-5 space-y-4">
                                        {/* Story Section */}
                                        <StoryContainer user={currentUser} />
                                        <SuggestFriends user={currentUser} />
                                        {/* All posts */}
                                        <PostContainer user={currentUser} />
                                    </div>
                                </div>
                            </div>
                            <RightSidebar user={currentUser} stompClient={stompClient} />
                        </div>
                    </div>
                    {/* <MainChatBox user={currentUser} /> */}
                </div>
                {/* <div className="content-wrapper">
            </div> */}
            </div>
        </>
    );
}

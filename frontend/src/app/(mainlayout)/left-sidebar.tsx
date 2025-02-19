"use client";

import { UserType } from "@/schema/user.schema";
import { Bookmark, CircleUserRound, Clock, Rss, Store, TvMinimalPlay, UsersRound } from "lucide-react";
import Link from "next/link";

const LeftSidebar: React.FC<{ user: UserType | null }> = ({ user }) => {
    return (
        <div className="sticky top-[56px] h-[calc(100vh-56px)] w-[22.5rem] overflow-y-auto px-2 py-3">
            <ul className="text-black dark:text-gray-200">
                {user && (
                    <>
                        <Link href={"/profile"}>
                            <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                                <img className="h-8 w-8 rounded-full" src={user.avatarUrl} alt="user" />
                                <div>
                                    <p className="text-sm font-semibold">{user.name}</p>
                                </div>
                            </li>
                        </Link>
                        <Link href={"/friends"}>
                            <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                                <UsersRound className="h-8 w-8" />
                                <div>
                                    <p className="text-sm font-semibold">Friends</p>
                                </div>
                            </li>
                        </Link>
                        <Link href={"/memories"}>
                            <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                                <Clock className="h-8 w-8" />
                                <div>
                                    <p className="text-sm font-semibold">Memories</p>
                                </div>
                            </li>
                        </Link>
                        <Link href={"/saved"}>
                            <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                                <Bookmark className="h-8 w-8" />
                                <div>
                                    <p className="text-sm font-semibold">Saved</p>
                                </div>
                            </li>
                        </Link>
                        <Link href={"/group"}>
                            <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                                <CircleUserRound className="h-8 w-8" />
                                <div>
                                    <p className="text-sm font-semibold">Groups</p>
                                </div>
                            </li>
                        </Link>
                    </>
                )}
                <Link href={"/group"}>
                    <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                        <TvMinimalPlay className="h-8 w-8" />
                        <div>
                            <p className="text-sm font-semibold">Video</p>
                        </div>
                    </li>
                </Link>
                <Link href={"/group"}>
                    <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                        <Store className="h-8 w-8" />
                        <div>
                            <p className="text-sm font-semibold">Marketplace</p>
                        </div>
                    </li>
                </Link>
                <Link href={"/group"}>
                    <li className="justify-content mb-2 flex h-12 cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-gray-200 dark:hover:bg-neutral-800">
                        <Rss className="h-8 w-8" />
                        <div>
                            <p className="text-sm font-semibold">Feeds</p>
                        </div>
                    </li>
                </Link>
            </ul>
        </div>
    );
};

export default LeftSidebar;

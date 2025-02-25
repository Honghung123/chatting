"use client";
import MyNotification from "@/app/(mainlayout)/notification";
import { navLinks } from "@/components/shared/header/nav-links";
import UserProfileDropdown from "@/components/shared/header/profile-dropdown";
import { Button } from "@/components/ui/button";
import { UserType } from "@/schema/user.schema";
import { Bell, Facebook, Grip, MessageCircle } from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";

const Navbar: React.FC<{ user: UserType | null }> = ({ user }) => {
    const pathName = usePathname();
    return (
        <div className="fixed z-50 grid h-14 w-full grid-cols-7 gap-4 bg-white shadow-sm dark:bg-[#242526] px-4">
            <div className="col-span-2 flex items-center">
                <div className="ml-2 flex items-center">
                    <div className="h-10 text-primary">
                        <Link href="/">
                            {/* <Button className="h-10 w-10 rounded-full bg-blue-600 text-white hover:bg-primary-focus">
                                <Facebook stroke="white" fill="white" />
                            </Button> */}
                            <img
                                src={"https://cdn-icons-png.flaticon.com/512/2111/2111402.png"}
                                alt="Social"
                                className="h-10 w-10"
                            />
                        </Link>
                    </div>

                    <div className="h-10">
                        <input
                            placeholder="Search Facebook"
                            className="dark:placeholder:text-gra ml-2 h-full rounded-full bg-gray-100 px-3 pr-4 placeholder:text-neutral-400 focus:outline-none dark:bg-neutral-700"
                        />
                    </div>
                </div>
            </div>
            <div className="col-span-3 flex items-center justify-center space-x-2">
                {navLinks.map((link, index) => (
                    <Link href={link.href} key={index}>
                        <div className="flex h-12 w-24 cursor-pointer items-center justify-center rounded-lg hover:bg-gray-100 dark:hover:bg-neutral-700">
                            <div className="relative flex h-auto w-14 items-center justify-center">
                                <div className={`${pathName === link.href ? "text-primary" : "text-gray-400"}`}>
                                    <link.icon className="h-6 w-6" />
                                </div>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>
            <div className="col-span-2 flex items-center justify-end">
                <div className="flex h-10 w-auto items-center space-x-2 pr-2">
                    {user ? (
                        <>
                            <button className="h-10 w-10 rounded-full bg-gray-200 hover:bg-neutral-600 focus:outline-none dark:bg-neutral-700 dark:text-gray-200 flex justify-center items-center">
                                <Grip />
                            </button>
                            <button className="h-10 w-10 rounded-full bg-gray-200 hover:bg-neutral-600 focus:outline-none dark:bg-neutral-700 dark:text-gray-200 flex justify-center items-center">
                                <MessageCircle />
                            </button>
                            <MyNotification user={user!}>
                                <div className="h-10 w-10 rounded-full bg-gray-200 hover:bg-neutral-600 focus:outline-none dark:bg-neutral-700 dark:text-gray-200 flex justify-center items-center">
                                    <Bell />
                                </div>
                            </MyNotification>
                            <UserProfileDropdown user={user!}>
                                <img
                                    src={user!.avatarUrl}
                                    className="h-10 w-10 rounded-full bg-gray-200 hover:bg-neutral-600 focus:outline-none dark:bg-neutral-700 dark:text-gray-200"
                                />
                            </UserProfileDropdown>
                        </>
                    ) : (
                        <>
                            <Link href="/login">
                                <Button className="bg-sky-600">Login</Button>
                            </Link>
                            <Link href="/register">
                                <Button className="bg-sky-600">Register</Button>
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Navbar;

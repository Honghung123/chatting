"use client";
import RightHeader from "@/components/shared/header/header-right-section";

import Link from "next/link";
import { Suspense } from "react";

const APP_NAME = "TMDB";

export default function Header() {
    return (
        <>
            <header className="w-full border-b bg-main">
                <div className="wrapper flex justify-between items-center py-3 content-wrapper">
                    <div className="flex-start flex items-center gap-4">
                        <Link href="/" className="flex-start">
                            <img
                                src="https://www.themoviedb.org/assets/2/v4/logos/v2/blue_short-8e7b30f73a4020692ccca9c88bafe5dcb6f8a62a4c6bc55cd9ba82bb2cd95f6c.svg"
                                className="h-5 w-auto"
                                alt={`${APP_NAME} logo`}
                            />
                        </Link>
                        {/* <Navigation routes={navRoutes} /> */}
                    </div>
                    <div className="hidden md:block">{/* <Search /> */}</div>
                    <div className="flex justify-center items-center">
                        <Suspense fallback={<div className="text-white font-semibold">Loading...</div>}>
                            <RightHeader />
                        </Suspense>
                    </div>
                </div>
                <div className="md:hidden block px-5 pb-2">{/* <Search /> */}</div>
            </header>
        </>
    );
}

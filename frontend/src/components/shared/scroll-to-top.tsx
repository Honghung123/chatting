"use client";

import { Button } from "@/components/ui/button";
import { ArrowUpFromLine } from "lucide-react";
import React, { useEffect, useState } from "react";

export default function ScrollToTop() {
    const [isVisible, setIsVisible] = useState(false);
    const handleScroll = () => {
        if (window.scrollY > 500) {
            setIsVisible(true);
        } else {
            setIsVisible(false);
        }
    };
    useEffect(() => {
        window.addEventListener("scroll", handleScroll);
        return () => {
            window.removeEventListener("scroll", handleScroll);
        };
    }, []);
    return (
        <>
            {isVisible && (
                <div className="fixed bottom-3 right-3 lg:bottom-6 lg:right-6">
                    <Button
                        variant="outline"
                        onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
                        className="animate-bounce rounded-full !hover:bg-main !hover:text-white"
                    >
                        <ArrowUpFromLine size={48} />
                    </Button>
                </div>
            )}
        </>
    );
}

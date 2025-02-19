"use client";

import { StoryType } from "@/schema/story.schema";

const Story: React.FC<{ story: StoryType }> = ({ story }) => {
    return (
        <div
            className="relative h-48 w-full cursor-pointer rounded-xl p-3 shadow bg-sky-500"
            style={{
                backgroundImage:
                    story.attachment && story.attachment[0]?.fileUrl ? `url("${story.attachment[0]?.fileUrl}")` : ``,
                backgroundSize: "cover", // Đảm bảo ảnh không bị méo
                backgroundPosition: "center", // Căn giữa ảnh
                backgroundRepeat: "no-repeat", // Không lặp lại ảnh
            }}
        >
            <div className="absolute">
                <img
                    src={
                        story.user.avatarUrl ||
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/330px-Image_created_with_a_mobile_phone.png"
                    }
                    className="h-10 w-10 rounded-full border-4 border-white dark:border-neutral-800"
                    alt="story"
                />
                {story.attachment && story.attachment.length == 0 && (
                    <div className="flex justify-center items-center bg-gradient">
                        <p className="text-white text-xs">{story.caption}</p>
                    </div>
                )}
            </div>
            <div className="absolute text-center" style={{ bottom: "5%" }}>
                <p className="font-semibold text-white">User story</p>
            </div>
        </div>
    );
};

export default Story;

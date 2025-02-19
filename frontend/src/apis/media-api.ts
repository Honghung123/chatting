import { BASE_API } from "@/apis/constants";
import { FileUploadResponseType, FileUploadType } from "@/schema/media.schema";
import { SuccessResponseType } from "@/schema/types/common";
import axios from "axios";

export async function callUploadImage(
    payload: FileUploadType[]
): Promise<SuccessResponseType<FileUploadResponseType[], null> | null> {
    try {
        const formData = new FormData();
        payload.forEach((item) => {
            formData.append("files", item.file);
            formData.append("types", item.type);
        });
        const result = (
            await axios.post(`${BASE_API}/file/upload`, formData, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                    "Content-Type": "multipart/form-data",
                },
            })
        ).data as SuccessResponseType<FileUploadResponseType[], null>;
        return result;
    } catch (error: any) {
        return null;
    }
}

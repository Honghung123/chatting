"use client";
import { useCallback, useRef, useState } from "react";
import { useDropzone } from "react-dropzone";
import { FileCheck, Upload, X } from "lucide-react";
import { FileUploadType } from "@/schema/media.schema";

const ChatUpload = ({ onUpload }: { onUpload: (files: FileUploadType[]) => void }) => {
    const [selectedFiles, setSelectedFiles] = useState<FileUploadType[]>([]);
    const selectedFilesRef = useRef<FileUploadType[]>([]);

    const determineFileType = (file: File) => {
        if (file.type.startsWith("image")) return "IMAGE";
        if (file.type.startsWith("video")) return "VIDEO";
        if (file.type.startsWith("audio")) return "AUDIO";
        return "FILE";
    };

    // Xử lý khi người dùng chọn hoặc kéo thả file vào dropzone
    const onDrop = useCallback(
        (acceptedFiles: File[]) => {
            const newFiles = acceptedFiles.map(
                (file: File) =>
                    ({
                        file,
                        preview: URL.createObjectURL(file),
                        type: determineFileType(file),
                    } as FileUploadType)
            );
            selectedFilesRef.current = [...selectedFilesRef.current, ...newFiles];
            setSelectedFiles([...selectedFilesRef.current]);
            onUpload(selectedFilesRef.current);
        },
        [onUpload]
    );

    // Xóa file khỏi danh sách
    const removeFile = (index: number) => {
        selectedFilesRef.current = selectedFilesRef.current.filter((_, i) => i !== index);
        setSelectedFiles([...selectedFilesRef.current]); // Cập nhật state để re-render UI
        onUpload(selectedFilesRef.current); // Cập nhật danh sách file gửi lên server
    };

    const { getRootProps, getInputProps } = useDropzone({
        // accept: { "*/*": [] },
        multiple: true,
        onDrop,
    });

    return (
        <div className="p-4 border-2 border-dashed rounded-lg">
            {/* Khu vực kéo thả */}
            <div
                {...getRootProps()}
                className="flex flex-col items-center justify-center p-5 bg-gray-100 border border-gray-300 rounded-md cursor-pointer hover:bg-gray-200"
            >
                <input {...getInputProps()} />
                <Upload className="w-10 h-10 text-gray-500" />
                <p className="mt-2 text-sm text-gray-600">Drag and drop files here, or click to upload</p>
            </div>

            {/* Hiển thị file đã chọn */}
            <div className="flex justify-center gap-4 flex-wrap mt-6">
                {selectedFilesRef.current.map((file, index) => (
                    <div key={index} className="relative group">
                        {file.type === "IMAGE" && (
                            <img src={file.preview} alt="preview" className="w-full h-32 object-cover rounded-md" />
                        )}
                        {file.type === "VIDEO" && (
                            <video className="w-full h-32 rounded-md" controls>
                                <source src={file.preview} type="video/mp4" />
                            </video>
                        )}
                        {file.type === "AUDIO" && (
                            <audio className="min-w-28 h-16 rounded-md" controls>
                                <source src={file.preview} type="audio/mpeg" />
                            </audio>
                        )}
                        {file.type === "FILE" && (
                            <div className="w-full h-16 px-4 text-xs border flex items-center justify-center rounded-md gap-3">
                                <FileCheck />
                                <p>{file.file.name}</p>
                            </div>
                        )}
                        {/* Nút Xóa */}
                        <button
                            onClick={() => removeFile(index)}
                            className="absolute top-1 right-1 p-1 bg-black/50 rounded-full text-white hidden group-hover:block"
                        >
                            <X size={16} />
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ChatUpload;

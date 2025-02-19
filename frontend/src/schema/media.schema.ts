export type FileUploadType = {
    file: File;
    preview: string;
    type: "IMAGE" | "VIDEO" | "AUDIO" | "FILE";
};

export type FileUploadResponseType = {
    assetId: string;
    publicId: string;
    versionId: string;
    width: number;
    height: number;
    format: string;
    url: string;
    secureUrl: string;
    createdAt: string;
    displayName: string;
    bytes: number;
};

export type AttachmentRequestType = {
    fileId: string;
    fileName: string;
    fileUrl: string;
    format: string;
};

export type AttachmentType = {
    fileId: string;
    fileName: string;
    fileUrl: string;
    format: string;
};

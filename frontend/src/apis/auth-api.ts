import { ErrorResponseType, SuccessResponseType } from "@/schema/types/common";
import { RenewTokensType } from "@/schema/auth.schema";
import { myAxios } from "@/apis/constants"; 

export const callRenewTokenRequest = async (): Promise<RenewTokensType | ErrorResponseType> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken"))
            return {
                statusCode: 401,
                message: "Unauthorized",
                timestamp: new Date().toISOString(),
                path: "/",
                errorCode: "invalid_token",
            };
        const payload = {
            accessToken: localStorage.getItem("accessToken"),
            refreshToken: localStorage.getItem("refreshToken"),
        };
        const response = (await myAxios.post(`/auth/renew-token`, payload)).data as SuccessResponseType<
            RenewTokensType,
            null
        >;
        return response.data;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

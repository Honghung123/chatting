import { ErrorResponseType, PaginationV2Type, SuccessResponseType } from "@/schema/types/common";
import { LoginRequestDTO, LoginResponseDTO, RegisterRequestDTO } from "@/schema/auth.schema";
import { UserListType, UserType } from "@/schema/user.schema";
import { myAxios } from "@/apis/constants";
import { ConversationType } from "@/schema/chat.schema";

export const callRegisterRequest = async (
    payload: RegisterRequestDTO
): Promise<SuccessResponseType<string, null> | ErrorResponseType> => {
    try {
        const response = await myAxios.post(`/auth/register`, payload);
        if (response.status !== 201) return response.data as ErrorResponseType;
        const result = response.data as SuccessResponseType<string, null>;
        return result;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

export const callActivateAccountRequest = async (
    token: string
): Promise<SuccessResponseType<LoginResponseDTO, null> | ErrorResponseType> => {
    try {
        const response = await myAxios.get(`/auth/activate?token=${token}`);
        console.log(response);
        if (response.status !== 200) return response.data as ErrorResponseType;
        const result = response.data as SuccessResponseType<LoginResponseDTO, null>;
        return result;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

export const callLoginRequest = async (
    values: LoginRequestDTO
): Promise<SuccessResponseType<LoginResponseDTO, null> | ErrorResponseType> => {
    try {
        const response = await myAxios.post(`/auth/login`, values);
        console.log(response);
        if (response.status !== 200) return response.data as ErrorResponseType;
        const result = response.data as SuccessResponseType<LoginResponseDTO, null>;
        return result;
    } catch (error: any) {
        return error.response.data as ErrorResponseType;
    }
};

// export const callSendOTPRequest = async (values: SendOTPRequestDTO): Promise<SentOTPResponseDTO | ErrorResponse> => {
//     try {
//         const result = (await axios.patch(`${BASE_API}/user-service/api/users/verify-email`, values)).data;
//         return result as SentOTPResponseDTO;
//     } catch (error: any) {
//         return error.response.data as ErrorResponse;
//     }
// };

// export const callReSendOTPRequest = async (
//     values: ReSendOTPRequestDTO
// ): Promise<ReSentOTPResponseDTO | ErrorResponse> => {
//     try {
//         const result = await axios.post(`${BASE_API}/user-service/api/users/resend-otp`, values);
//         return { data: result.data, statusCode: result.status } as ReSentOTPResponseDTO;
//     } catch (error: any) {
//         return error.response.data as ErrorResponse;
//     }
// };

export const callGettingUserInfoRequest = async (): Promise<UserType | ErrorResponseType> => {
    try {
        console.log("call getting user info");
        if (!localStorage.getItem("accessToken"))
            return {
                statusCode: 401,
                message: "Unauthorized",
                timestamp: new Date().toISOString(),
                path: "/user/info",
                errorCode: "not-authorized",
            };
        const response = await myAxios.get(`/user/info`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        console.log(response);
        if (response.status == 401) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            return response.data as ErrorResponseType;
        } else if (response.status != 200) {
            return response.data as ErrorResponseType;
        }
        const result = response.data as SuccessResponseType<UserType, null>;
        return result.data;
    } catch (error: any) {
        if (error.status == 401) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
        }
        return error.response.data as ErrorResponseType;
    }
};

export const callSuggestFriendInfoRequest = async (): Promise<PaginationV2Type<UserType> | ErrorResponseType> => {
    try {
        console.log("call getting user info");
        if (!localStorage.getItem("accessToken"))
            return {
                statusCode: 401,
                message: "Unauthorized",
                timestamp: new Date().toISOString(),
                path: "/user/info",
                errorCode: "not-authorized",
            };
        const response = await myAxios.get(`/account/suggest-friends`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        console.log(response);
        if (response.status == 401) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            return response.data as ErrorResponseType;
        } else if (response.status != 200) {
            return response.data as ErrorResponseType;
        }
        const result = response.data as SuccessResponseType<PaginationV2Type<UserType>, null>;
        return result.data;
    } catch (error: any) {
        if (error.status == 401) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
        }
        return error.response.data as ErrorResponseType;
    }
};

export const callLogoutRequest = async (): Promise<number> => {
    try {
        if (!localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken")) return 200;
        const payload = {
            accessToken: localStorage.getItem("accessToken"),
            refreshToken: localStorage.getItem("refreshToken"),
        };
        const response = await myAxios.post(`/auth/logout`, payload);
        console.log(response);
        return response.status;
    } catch (error: any) {
        return 400;
    }
};

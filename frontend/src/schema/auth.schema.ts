import z from "zod";

export const RegisterRequestSchema = z
    .object({
        name: z
            .string({
                required_error: "Invalid full name",
            })
            .trim()
            .min(2, {
                message: "Full name is invalid",
            })
            .max(70, {
                message: "Full name must not exceed 70 characters",
            })
            .regex(/\w+\s\w+/, { message: "Full name must be at least first name and last name" }),
        email: z.string().email(),
        password: z
            .string({
                required_error: "Invalid password",
            })
            .min(6, {
                message: "Password must be at least 6 characters",
            })
            .max(30, {
                message: "Password must not exceed 30 characters",
            }),
        repassword: z
            .string({
                required_error: "Invalid password",
            })
            .min(6, {
                message: "Password must be at least 6 characters",
            })
            .max(30, {
                message: "Password must not exceed 30 characters",
            }),
        // isBrand: z.boolean().default(false),
    })
    .strict()
    .superRefine(({ repassword, password }, ctx) => {
        if (repassword !== password) {
            ctx.addIssue({
                code: "custom",
                message: "Password and confirm password does not match",
                path: ["repassword"],
            });
        }
    });

export type RegisterRequestDTO = z.infer<typeof RegisterRequestSchema>;

export type RegisterResponseDTO = {};

export const LoginRequestSchema = z
    .object({
        email: z.string().email(),
        password: z
            .string({
                required_error: "Invalid password",
            })
            .min(6, {
                message: "Password must be at least 6 characters",
            })
            .max(30, {
                message: "Password must not exceed 30 characters",
            }),
    })
    .strict();

export type LoginRequestDTO = z.infer<typeof LoginRequestSchema>;

export type LoginResponseDTO = {
    accessToken: string;
    refreshToken: string;
};

export const UserTypeSchema = z.object({
    id: z.string().uuid(),
    name: z.string(),
    phoneNumber: z.string().min(10).max(10),
    email: z.string(),
    image: z.string().nullable(),
    brand: z.string().nullable(),
    imageId: z.string().nullable(),
    role: z.string(),
    status: z.string(),
});

export type UserType = z.infer<typeof UserTypeSchema>;

export const OTPSchema = z.object({
    otpCode: z
        .string()
        .min(6, {
            message: "Your one-time password must be 6 characters.",
        })
        .max(6, {
            message: "Your one-time password must be 6 characters.",
        }),
    email: z.string().email(),
});

export type SendOTPRequestDTO = z.infer<typeof OTPSchema>;

export const ReSendOTPRequestSchema = z.object({
    email: z.string().email(),
});

export type SentOTPResponseDTO = {
    data: string;
};

export type ReSendOTPRequestDTO = z.infer<typeof ReSendOTPRequestSchema>;
export type ReSentOTPResponseDTO = {
    data: string;
};

export type RenewTokensType = {
    accessToken: string;
    refreshToken: string;
};

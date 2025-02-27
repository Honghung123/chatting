"use client";

import { useEffect, useState } from "react";

import { zodResolver } from "@hookform/resolvers/zod";
import { useToast } from "@/hooks/use-toast";
import { useRouter } from "next/navigation";
import { set, useForm } from "react-hook-form";
import { RegisterRequestDTO, RegisterRequestSchema } from "@/schema/auth.schema";
import { Form, FormField, FormItem, FormLabel, FormControl, FormDescription, FormMessage } from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { handleErrorApi } from "@/lib/utils";
import { PasswordInput } from "@/components/ui/password-input";
import { callRegisterRequest } from "@/apis/user-api";
import { ErrorResponseType, SuccessResponseType } from "@/schema/types/common";

export function RegisterForm() {
    const [loading, setLoading] = useState(false);
    const { toast } = useToast();
    const router = useRouter();
    // 1. Define your form.
    const registerForm = useForm<RegisterRequestDTO>({
        resolver: zodResolver(RegisterRequestSchema),
        defaultValues: {
            name: "Dam Hong Nguyen",
            email: "nr7b8gn67@mozmail.com",
            password: "12345678",
            repassword: "12345678",
        },
    });

    useEffect(() => {
        const accessToken = localStorage.getItem("accessToken");
        const refreshToken = localStorage.getItem("refreshToken");
        if (accessToken && refreshToken) {
            router.push("/");
        } else {
            if (accessToken) localStorage.removeItem("accessToken");
            if (refreshToken) localStorage.removeItem("refreshToken");
        }
    }, []);

    // 2. Define a submit handler.
    async function onSubmit(values: RegisterRequestDTO) {
        if (loading) return;
        setLoading(true);
        const result = await callRegisterRequest(values);
        if (result && !result.hasOwnProperty("errorCode")) {
            const response = result as SuccessResponseType<string, null>;
            toast({
                description: response.message,
                duration: 2000,
                className: "bg-lime-500 text-white",
            });
            setLoading(false);
            sessionStorage.setItem("emailRegistered", response.data);
            router.push(`/check-email`);
        } else {
            const response = result as ErrorResponseType;
            let errorMessage = "An unexpected error has occurred. Please try again later.";
            if (response.statusCode != 500) {
                errorMessage = response.message;
            }
            toast({
                description: errorMessage,
                variant: "destructive",
                duration: 2000,
                className: "bg-red-500 text-white",
            });
        }
        setLoading(false);
    }

    // 3. Return the form.
    return (
        <>
            <Form {...registerForm}>
                <form onSubmit={registerForm.handleSubmit(onSubmit)} noValidate>
                    <FormField
                        control={registerForm.control}
                        name="name"
                        render={({ field }) => (
                            <FormItem className="mt-0">
                                <FormLabel>Full name</FormLabel>
                                <FormControl>
                                    <Input
                                        placeholder="Enter your name"
                                        className="!mt-0 border-gray-300 dark:border-white"
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={registerForm.control}
                        name="email"
                        render={({ field }) => (
                            <FormItem className="mt-3">
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input
                                        placeholder="Enter your email"
                                        className="!mt-0 border-gray-300 dark:border-white"
                                        type="email"
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={registerForm.control}
                        name="password"
                        render={({ field }) => (
                            <FormItem className="mt-3">
                                <FormLabel>Password</FormLabel>
                                <FormControl>
                                    <PasswordInput
                                        placeholder="Enter your password"
                                        className="mt-0 border-gray-300 dark:border-white"
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={registerForm.control}
                        name="repassword"
                        render={({ field }) => (
                            <FormItem className="mt-3">
                                <FormLabel>Confirm password</FormLabel>
                                <FormControl>
                                    <PasswordInput
                                        placeholder="Enter your password again"
                                        className="mt-0 border-gray-300 dark:border-white"
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    {/* <FormField
                        control={registerForm.control}
                        name="isBrand"
                        render={({ field, fieldState }) => (
                            <FormItem className="mt-3 flex items-center justify-start">
                                <FormControl>
                                    <div className="flex items-center mb-4">
                                        <input
                                            id="default-radio-1"
                                            type="checkbox"
                                            checked={field.value}
                                            onChange={(e) => field.onChange(e.target.checked)}
                                            className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                                        />
                                        <label
                                            htmlFor="default-radio-1"
                                            className="ms-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                                        >
                                            Is Brand
                                        </label>
                                    </div>
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    /> */}

                    <Button
                        type="submit"
                        className="!mt-5 block m-auto bg-gradient-to-br from-sky-400 to-fuchsia-400 transition-all duration-1000 hover:bg-gradient-to-br hover:from-sky-500 hover:to-fuchsia-500"
                    >
                        <span className="text-white text-md">Register</span>
                        {loading && <span className="animate-ping">⌛</span>}
                    </Button>
                </form>
            </Form>
        </>
    );
}

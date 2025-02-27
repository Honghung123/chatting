import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    reactStrictMode: true,
    eslint: {
        // Warning: This allows production builds to successfully complete even if
        // your project has ESLint errors.
        ignoreDuringBuilds: true,
    },
    compiler: {
        // removeConsole: {
        //     exclude: ["error"],
        // },
        removeConsole: true,
    },
    ignoreBuildErrors: true,
};

export default nextConfig;

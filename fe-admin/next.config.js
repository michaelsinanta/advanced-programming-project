/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
};

module.exports = nextConfig;

module.exports = {
  async rewrites() {
    return [
      {
        source: "/api/warnet/:path*",
        destination: "http://34.143.176.116/warnet/:path*", // The :path parameter is used here so will not be automatically passed in the query
      },
      {
        source: "/api/bayar/:path*",
        destination: "http://34.142.223.187/api/v1/:path*",
      },
      {
        source: "/api/cafe/:path*",
        destination: "http://34.87.82.213/cafe/:path*",
      },
    ];
  },
};

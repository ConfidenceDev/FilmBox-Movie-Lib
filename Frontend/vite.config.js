import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

const isDocker = process.env.DOCKER === "true";

// Backend URL
const backendUrl = "http://localhost:9090";

export default defineConfig({
  plugins: [react()],
  server: {
    host: "0.0.0.0",
    port: 5173,
    strictPort: true,
    watch: {
      usePolling: isDocker,
    },
    hmr: {
      clientPort: 5173,
    },
    proxy: {
      "/api/v1": {
        target: backendUrl,
        changeOrigin: true,
        secure: false,
      },
    },
  },
});

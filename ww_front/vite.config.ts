import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api/core': {
        target: 'http://localhost:8110',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
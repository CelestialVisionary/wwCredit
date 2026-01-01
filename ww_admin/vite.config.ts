import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5174,
    proxy: {
      '/admin': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  build: {
    target: 'esnext',
    outDir: '../src/main/resources/static',
    emptyOutDir: true,
    sourcemap: true,
    watch: {
      include: 'src/**'
    },
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    },
  },
  define: {
    'process.env': {},
    __VUE_PROD_DEVTOOLS__: true
  },
  server: {
    watch: {
      usePolling: true
    }
  }



})

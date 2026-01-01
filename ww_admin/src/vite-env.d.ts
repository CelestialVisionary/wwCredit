/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare interface ImportMetaEnv {
  readonly BASE_URL: string
}

declare interface ImportMeta {
  readonly env: ImportMetaEnv
}
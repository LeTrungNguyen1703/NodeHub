import { defineConfig } from 'orval';

export default defineConfig({
  default: {
    input: 'http://localhost:8080/v3/api-docs',
    output: {
      httpClient: 'axios',
      target: './src/api.ts',
    },
  },
});

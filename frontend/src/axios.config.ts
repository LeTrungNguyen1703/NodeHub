import axios from 'axios';

axios.defaults.baseURL = '/';

// Token will be added by a request interceptor that we'll set up after OIDC is initialized
// This will be done in main.tsx after AuthProvider is mounted

export const setupAxiosInterceptors = (
  getAccessToken: () => string | undefined,
) => {
  axios.interceptors.request.use(
    async (config) => {
      const token = getAccessToken();
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    },
  );
};

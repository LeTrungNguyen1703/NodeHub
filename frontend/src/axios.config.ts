import axios from 'axios';
import keycloak from './auth/keycloak.ts';

axios.defaults.baseURL = '/';

axios.interceptors.request.use(
  async (config) => {
    if (keycloak.token) {
      // Optional: Check if token is expired and update it
      try {
        await keycloak.updateToken(30);
      } catch (e) {
        console.error('Failed to refresh token', e);
        await keycloak.logout();
      }

      config.headers.Authorization = `Bearer ${keycloak.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

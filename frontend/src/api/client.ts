import axios from 'axios';
import keycloak from '../auth/keycloak';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // Adjust to your backend URL
});

apiClient.interceptors.request.use(
  async (config) => {
    if (keycloak.token) {
      // Optional: Check if token is expired and update it
      try {
          await keycloak.updateToken(30);
      } catch (e) {
          console.error("Failed to refresh token", e);
          await keycloak.logout();
      }
      
      config.headers.Authorization = `Bearer ${keycloak.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default apiClient;

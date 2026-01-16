import { StrictMode, useEffect } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import './axios.config.ts';
import { MantineProvider } from '@mantine/core';
import { AuthProvider, useAuth } from 'react-oidc-context';
import { createBrowserRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import App from './App.tsx';
import { getCurrentUser } from './api.ts';
import { oidcConfig } from './auth/oidc.config.ts';
import { setupAxiosInterceptors } from './axios.config.ts';
import LandingPage from './pages/LandingPage.tsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <LandingPage />,
      },
    ],
  },
]);

// Component to setup axios interceptors and sync user with backend
// eslint-disable-next-line react-refresh/only-export-components
const AuthSetup = () => {
  const auth = useAuth();

  useEffect(() => {
    // Setup axios interceptors with OIDC token
    setupAxiosInterceptors(() => auth.user?.access_token);
  }, [auth.user?.access_token]);

  useEffect(() => {
    // Sync user with backend when authenticated
    if (auth.isAuthenticated && auth.user) {
      getCurrentUser()
        .then(() => console.log('User synced with backend'))
        .catch((error) =>
          console.error('Failed to sync user with backend', error),
        );
    }
  }, [auth.isAuthenticated, auth.user]);

  return null;
};

// biome-ignore lint/style/noNonNullAssertion: because we are sure that 'root' exists
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <AuthProvider {...oidcConfig}>
      <MantineProvider defaultColorScheme="light">
        <AuthSetup />
        <RouterProvider router={router} />
      </MantineProvider>
    </AuthProvider>
  </StrictMode>,
);

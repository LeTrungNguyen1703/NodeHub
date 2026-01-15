import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import './axios.config.ts';
import { MantineProvider } from '@mantine/core';
import { createBrowserRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import App from './App.tsx';
import { AuthProvider } from './auth/AuthContext.tsx';
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

// biome-ignore lint/style/noNonNullAssertion: because we are sure that 'root' exists
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <MantineProvider defaultColorScheme="light">
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
    </MantineProvider>
  </StrictMode>,
);

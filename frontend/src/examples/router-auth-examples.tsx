/**
 * Example: React Router v7 Configuration with OIDC Authentication
 *
 * This file demonstrates how to set up routes with authentication
 * using react-oidc-context and React Router v7 patterns.
 */

import { createBrowserRouter } from 'react-router';
import App from '../App';
import { AuthMiddleware, GuestMiddleware } from '../middlewares/AuthMiddleware';
import LandingPage from '../pages/LandingPage';

/**
 * Example 1: Basic router with public and protected routes
 */
export const basicRouterExample = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <LandingPage />,
      },
      // Protected routes using AuthMiddleware
      {
        element: <AuthMiddleware />,
        children: [
          {
            path: '/dashboard',
            element: <div>Dashboard - Protected</div>,
          },
          {
            path: '/profile',
            element: <div>Profile - Protected</div>,
          },
        ],
      },
    ],
  },
]);

/**
 * Example 2: Router with guest routes (redirect authenticated users)
 */
export const routerWithGuestPages = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      // Public routes
      {
        index: true,
        element: <LandingPage />,
      },
      // Guest-only routes (redirect if authenticated)
      {
        element: <GuestMiddleware redirectTo="/dashboard" />,
        children: [
          {
            path: '/login',
            element: <div>Login Page</div>,
          },
          {
            path: '/register',
            element: <div>Register Page</div>,
          },
        ],
      },
      // Protected routes
      {
        element: <AuthMiddleware />,
        children: [
          {
            path: '/dashboard',
            element: <div>Dashboard</div>,
          },
        ],
      },
    ],
  },
]);

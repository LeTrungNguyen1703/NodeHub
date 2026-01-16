import { useAuth } from 'react-oidc-context';
import { Navigate, Outlet } from 'react-router';

/**
 * Middleware component for protected routes using React Router v7
 *
 * Usage:
 * const router = createBrowserRouter([
 *   {
 *     element: <AuthMiddleware />,
 *     children: [
 *       { path: '/dashboard', element: <Dashboard /> },
 *       { path: '/profile', element: <Profile /> },
 *     ],
 *   },
 * ]);
 */
export const AuthMiddleware = () => {
  const auth = useAuth();

  // Show loading state while authentication is being checked
  if (auth.isLoading) {
    return (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
        }}
      >
        <div>Loading authentication...</div>
      </div>
    );
  }

  // Handle authentication errors
  if (auth.error) {
    console.error('Authentication error:', auth.error);
    return (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
          flexDirection: 'column',
          gap: '1rem',
        }}
      >
        <div>Authentication error occurred</div>
        <button
          type="button"
          onClick={() => void auth.signinRedirect()}
          style={{
            padding: '0.5rem 1rem',
            background: '#007bff',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
          }}
        >
          Try Again
        </button>
      </div>
    );
  }

  // Redirect to login if not authenticated
  if (!auth.isAuthenticated) {
    // Store the attempted URL for redirect after login
    const currentUrl = window.location.href;
    void auth.signinRedirect({ redirect_uri: currentUrl });

    return (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
        }}
      >
        <div>Redirecting to login...</div>
      </div>
    );
  }

  // User is authenticated, render child routes
  return <Outlet />;
};

/**
 * Optional: Middleware that redirects authenticated users away from auth pages
 *
 * Usage:
 * const router = createBrowserRouter([
 *   {
 *     element: <GuestMiddleware />,
 *     children: [
 *       { path: '/login', element: <LoginPage /> },
 *       { path: '/register', element: <RegisterPage /> },
 *     ],
 *   },
 * ]);
 */
export const GuestMiddleware = ({
  redirectTo = '/',
}: {
  redirectTo?: string;
}) => {
  const auth = useAuth();

  if (auth.isLoading) {
    return (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
        }}
      >
        <div>Loading...</div>
      </div>
    );
  }

  // If user is authenticated, redirect them away
  if (auth.isAuthenticated) {
    return <Navigate to={redirectTo} replace />;
  }

  // User is not authenticated, show the page
  return <Outlet />;
};

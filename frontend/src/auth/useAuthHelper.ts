import { useMemo } from 'react';
import { useAuth as useOidcAuth } from 'react-oidc-context';
import type { UserProfile } from './auth.utils';
import { getUserProfile, login, logout, register } from './auth.utils';

/**
 * Custom hook that provides a simplified auth API
 * This wraps react-oidc-context with convenient utilities
 *
 * Usage:
 * ```typescript
 * const { isAuthenticated, userProfile, login, logout, isLoading } = useAuthHelper();
 * ```
 */
export const useAuthHelper = () => {
  const auth = useOidcAuth();

  const userProfile = useMemo(() => getUserProfile(auth.user), [auth.user]);

  const isAuthenticated = auth.isAuthenticated && !auth.isLoading;

  return {
    // Auth state
    isAuthenticated,
    isLoading: auth.isLoading,
    error: auth.error,

    // User info
    user: auth.user,
    userProfile,

    // Auth actions
    login: () => login(auth),
    register: () => register(auth),
    logout: () => logout(auth),

    // Token access (if needed)
    accessToken: auth.user?.access_token,
    idToken: auth.user?.id_token,

    // Raw auth context (for advanced usage)
    auth,
  };
};

/**
 * Hook to check if user has specific roles/permissions
 *
 * Usage:
 * ```typescript
 * const { hasRole, hasAnyRole, hasAllRoles } = useAuthRoles();
 * const canEdit = hasRole('editor');
 * ```
 */
export const useAuthRoles = () => {
  const auth = useOidcAuth();

  const roles = useMemo(() => {
    const profile = auth.user?.profile as UserProfile & {
      realm_access?: { roles?: string[] };
      resource_access?: Record<string, { roles?: string[] }>;
    };

    // Get realm roles
    // Get client roles (if needed) by adding:
    // const clientRoles = profile?.resource_access?.['client-id']?.roles || [];

    return profile?.realm_access?.roles || [];
  }, [auth.user?.profile]);

  const hasRole = (role: string): boolean => {
    return roles.includes(role);
  };

  const hasAnyRole = (...rolesToCheck: string[]): boolean => {
    return rolesToCheck.some((role) => roles.includes(role));
  };

  const hasAllRoles = (...rolesToCheck: string[]): boolean => {
    return rolesToCheck.every((role) => roles.includes(role));
  };

  return {
    roles,
    hasRole,
    hasAnyRole,
    hasAllRoles,
  };
};

/**
 * Hook to get authentication status with detailed information
 *
 * Usage:
 * ```typescript
 * const { status, message } = useAuthStatus();
 * // status: 'loading' | 'authenticated' | 'unauthenticated' | 'error'
 * ```
 */
export const useAuthStatus = () => {
  const auth = useOidcAuth();

  const status = useMemo(() => {
    if (auth.isLoading) return 'loading';
    if (auth.error) return 'error';
    if (auth.isAuthenticated) return 'authenticated';
    return 'unauthenticated';
  }, [auth.isLoading, auth.error, auth.isAuthenticated]);

  const message = useMemo(() => {
    switch (status) {
      case 'loading':
        return 'Checking authentication...';
      case 'error':
        return auth.error?.message || 'Authentication error';
      case 'authenticated':
        return 'Authenticated';
      case 'unauthenticated':
        return 'Not authenticated';
      default:
        return '';
    }
  }, [status, auth.error]);

  return {
    status,
    message,
    isLoading: status === 'loading',
    isError: status === 'error',
    isAuthenticated: status === 'authenticated',
    error: auth.error,
  };
};

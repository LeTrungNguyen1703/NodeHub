import type { User } from 'oidc-client-ts';
import type { AuthContextProps } from 'react-oidc-context';
import { redirect } from 'react-router';

export interface UserProfile {
  sub?: string;
  preferred_username?: string;
  email?: string;
  email_verified?: boolean;
  name?: string;
  given_name?: string;
  family_name?: string;
  picture?: string;
  [key: string]: unknown;
}

/**
 * Loader that requires authentication
 * Use this in routes that need the user to be logged in
 */
export const requireAuth = (auth: AuthContextProps) => {
  if (!auth.isAuthenticated && !auth.isLoading) {
    // User is not authenticated, redirect to login
    void auth.signinRedirect({
      state: { returnUrl: window.location.pathname + window.location.search },
    });
    throw redirect('/');
  }

  if (auth.isLoading) {
    // Still loading, return null to show loading state
    return null;
  }

  return {
    user: auth.user,
    userProfile: auth.user?.profile as UserProfile | undefined,
  };
};

/**
 * Get user profile from OIDC user object
 */
export const getUserProfile = (
  user: User | null | undefined,
): UserProfile | undefined => {
  if (!user?.profile) return undefined;
  return user.profile as UserProfile;
};

/**
 * Check if user is authenticated
 */
export const isAuthenticated = (auth: AuthContextProps): boolean => {
  return auth.isAuthenticated && !auth.isLoading;
};

/**
 * Login function
 */
export const login = (auth: AuthContextProps) => {
  void auth.signinRedirect({
    state: { returnUrl: window.location.pathname + window.location.search },
  });
};

/**
 * Register function (redirect to Keycloak registration)
 */
export const register = (auth: AuthContextProps) => {
  void auth.signinRedirect({
    state: { returnUrl: window.location.pathname + window.location.search },
    extraQueryParams: { kc_action: 'REGISTER' },
  });
};

/**
 * Logout function
 */
export const logout = (auth: AuthContextProps) => {
  void auth.signoutRedirect({
    post_logout_redirect_uri: window.location.origin,
  });
};

import type { KeycloakProfile } from 'keycloak-js';
import type React from 'react';
import type { ReactNode } from 'react';
import { createContext, useContext, useEffect, useState } from 'react';
import { getCurrentUser } from '../api.ts';
import keycloak from './keycloak';

interface ExtendedKeycloakProfile extends KeycloakProfile {
  avatarUrl?: string;
}

interface AuthContextType {
  isAuthenticated: boolean;
  token: string | undefined;
  login: () => void;
  register: () => void;
  logout: () => void;
  userProfile: ExtendedKeycloakProfile | undefined;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const syncUserWithBackend = async () => {
  try {
    await getCurrentUser();
    console.log('User synced with backend');
  } catch (error) {
    console.error('Failed to sync user with backend', error);
  }
};

export const AuthProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState<string | undefined>(undefined);
  const [userProfile, setUserProfile] = useState<
    ExtendedKeycloakProfile | undefined
  >(undefined);
  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    const initKeycloak = async () => {
      try {
        const authenticated = await keycloak.init({
          onLoad: 'check-sso',
          checkLoginIframe: false,
          pkceMethod: 'S256',
          silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`,
          // This ensures redirects happen in same window, not new tab
          flow: 'standard',
        });

        console.log('Keycloak initialized, authenticated:', authenticated);

        setIsAuthenticated(authenticated);
        if (authenticated) {
          setToken(keycloak.token);
          const profile = await keycloak.loadUserProfile();

          // Extract avatar from token if available (standard claim 'picture' or custom claim)
          const avatarUrl = keycloak.tokenParsed?.picture;

          setUserProfile({ ...profile, avatarUrl });

          await syncUserWithBackend();
        }
      } catch (error) {
        console.error('Failed to initialize Keycloak', error);
      } finally {
        setIsInitialized(true);
      }
    };

    void initKeycloak();

    // Token refresh logic
    keycloak.onTokenExpired = () => {
      keycloak
        .updateToken(30)
        .then((refreshed) => {
          if (refreshed) {
            setToken(keycloak.token);
            // Update avatar if token refreshed (in case it changed)
            const avatarUrl = keycloak.tokenParsed?.picture;
            setUserProfile((prev) =>
              prev ? { ...prev, avatarUrl } : undefined,
            );
          } else {
            console.warn(
              'Token not refreshed, valid for ' +
                Math.round(
                  keycloak.tokenParsed!.exp! +
                    keycloak.timeSkew! -
                    Date.now() / 1000,
                ) +
                ' seconds',
            );
          }
        })
        .catch(() => {
          console.error('Failed to refresh token');
          keycloak.logout();
        });
    };
  }, []);

  const login = () => {
    // This will redirect in the same tab, not open a new one
    void keycloak.login({
      redirectUri: window.location.href, // Use current URL to return to same page
    });
  };

  const register = () => {
    // This will redirect in the same tab, not open a new one
    void keycloak.register({
      redirectUri: window.location.href, // Use current URL to return to same page
    });
  };

  const logout = () => {
    keycloak.logout({
      redirectUri: window.location.origin,
    });
  };

  if (!isInitialized) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading authentication...
      </div>
    );
  }

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, token, login, register, logout, userProfile }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

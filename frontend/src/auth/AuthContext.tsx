import React, { createContext, useContext, useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import type { KeycloakProfile } from 'keycloak-js';
import keycloak from './keycloak';
import { getMyProfile } from '../api/userService';

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

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState<string | undefined>(undefined);
  const [userProfile, setUserProfile] = useState<ExtendedKeycloakProfile | undefined>(undefined);
  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    const initKeycloak = async () => {
      try {
        const authenticated = await keycloak.init({
          onLoad: 'check-sso',
          checkLoginIframe: false,
          pkceMethod: 'S256',
          silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html'
        });

        console.log('Keycloak initialized, authenticated:', authenticated);

        setIsAuthenticated(authenticated);
        if (authenticated) {
          setToken(keycloak.token);
          const profile = await keycloak.loadUserProfile();
          
          // Extract avatar from token if available (standard claim 'picture' or custom claim)
          const avatarUrl = (keycloak.tokenParsed as any)?.picture;
          
          setUserProfile({ ...profile, avatarUrl });

          await syncUserWithBackend();
        }
      } catch (error) {
        console.error('Failed to initialize Keycloak', error);
      } finally {
        setIsInitialized(true);
      }
    };

    initKeycloak();

    // Token refresh logic
    keycloak.onTokenExpired = () => {
      keycloak.updateToken(30).then((refreshed) => {
        if (refreshed) {
          setToken(keycloak.token);
          // Update avatar if token refreshed (in case it changed)
          const avatarUrl = (keycloak.tokenParsed as any)?.picture;
          setUserProfile(prev => prev ? { ...prev, avatarUrl } : undefined);
        } else {
          console.warn('Token not refreshed, valid for ' + Math.round(keycloak.tokenParsed!.exp! + keycloak.timeSkew! - new Date().getTime() / 1000) + ' seconds');
        }
      }).catch(() => {
        console.error('Failed to refresh token');
        keycloak.logout();
      });
    };

  }, []);

  const login = async () => {
    await  keycloak.login({
        redirectUri: window.location.origin
    });
    await syncUserWithBackend();
  };

  const register = async () => {
    await keycloak.register({
        redirectUri: window.location.origin
    });

    await syncUserWithBackend();
  };

  const logout = () => {
    keycloak.logout({
        redirectUri: window.location.origin
    });
  };

  const syncUserWithBackend = async () => {
    try {
      await getMyProfile();
      console.log('User synced with backend');
    } catch (error) {
      console.error('Failed to sync user with backend', error);
    }
  }

  if (!isInitialized) {
    return <div className="min-h-screen flex items-center justify-center">Loading authentication...</div>;
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, token, login, register, logout, userProfile }}>
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

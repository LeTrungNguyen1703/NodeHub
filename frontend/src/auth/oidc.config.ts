import type { UserManagerSettings } from 'oidc-client-ts';

export const oidcConfig: UserManagerSettings = {
  authority: 'http://localhost:8180/realms/auction-system',
  client_id: 'auction-fe',
  redirect_uri: `${window.location.origin}/`,
  silent_redirect_uri: `${window.location.origin}/silent-check-sso.html`,
  post_logout_redirect_uri: window.location.origin,
  response_type: 'code',
  scope: 'openid profile email',
  automaticSilentRenew: true,
  loadUserInfo: true,
  // Disable popup mode - use redirects in same window
  popupWindowFeatures: undefined,
  popupWindowTarget: undefined,
};

declare module 'keycloak-js' {
  interface KeycloakTokenParsed {
    picture?: string;
    [key: string]: any;
  }
}

export {};

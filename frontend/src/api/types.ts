// --- Common Responses ---
export interface GenericApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: any;
  timestamp: string;
  statusCode: number;
}

// --- User ---
export interface UserProfileView {
  keycloakId: string;
  email: string;
  username: string;
  firstName?: string;
  lastName?: string;
  fullName: string;
  avatarUrl: string;
  preferredLanguage: string;
}

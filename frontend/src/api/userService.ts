import type {GenericApiResponse, UserProfileView} from "./types.ts";
import apiClient from "./client.ts";

export const getMyProfile = async (): Promise<UserProfileView> => {
    const response = await apiClient.get<GenericApiResponse<UserProfileView>>('/users/me');
    return response.data.data;
};

export const updateUserAvatar = async (avatarUrl: string): Promise<void> => {
    await apiClient.patch('/users/me/avatar', { avatarUrl });
};
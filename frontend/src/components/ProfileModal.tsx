import type React from 'react';
import { useAuth } from 'react-oidc-context';
import { getUserProfile } from '@/auth/auth.utils';

interface ProfileModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const ProfileModal: React.FC<ProfileModalProps> = ({ isOpen, onClose }) => {
  const auth = useAuth();
  const userProfile = getUserProfile(auth.user);

  if (!isOpen) return null;

  return (
    // biome-ignore lint/a11y/useKeyWithClickEvents: Modal overlay click to close is standard UX
    // biome-ignore lint/a11y/noStaticElementInteractions: Modal overlay click to close is standard UX
    <div className="modal-overlay" onClick={onClose}>
      {/* biome-ignore lint/a11y/useKeyWithClickEvents: Preventing propagation for modal content */}
      {/* biome-ignore lint/a11y/noStaticElementInteractions: Preventing propagation for modal content */}
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>User Profile</h2>
          <button type="button" className="close-button" onClick={onClose}>
            ×
          </button>
        </div>

        <div className="modal-body">
          {userProfile?.picture && (
            <div className="profile-avatar">
              <img src={userProfile.picture} alt="User avatar" />
            </div>
          )}

          <div className="profile-info">
            <div className="profile-field">
              <span className="field-label">Username:</span>
              <span>{userProfile?.preferred_username || 'N/A'}</span>
            </div>

            <div className="profile-field">
              <span className="field-label">Email:</span>
              <span>{userProfile?.email || 'N/A'}</span>
            </div>

            <div className="profile-field">
              <span className="field-label">First Name:</span>
              <span>{userProfile?.given_name || 'N/A'}</span>
            </div>

            <div className="profile-field">
              <span className="field-label">Last Name:</span>
              <span>{userProfile?.family_name || 'N/A'}</span>
            </div>
          </div>
        </div>

        <div className="modal-footer">
          <button type="button" className="btn btn-secondary" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProfileModal;

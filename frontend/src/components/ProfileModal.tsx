import React from 'react';
import { useAuth } from '../auth/AuthContext';

interface ProfileModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const ProfileModal: React.FC<ProfileModalProps> = ({ isOpen, onClose }) => {
  const { userProfile } = useAuth();

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>User Profile</h2>
          <button className="close-button" onClick={onClose}>
            ×
          </button>
        </div>

        <div className="modal-body">
          {userProfile?.avatarUrl && (
            <div className="profile-avatar">
              <img src={userProfile.avatarUrl} alt="User avatar" />
            </div>
          )}

          <div className="profile-info">
            <div className="profile-field">
              <label>Username:</label>
              <span>{userProfile?.username || 'N/A'}</span>
            </div>

            <div className="profile-field">
              <label>Email:</label>
              <span>{userProfile?.email || 'N/A'}</span>
            </div>

            <div className="profile-field">
              <label>First Name:</label>
              <span>{userProfile?.firstName || 'N/A'}</span>
            </div>

            <div className="profile-field">
              <label>Last Name:</label>
              <span>{userProfile?.lastName || 'N/A'}</span>
            </div>
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProfileModal;


import React, { useState } from 'react';
import { useAuth } from '../auth/AuthContext';
import ProfileModal from '../components/ProfileModal';
import './LandingPage.css';

const LandingPage: React.FC = () => {
  const { isAuthenticated, login, register, logout, userProfile } = useAuth();
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);

  return (
    <div className="landing-page">
      {/* Header/Navigation */}
      <header className="header">
        <div className="container">
          <div className="logo">
            <h1>Auction System</h1>
          </div>

          <nav className="nav">
            {isAuthenticated ? (
              <div className="user-menu">
                <button
                  className="btn btn-profile"
                  onClick={() => setIsProfileModalOpen(true)}
                >
                  <span className="user-icon">👤</span>
                  <span>{userProfile?.firstName || userProfile?.username || 'Profile'}</span>
                </button>
                <button className="btn btn-secondary" onClick={logout}>
                  Logout
                </button>
              </div>
            ) : (
              <div className="auth-buttons">
                <button className="btn btn-secondary" onClick={login}>
                  Login
                </button>
                <button className="btn btn-primary" onClick={register}>
                  Register
                </button>
              </div>
            )}
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <main className="main-content">
        <div className="hero-section">
          <div className="container">
            <h2 className="hero-title">
              Welcome to Auction System
            </h2>
            <p className="hero-subtitle">
              {isAuthenticated
                ? `Hello, ${userProfile?.firstName || userProfile?.username}! Ready to start bidding?`
                : 'Join us today and start bidding on amazing items!'
              }
            </p>

            {!isAuthenticated && (
              <div className="hero-actions">
                <button className="btn btn-primary btn-large" onClick={register}>
                  Get Started
                </button>
                <button className="btn btn-outline btn-large" onClick={login}>
                  Sign In
                </button>
              </div>
            )}

            {isAuthenticated && (
              <div className="hero-actions">
                <button className="btn btn-primary btn-large">
                  Browse Auctions
                </button>
                <button
                  className="btn btn-outline btn-large"
                  onClick={() => setIsProfileModalOpen(true)}
                >
                  View Profile
                </button>
              </div>
            )}
          </div>
        </div>

        {/* Features Section */}
        <section className="features-section">
          <div className="container">
            <div className="features-grid">
              <div className="feature-card">
                <div className="feature-icon">🔨</div>
                <h3>Live Bidding</h3>
                <p>Participate in real-time auctions with instant updates</p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">🔒</div>
                <h3>Secure Platform</h3>
                <p>Protected by Keycloak authentication and security</p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">💎</div>
                <h3>Quality Items</h3>
                <p>Browse through curated collections of premium items</p>
              </div>
            </div>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="footer">
        <div className="container">
          <p>&copy; 2026 Auction System. All rights reserved.</p>
        </div>
      </footer>

      {/* Profile Modal */}
      <ProfileModal
        isOpen={isProfileModalOpen}
        onClose={() => setIsProfileModalOpen(false)}
      />
    </div>
  );
};

export default LandingPage;


import type React from 'react';
import { useState } from 'react';
import { useAuth } from 'react-oidc-context';
import { getUserProfile, login, logout, register } from '@/auth/auth.utils';
import ProfileModal from '@/components/ProfileModal';
import './LandingPage.css';

const LandingPage: React.FC = () => {
  const auth = useAuth();
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);

  const userProfile = getUserProfile(auth.user);
  const isAuthenticated = auth.isAuthenticated && !auth.isLoading;

  // Show loading state
  if (auth.isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading authentication...
      </div>
    );
  }

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
                  type="button"
                  className="btn btn-profile"
                  onClick={() => setIsProfileModalOpen(true)}
                >
                  <span className="user-icon">👤</span>
                  <span>
                    {userProfile?.given_name ||
                      userProfile?.preferred_username ||
                      'Profile'}
                  </span>
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => logout(auth)}
                >
                  Logout
                </button>
              </div>
            ) : (
              <div className="auth-buttons">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => login(auth)}
                >
                  Login
                </button>
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={() => register(auth)}
                >
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
            <h2 className="hero-title">Welcome to Auction System</h2>
            <p className="hero-subtitle">
              {isAuthenticated
                ? `Hello, ${userProfile?.given_name || userProfile?.preferred_username}! Ready to start bidding?`
                : 'Join us today and start bidding on amazing items!'}
            </p>

            {!isAuthenticated && (
              <div className="hero-actions">
                <button
                  type="button"
                  className="btn btn-primary btn-large"
                  onClick={() => register(auth)}
                >
                  Get Started
                </button>
                <button
                  type="button"
                  className="btn btn-outline btn-large"
                  onClick={() => login(auth)}
                >
                  Sign In
                </button>
              </div>
            )}

            {isAuthenticated && (
              <div className="hero-actions">
                <button type="button" className="btn btn-primary btn-large">
                  Browse Auctions
                </button>
                <button
                  type="button"
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

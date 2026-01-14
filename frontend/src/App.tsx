import './App.css';
import { AuthProvider } from './auth/AuthContext';
import LandingPage from "./pages/LandingPage.tsx";

function App() {

  return (
    <AuthProvider>
      <LandingPage/>
    </AuthProvider>
  );
}

export default App;

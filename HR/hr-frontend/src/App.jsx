import { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import EditUserPage from "./pages/EditUserPage";
import EditCandidatePage from "./pages/EditCandidatePage";

import CandidateDashboard from "./pages/CandidateDashboard";
import CandidatePage from "./pages/CandidatePage";
import InterviewAdminPage from "./pages/InterviewAdminPage";
import DashboardPage from "./pages/DashboardPage";
import MyInterviewsPage from "./pages/MyInterviewsPage";
import AssignedInterviewsPage from "./pages/AssignedInterviewsPage";
import ProfilePage from "./pages/ProfilePage";

import Layout from "./components/Layout";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || "");

  const handleLogin = (newToken) => {
    localStorage.setItem("token", newToken);
    setToken(newToken);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setToken("");
  };

  return (
    <Router>
      <Routes>
        {/* Public routes */}
        <Route
          path="/login"
          element={
            token ? <Navigate to="/dashboard" replace /> : <LoginPage onLogin={handleLogin} />
          }
        />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/edit-user" element={<EditUserPage />} />
        <Route path="/edit-candidate" element={<EditCandidatePage />} />

        {/* Protected routes with Navbar via Layout */}
        <Route element={<Layout token={token} onLogout={handleLogout} />}>
          <Route
            path="/candidate-dashboard"
            element={
              token ? <CandidateDashboard token={token} onLogout={handleLogout} /> : <Navigate to="/login" replace />
            }
          />
          <Route
            path="/candidates"
            element={
              token ? <CandidatePage token={token} onLogout={handleLogout} /> : <Navigate to="/login" replace />
            }
          />
          <Route
            path="/admin/interviews"
            element={
              token ? <InterviewAdminPage token={token} /> : <Navigate to="/login" replace />
            }
          />
          <Route
            path="/dashboard"
            element={
              token ? <DashboardPage /> : <Navigate to="/login" replace />
            }
          />
          <Route
            path="/my-interviews"
            element={
              token ? <MyInterviewsPage /> : <Navigate to="/login" replace />
            }
          />
          <Route
            path="/assigned-interviews"
            element={
              token ? <AssignedInterviewsPage /> : <Navigate to="/login" replace />
            }
          />
          <Route
            path="/profile"
            element={
              token ? <ProfilePage /> : <Navigate to="/login" replace />
            }
          />
        </Route>

        {/* Fallback route */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;

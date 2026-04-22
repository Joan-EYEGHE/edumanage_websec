import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "../pages/LoginPage";
import DashboardPage from "../pages/DashboardPage";
import UsersPage from "../pages/UsersPage";
import FormationsPage from "../pages/FormationsPage";
import InscriptionsPage from "../pages/InscriptionsPage";
import PaiementsPage from "../pages/PaiementsPage";
import AuditLogsPage from "../pages/AuditLogsPage";
import ProtectedRoute from "../components/common/ProtectedRoute";

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <DashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/users"
          element={
            <ProtectedRoute>
              <UsersPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/formations"
          element={
            <ProtectedRoute>
              <FormationsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/inscriptions"
          element={
            <ProtectedRoute>
              <InscriptionsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/paiements"
          element={
            <ProtectedRoute>
              <PaiementsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/audit-logs"
          element={
            <ProtectedRoute>
              <AuditLogsPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;
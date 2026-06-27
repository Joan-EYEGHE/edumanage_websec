import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "../pages/LoginPage";
import DashboardPage from "../pages/DashboardPage";
import UsersPage from "../pages/UsersPage";
import FormationsPage from "../pages/FormationsPage";
import InscriptionsPage from "../pages/InscriptionsPage";
import PaiementsPage from "../pages/PaiementsPage";
import AuditLogsPage from "../pages/AuditLogsPage";
import ForbiddenPage from "../pages/ForbiddenPage";
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
            <ProtectedRoute allowedRoles={["ADMINISTRATEUR", "GESTIONNAIRE", "FORMATEUR", "APPRENANT"]}>
              <DashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/users"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRATEUR"]}>
              <UsersPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/formations"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRATEUR", "GESTIONNAIRE", "FORMATEUR", "APPRENANT"]}>
              <FormationsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/inscriptions"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRATEUR", "GESTIONNAIRE", "FORMATEUR"]}>
              <InscriptionsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/paiements"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRATEUR", "GESTIONNAIRE"]}>
              <PaiementsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/audit-logs"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRATEUR"]}>
              <AuditLogsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/forbidden"
          element={
            <ProtectedRoute>
              <ForbiddenPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;
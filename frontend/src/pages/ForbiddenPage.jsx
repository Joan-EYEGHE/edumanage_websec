import { useNavigate } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";

function ForbiddenPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <PageHeader
        title="Accès refusé"
        subtitle="Vous n'avez pas les permissions nécessaires pour accéder à cette page."
      />
      <div style={styles.container}>
        <div style={styles.card}>
          <div style={styles.icon}>🔒</div>
          <p style={styles.message}>
            Votre rôle ne vous autorise pas à consulter cette section.
            Contactez un administrateur si vous pensez qu'il s'agit d'une erreur.
          </p>
          <button style={styles.button} onClick={() => navigate("/dashboard")}>
            Retour au tableau de bord
          </button>
        </div>
      </div>
    </AppLayout>
  );
}

const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    padding: "48px 24px",
  },
  card: {
    background: "#ffffff",
    borderRadius: "16px",
    padding: "48px 40px",
    textAlign: "center",
    maxWidth: "480px",
    boxShadow: "0 2px 16px rgba(0,0,0,0.08)",
  },
  icon: {
    fontSize: "56px",
    marginBottom: "20px",
  },
  message: {
    color: "#64748b",
    fontSize: "15px",
    lineHeight: "1.6",
    marginBottom: "32px",
  },
  button: {
    background: "#00798f",
    color: "#ffffff",
    border: "none",
    borderRadius: "8px",
    padding: "12px 28px",
    fontSize: "14px",
    fontWeight: "600",
    cursor: "pointer",
  },
};

export default ForbiddenPage;

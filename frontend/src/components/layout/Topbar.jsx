import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function Topbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <header style={styles.topbar}>
      <div>
        <h2 style={styles.title}>Tableau de bord</h2>
      </div>

      <div style={styles.userBox}>
        <div style={styles.userInfo}>
          <strong>
            {user?.prenom} {user?.nom}
          </strong>
          <span style={styles.role}>
            {user?.roles?.join(", ") || "Utilisateur"}
          </span>
        </div>

        <button onClick={handleLogout} style={styles.button}>
          Déconnexion
        </button>
      </div>
    </header>
  );
}

const styles = {
  topbar: {
    backgroundColor: "#fff",
    padding: "1rem 1.5rem",
    borderBottom: "1px solid #e5e7eb",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  title: {
    fontSize: "1.25rem",
    color: "#111827",
  },
  userBox: {
    display: "flex",
    alignItems: "center",
    gap: "1rem",
  },
  userInfo: {
    display: "flex",
    flexDirection: "column",
    alignItems: "flex-end",
  },
  role: {
    fontSize: "0.85rem",
    color: "#6b7280",
  },
  button: {
    backgroundColor: "#dc2626",
    color: "#fff",
    border: "none",
    padding: "0.7rem 1rem",
    borderRadius: "8px",
  },
};

export default Topbar;
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function Topbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const initials = `${user?.prenom?.[0] || ""}${user?.nom?.[0] || ""}` || "U";

  return (
    <header className="topbar">
      <div>
        <p className="topbar-kicker">Application sécurisée</p>
        <h2 className="topbar-title">Tableau de bord</h2>
      </div>

      <div className="topbar-user-area">
        <div className="user-avatar">{initials.toUpperCase()}</div>

        <div className="user-info">
          <strong>
            {user?.prenom} {user?.nom}
          </strong>
          <span>{user?.roles?.join(", ") || "Utilisateur"}</span>
        </div>

        <button onClick={handleLogout} className="logout-button">
          Déconnexion
        </button>
      </div>
    </header>
  );
}

export default Topbar;
import { NavLink } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { hasAnyRole } from "../../utils/roles";

function EduManageLogo() {
  return (
    <svg width="230" height="70" viewBox="0 0 220 64" xmlns="http://www.w3.org/2000/svg">
      <rect x="6" y="8" width="48" height="48" rx="14" fill="#f5feff" opacity="0.16" />
      <path d="M18 22 L32 15 L46 22 V32 C46 42 39 48 32 51 C25 48 18 42 18 32V22Z" fill="#f5feff" />
      <path d="M27 24 H39" stroke="#00798f" strokeWidth="3" strokeLinecap="round" />
      <path d="M27 32 H37" stroke="#00798f" strokeWidth="3" strokeLinecap="round" />
      <path d="M27 40 H39" stroke="#00798f" strokeWidth="3" strokeLinecap="round" />
      <text x="66" y="35" fontFamily="Inter, Arial, sans-serif" fontSize="24" fontWeight="1000" fill="#f5feff">
        EduManage
      </text>
      <text x="68" y="58" fontFamily="Inter, Arial, sans-serif" fontSize="15" fontWeight="600" fill="#f5feff" opacity="0.78" letterSpacing="2">
        SECURE LMS
      </text>
    </svg>
  );
}

function Sidebar() {
  const { user } = useAuth();

  const menuItems = [
  { label: "Dashboard", path: "/dashboard", icon: "📊", visible: true },
  {
    label: "Utilisateurs",
    path: "/users",
    icon: "👤",
    visible: hasAnyRole(user, ["ADMINISTRATEUR", "GESTIONNAIRE"]),
  },
  {
    label: "Formations",
    path: "/formations",
    icon: "🎓",
    visible: hasAnyRole(user, ["ADMINISTRATEUR", "GESTIONNAIRE", "FORMATEUR"]),
  },
  {
    label: "Inscriptions",
    path: "/inscriptions",
    icon: "🗂️",
    visible: hasAnyRole(user, ["ADMINISTRATEUR", "GESTIONNAIRE", "FORMATEUR"]),
  },
  {
    label: "Paiements",
    path: "/paiements",
    icon: "💳",
    visible: hasAnyRole(user, ["ADMINISTRATEUR", "GESTIONNAIRE"]),
  },
  {
    label: "Audit Logs",
    path: "/audit-logs",
    icon: "🔐",
    visible: hasAnyRole(user, ["ADMINISTRATEUR"]),
  },
];

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <EduManageLogo />
      </div>

      <div className="sidebar-section-label">Navigation</div>

      <nav className="sidebar-nav">
        {menuItems
          .filter((item) => item.visible)
          .map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                isActive ? "sidebar-link sidebar-link-active" : "sidebar-link"
              }
            >
              <span className="sidebar-icon">{item.icon}</span>
              <span>{item.label}</span>
            </NavLink>
          ))}
      </nav>

      <div className="sidebar-footer">
        <div className="security-badge">JWT Secure</div>
        <small>Accès protégé par rôles</small>
      </div>
    </aside>
  );
}

export default Sidebar;
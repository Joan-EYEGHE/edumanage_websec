import { NavLink } from "react-router-dom";

function Sidebar() {
  const menuItems = [
    { label: "Dashboard", path: "/dashboard" },
    { label: "Utilisateurs", path: "/users" },
    { label: "Formations", path: "/formations" },
    { label: "Inscriptions", path: "/inscriptions" },
    { label: "Paiements", path: "/paiements" },
    { label: "Audit Logs", path: "/audit-logs" },
  ];

  return (
    <aside style={styles.sidebar}>
      <div style={styles.logo}>EduManage Secure</div>

      <nav style={styles.nav}>
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            style={({ isActive }) => ({
              ...styles.link,
              ...(isActive ? styles.activeLink : {}),
            })}
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}

const styles = {
  sidebar: {
    width: "240px",
    minHeight: "100vh",
    backgroundColor: "#111827",
    color: "#fff",
    padding: "1.5rem 1rem",
  },
  logo: {
    fontSize: "1.2rem",
    fontWeight: "bold",
    marginBottom: "2rem",
    textAlign: "center",
  },
  nav: {
    display: "flex",
    flexDirection: "column",
    gap: "0.75rem",
  },
  link: {
    padding: "0.8rem 1rem",
    borderRadius: "8px",
    color: "#d1d5db",
    transition: "0.2s",
  },
  activeLink: {
    backgroundColor: "#2563eb",
    color: "#fff",
  },
};

export default Sidebar;
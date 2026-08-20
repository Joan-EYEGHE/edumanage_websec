import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function LoginLogo() {
  return (
    <svg width="230" height="70" viewBox="0 0 220 64" xmlns="http://www.w3.org/2000/svg">
      <rect x="6" y="8" width="48" height="48" rx="14" fill="#00798f" opacity="0.12" />
      <path d="M18 22 L32 15 L46 22 V32 C46 42 39 48 32 51 C25 48 18 42 18 32V22Z" fill="#00798f" />
      <path d="M27 24 H39" stroke="#f5feff" strokeWidth="3" strokeLinecap="round" />
      <path d="M27 32 H37" stroke="#f5feff" strokeWidth="3" strokeLinecap="round" />
      <path d="M27 40 H39" stroke="#f5feff" strokeWidth="3" strokeLinecap="round" />
      <text x="66" y="35" fontFamily="Inter, Arial, sans-serif" fontSize="23" fontWeight="800" fill="#00798f">
        EduManage
      </text>
      <text x="68" y="50" fontFamily="Inter, Arial, sans-serif" fontSize="10" fontWeight="600" fill="#00798f" opacity="0.75" letterSpacing="2">
        SECURE LMS
      </text>
    </svg>
  );
}

function LoginPage() {
  const navigate = useNavigate();
  const { login, loading } = useAuth();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!formData.email.trim() || !formData.password.trim()) {
      setError("Veuillez renseigner l’email et le mot de passe.");
      return;
    }

    try {
      await login(formData.email.trim(), formData.password);
      navigate("/dashboard");
    } catch (err) {
      console.error("Erreur login :", err);
      setError("Email ou mot de passe incorrect.");
    }
  };

  return (
    <div style={styles.page}>
      <div style={styles.backgroundCircleOne}></div>
      <div style={styles.backgroundCircleTwo}></div>

      <div style={styles.leftPanel}>
        <div style={styles.badge}>Plateforme sécurisée</div>
        <h1 style={styles.heroTitle}>
          Gérez les formations, inscriptions et paiements en toute sécurité.
        </h1>
        <p style={styles.heroText}>
          EduManage centralise les utilisateurs, les rôles, les formations et les opérations sensibles avec authentification JWT.
        </p>

        <div style={styles.featureGrid}>
          <div style={styles.featureCard}>Authentification JWT</div>
          <div style={styles.featureCard}>Gestion des rôles</div>
          <div style={styles.featureCard}>Suivi des paiements</div>
          <div style={styles.featureCard}>Traçabilité sécurisée</div>
        </div>
      </div>

      <div style={styles.card}>
        <div style={styles.logoWrapper}>
          <LoginLogo />
        </div>

        <div style={styles.heading}>
          <h2 style={styles.title}>Connexion</h2>
          <p style={styles.subtitle}>Accédez à votre espace sécurisé</p>
        </div>

        <form onSubmit={handleSubmit} style={styles.form} autoComplete="off">
          <div style={styles.inputGroup}>
            <label style={styles.label}>Adresse email</label>
            <input
              type="email"
              name="email"
              placeholder="exemple@email.com"
              value={formData.email}
              onChange={handleChange}
              style={styles.input}
              required
              autoComplete="off"
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Mot de passe</label>
            <input
              type="password"
              name="password"
              placeholder="Votre mot de passe"
              value={formData.password}
              onChange={handleChange}
              style={styles.input}
              required
              autoComplete="new-password"
            />
          </div>

          {error && <div style={styles.error}>{error}</div>}

          <button type="submit" style={styles.button} disabled={loading}>
            {loading ? "Connexion en cours..." : "Se connecter"}
          </button>
        </form>

        <p style={styles.footerText}>
          Accès réservé aux utilisateurs autorisés.
        </p>
      </div>
    </div>
  );
}

const styles = {
  page: {
    minHeight: "100vh",
    display: "grid",
    gridTemplateColumns: "1.1fr 0.9fr",
    alignItems: "center",
    gap: "2rem",
    padding: "3rem",
    background:
      "radial-gradient(circle at top left, rgba(0,121,143,0.22), transparent 32%), linear-gradient(135deg, #f5feff 0%, #ffffff 55%, #eafcff 100%)",
    position: "relative",
    overflow: "hidden",
  },
  backgroundCircleOne: {
    position: "absolute",
    width: "360px",
    height: "360px",
    borderRadius: "50%",
    background: "rgba(0,121,143,0.08)",
    bottom: "-120px",
    left: "-100px",
  },
  backgroundCircleTwo: {
    position: "absolute",
    width: "260px",
    height: "260px",
    borderRadius: "50%",
    background: "rgba(0,121,143,0.1)",
    top: "-90px",
    right: "120px",
  },
  leftPanel: {
    position: "relative",
    zIndex: 1,
    maxWidth: "650px",
  },
  badge: {
    display: "inline-block",
    backgroundColor: "rgba(0,121,143,0.12)",
    color: "#00798f",
    padding: "0.55rem 0.9rem",
    borderRadius: "999px",
    fontWeight: 800,
    fontSize: "0.85rem",
    marginBottom: "1.2rem",
  },
  heroTitle: {
    fontSize: "3.2rem",
    lineHeight: 1.08,
    color: "#102a30",
    marginBottom: "1.2rem",
    letterSpacing: "-1.8px",
  },
  heroText: {
    fontSize: "1.08rem",
    lineHeight: 1.7,
    color: "#547176",
    maxWidth: "560px",
    marginBottom: "1.5rem",
  },
  featureGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(2, minmax(180px, 1fr))",
    gap: "0.9rem",
    maxWidth: "500px",
  },
  featureCard: {
    backgroundColor: "rgba(255,255,255,0.72)",
    border: "1px solid rgba(0,121,143,0.14)",
    boxShadow: "0 10px 28px rgba(0,121,143,0.08)",
    borderRadius: "16px",
    padding: "1rem",
    color: "#00798f",
    fontWeight: 800,
  },
  card: {
    position: "relative",
    zIndex: 1,
    width: "100%",
    maxWidth: "460px",
    justifySelf: "center",
    backgroundColor: "rgba(255,255,255,0.9)",
    backdropFilter: "blur(18px)",
    border: "1px solid rgba(0,121,143,0.14)",
    padding: "2rem",
    borderRadius: "28px",
    boxShadow: "0 24px 70px rgba(0,121,143,0.18)",
  },
  logoWrapper: {
    display: "flex",
    justifyContent: "center",
    marginBottom: "1rem",
  },
  heading: {
    textAlign: "center",
    marginBottom: "1.4rem",
  },
  title: {
    fontSize: "1.8rem",
    color: "#102a30",
    marginBottom: "0.4rem",
  },
  subtitle: {
    color: "#6b7c80",
    fontSize: "0.95rem",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  inputGroup: {
    display: "flex",
    flexDirection: "column",
    gap: "0.45rem",
  },
  label: {
    fontSize: "0.9rem",
    fontWeight: 800,
    color: "#24464d",
  },
  input: {
    padding: "0.95rem 1rem",
    borderRadius: "14px",
    border: "1px solid rgba(0,121,143,0.18)",
    fontSize: "1rem",
    outline: "none",
    backgroundColor: "#f9feff",
  },
  button: {
    marginTop: "0.4rem",
    padding: "1rem",
    borderRadius: "16px",
    border: "none",
    background: "linear-gradient(135deg, #00798f, #005f70)",
    color: "#fff",
    fontSize: "1rem",
    fontWeight: 900,
    boxShadow: "0 16px 30px rgba(0,121,143,0.28)",
  },
  error: {
    backgroundColor: "#fee2e2",
    color: "#991b1b",
    padding: "0.8rem",
    borderRadius: "12px",
    fontSize: "0.9rem",
    fontWeight: 700,
  },
  footerText: {
    textAlign: "center",
    marginTop: "1rem",
    color: "#7a8d91",
    fontSize: "0.85rem",
  },
};

export default LoginPage;
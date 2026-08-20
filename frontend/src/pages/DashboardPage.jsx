import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import { getUsers } from "../api/usersApi";
import { getFormations } from "../api/formationsApi";
import { getInscriptions } from "../api/inscriptionsApi";
import { getPaiements } from "../api/paiementsApi";

function DashboardPage() {
  const [stats, setStats] = useState({
    users: 0,
    formations: 0,
    inscriptions: 0,
    paiements: 0,
  });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const users = await getUsers({ page: 0, size: 1 });
        const formations = await getFormations({ page: 0, size: 1 });
        const inscriptions = await getInscriptions({ page: 0, size: 1 });
        const paiements = await getPaiements({ page: 0, size: 1 });

        setStats({
          users: users.metadata.totalElements || 0,
          formations: formations.metadata.totalElements || 0,
          inscriptions: inscriptions.metadata.totalElements || 0,
          paiements: paiements.metadata.totalElements || 0,
        });
      } catch (error) {
        console.error("Erreur chargement dashboard :", error);
      }
    };

    fetchStats();
  }, []);

  const cards = [
    {
      title: "Utilisateurs",
      value: stats.users,
      icon: "👥",
      description: "Comptes actifs dans le système",
    },
    {
      title: "Formations",
      value: stats.formations,
      icon: "🎓",
      description: "Modules de formation disponibles",
    },
    {
      title: "Inscriptions",
      value: stats.inscriptions,
      icon: "📝",
      description: "Apprenants inscrits aux formations",
    },
    {
      title: "Paiements",
      value: stats.paiements,
      icon: "💳",
      description: "Transactions enregistrées",
    },
  ];

  return (
    <AppLayout>
      <PageHeader
        title="Dashboard"
        subtitle="Vue d’ensemble de la plateforme EduManage"
      />

      <section style={styles.hero}>
        <div>
          <span style={styles.badge}>Système sécurisé</span>
          <h1 style={styles.heroTitle}>Bienvenue dans EduManage</h1>
          <p style={styles.heroText}>
            Suivez en temps réel les utilisateurs, formations, inscriptions et paiements depuis une interface centralisée.
          </p>
        </div>

        <div style={styles.heroPanel}>
          <p style={styles.heroPanelLabel}>Statut système</p>
          <h2 style={styles.heroPanelTitle}>Opérationnel</h2>
          <p style={styles.heroPanelText}>Backend connecté · JWT actif · Rôles appliqués</p>
        </div>
      </section>

      <div style={styles.grid}>
        {cards.map((card) => (
          <div key={card.title} style={styles.card}>
            <div style={styles.cardTop}>
              <div style={styles.iconBox}>{card.icon}</div>
              <span style={styles.cardChip}>Live</span>
            </div>

            <h2 style={styles.value}>{card.value}</h2>
            <h3 style={styles.cardTitle}>{card.title}</h3>
            <p style={styles.cardText}>{card.description}</p>
          </div>
        ))}
      </div>

      <section style={styles.infoGrid}>
        <div style={styles.infoBox}>
          <h2 style={styles.infoTitle}>Résumé du système</h2>
          <p style={styles.infoText}>
            Les indicateurs affichés sont récupérés directement depuis le backend. Ils reflètent les données réellement enregistrées dans la base PostgreSQL.
          </p>
        </div>

        <div style={styles.infoBox}>
          <h2 style={styles.infoTitle}>Sécurité</h2>
          <p style={styles.infoText}>
            L’accès est protégé par authentification JWT, gestion des rôles et contrôle des menus selon le profil connecté.
          </p>
        </div>
      </section>
    </AppLayout>
  );
}

const styles = {
  hero: {
    background: "linear-gradient(135deg, #00798f, #005f70)",
    color: "#f5feff",
    borderRadius: "28px",
    padding: "2rem",
    display: "flex",
    justifyContent: "space-between",
    gap: "1.5rem",
    alignItems: "center",
    boxShadow: "0 22px 50px rgba(0,121,143,0.25)",
    marginBottom: "1.5rem",
  },
  badge: {
    display: "inline-block",
    backgroundColor: "rgba(245,254,255,0.18)",
    padding: "0.5rem 0.8rem",
    borderRadius: "999px",
    fontWeight: 800,
    fontSize: "0.8rem",
    marginBottom: "1rem",
  },
  heroTitle: {
    fontSize: "2.2rem",
    marginBottom: "0.7rem",
  },
  heroText: {
    maxWidth: "620px",
    lineHeight: 1.6,
    opacity: 0.9,
  },
  heroPanel: {
    minWidth: "260px",
    backgroundColor: "rgba(245,254,255,0.14)",
    border: "1px solid rgba(245,254,255,0.18)",
    borderRadius: "22px",
    padding: "1.2rem",
  },
  heroPanelLabel: {
    fontSize: "0.8rem",
    opacity: 0.8,
    marginBottom: "0.5rem",
  },
  heroPanelTitle: {
    fontSize: "1.6rem",
    marginBottom: "0.4rem",
  },
  heroPanelText: {
    fontSize: "0.9rem",
    opacity: 0.85,
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(230px, 1fr))",
    gap: "1.2rem",
  },
  card: {
    backgroundColor: "#fff",
    borderRadius: "24px",
    padding: "1.4rem",
    border: "1px solid rgba(0,121,143,0.12)",
    boxShadow: "0 14px 34px rgba(16,42,48,0.08)",
  },
  cardTop: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "1.2rem",
  },
  iconBox: {
    width: "52px",
    height: "52px",
    borderRadius: "18px",
    backgroundColor: "#f5feff",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    fontSize: "1.5rem",
    border: "1px solid rgba(0,121,143,0.14)",
  },
  cardChip: {
    backgroundColor: "rgba(0,121,143,0.1)",
    color: "#00798f",
    padding: "0.35rem 0.65rem",
    borderRadius: "999px",
    fontSize: "0.75rem",
    fontWeight: 800,
  },
  value: {
    fontSize: "2.3rem",
    color: "#00798f",
    marginBottom: "0.2rem",
  },
  cardTitle: {
    fontSize: "1.05rem",
    marginBottom: "0.4rem",
    color: "#102a30",
  },
  cardText: {
    color: "#6b7c80",
    fontSize: "0.92rem",
    lineHeight: 1.5,
  },
  infoGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))",
    gap: "1rem",
    marginTop: "1.5rem",
  },
  infoBox: {
    backgroundColor: "#fff",
    borderRadius: "22px",
    padding: "1.4rem",
    border: "1px solid rgba(0,121,143,0.12)",
    boxShadow: "0 10px 28px rgba(16,42,48,0.07)",
  },
  infoTitle: {
    color: "#102a30",
    marginBottom: "0.5rem",
  },
  infoText: {
    color: "#6b7c80",
    lineHeight: 1.7,
  },
};

export default DashboardPage;
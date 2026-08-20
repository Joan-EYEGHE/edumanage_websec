import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import Pagination from "../components/common/Pagination";
import { getAuditLogs } from "../api/auditLogsApi";

function AuditLogsPage() {
  const [logs, setLogs] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const columns = [
    { key: "userName", label: "Utilisateur" },
    { key: "userEmail", label: "Email" },
    { key: "action", label: "Action" },
    { key: "cible", label: "Cible" },
    { key: "adresseIp", label: "Adresse IP" },
    { key: "createdAt", label: "Date" },
  ];

  const formatDate = (value) => {
    if (!value) return "-";

    const text = String(value);

    if (text.includes("T")) {
      return text.replace("T", " ").slice(0, 16);
    }

    if (text.length === 8) {
      return `${text.slice(0, 4)}-${text.slice(4, 6)}-${text.slice(6, 8)}`;
    }

    return text;
  };

  useEffect(() => {
    const fetchLogs = async () => {
      setLoading(true);
      setError("");

      try {
        const result = await getAuditLogs({ page, size: 10 });

        const formattedData = Array.isArray(result.payload)
          ? result.payload.map((log) => ({
              id: log.id,
              userName: log.userName || log.utilisateur || "-",
              userEmail: log.userEmail || log.email || "-",
              action: log.action || "-",
              cible: log.cible || log.target || "-",
              adresseIp: log.adresseIp || log.ip || "-",
              createdAt: formatDate(log.createdAt || log.dateCreation),
            }))
          : [];

        setLogs(formattedData);
        setMetadata(result.metadata);
      } catch (err) {
        console.error(err);
        setError(err.message || "Impossible de charger les logs d’audit.");
      } finally {
        setLoading(false);
      }
    };

    fetchLogs();
  }, [page]);

  return (
    <AppLayout>
      <PageHeader
        title="Audit Logs"
        subtitle="Traçabilité et suivi des actions sensibles"
      />

      <div style={styles.securityPanel}>
        <div style={styles.iconBox}>🔐</div>
        <div>
          <h2 style={styles.panelTitle}>Journalisation de sécurité</h2>
          <p style={styles.panelText}>
            Cette section permet de suivre les actions sensibles effectuées dans
            le système : connexion, création, modification, suppression et opérations critiques.
          </p>
        </div>
      </div>

      {loading && <LoadingMessage message="Chargement des logs d’audit..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && logs.length === 0 && (
        <div style={styles.emptyBox}>
          <div style={styles.emptyIcon}>🛡️</div>
          <h3 style={styles.emptyTitle}>Aucun journal d’audit enregistré</h3>
          <p style={styles.emptyText}>
            Aucun événement de sécurité n’a encore été transmis par le backend.
            Dès que la journalisation sera activée, les actions apparaîtront ici.
          </p>
        </div>
      )}

      {!loading && !error && logs.length > 0 && (
        <>
          <DataTable columns={columns} data={logs} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}
    </AppLayout>
  );
}

const styles = {
  securityPanel: {
    display: "flex",
    alignItems: "center",
    gap: "1rem",
    background: "linear-gradient(135deg, #00798f, #005f70)",
    color: "#f5feff",
    borderRadius: "24px",
    padding: "1.4rem",
    boxShadow: "0 18px 40px rgba(0,121,143,0.22)",
    marginBottom: "1.4rem",
  },
  iconBox: {
    width: "58px",
    height: "58px",
    borderRadius: "20px",
    backgroundColor: "rgba(245,254,255,0.16)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "1.8rem",
  },
  panelTitle: {
    fontSize: "1.25rem",
    marginBottom: "0.35rem",
  },
  panelText: {
    opacity: 0.9,
    lineHeight: 1.6,
    maxWidth: "760px",
  },
  emptyBox: {
    backgroundColor: "#fff",
    padding: "2.2rem",
    borderRadius: "24px",
    color: "#6b7c80",
    border: "1px solid rgba(0,121,143,0.12)",
    boxShadow: "0 14px 34px rgba(16,42,48,0.08)",
    textAlign: "center",
  },
  emptyIcon: {
    fontSize: "2.4rem",
    marginBottom: "0.8rem",
  },
  emptyTitle: {
    color: "#102a30",
    fontSize: "1.3rem",
    marginBottom: "0.5rem",
  },
  emptyText: {
    maxWidth: "620px",
    margin: "0 auto",
    lineHeight: 1.7,
  },
};

export default AuditLogsPage;
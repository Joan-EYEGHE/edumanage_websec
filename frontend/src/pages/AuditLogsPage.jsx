import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import { getAuditLogs } from "../api/auditLogsApi";

function AuditLogsPage() {
  const [logs, setLogs] = useState([]);
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

  useEffect(() => {
    const fetchLogs = async () => {
      try {
        const data = await getAuditLogs();

        const formattedData = data.map((log) => ({
          id: log.id,
          userName: log.userName,
          userEmail: log.userEmail,
          action: log.action,
          cible: log.cible,
          adresseIp: log.adresseIp,
          createdAt: log.createdAt,
        }));

        setLogs(formattedData);
      } catch (err) {
        console.error(err);
        setError("Impossible de charger les logs d’audit.");
      } finally {
        setLoading(false);
      }
    };

    fetchLogs();
  }, []);

  return (
    <AppLayout>
      <PageHeader
        title="Audit Logs"
        subtitle="Traçabilité des actions sensibles"
      />

      {loading && <LoadingMessage message="Chargement des logs d’audit..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && <DataTable columns={columns} data={logs} />}
    </AppLayout>
  );
}

export default AuditLogsPage;
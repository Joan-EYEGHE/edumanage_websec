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

  useEffect(() => {
    const fetchLogs = async () => {
      setLoading(true);
      setError("");

      try {
        const result = await getAuditLogs({ page, size: 10 });

        const formattedData = result.payload.map((log) => ({
          id: log.id,
          userName: log.userName,
          userEmail: log.userEmail,
          action: log.action,
          cible: log.cible,
          adresseIp: log.adresseIp,
          createdAt: log.createdAt,
        }));

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
        subtitle="Traçabilité des actions sensibles"
      />

      {loading && <LoadingMessage message="Chargement des logs d’audit..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && (
        <>
          <DataTable columns={columns} data={logs} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}
    </AppLayout>
  );
}

export default AuditLogsPage;
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";

function AuditLogsPage() {
  const columns = [
    { key: "userName", label: "Utilisateur" },
    { key: "userEmail", label: "Email" },
    { key: "action", label: "Action" },
    { key: "cible", label: "Cible" },
    { key: "adresseIp", label: "Adresse IP" },
    { key: "createdAt", label: "Date" },
  ];

  const data = [
    {
      id: "1",
      userName: "Hamidou Camara",
      userEmail: "hamidou@test.com",
      action: "LOGIN",
      cible: "AUTH",
      adresseIp: "127.0.0.1",
      createdAt: "2026-04-22 12:30",
    },
    {
      id: "2",
      userName: "Awa Diallo",
      userEmail: "awa@test.com",
      action: "CREATE_INSCRIPTION",
      cible: "INSCRIPTION",
      adresseIp: "127.0.0.1",
      createdAt: "2026-04-22 12:35",
    },
  ];

  return (
    <AppLayout>
      <PageHeader
        title="Audit Logs"
        subtitle="Traçabilité des actions sensibles"
      />
      <DataTable columns={columns} data={data} />
    </AppLayout>
  );
}

export default AuditLogsPage;
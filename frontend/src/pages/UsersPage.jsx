import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";

function UsersPage() {
  const columns = [
    { key: "nom", label: "Nom" },
    { key: "prenom", label: "Prénom" },
    { key: "email", label: "Email" },
    { key: "telephone", label: "Téléphone" },
    { key: "roles", label: "Rôle" },
    { key: "actif", label: "Actif" },
  ];

  const data = [
    {
      id: "1",
      nom: "Camara",
      prenom: "Hamidou",
      email: "hamidou@test.com",
      telephone: "770000000",
      roles: "ADMIN",
      actif: "Oui",
    },
    {
      id: "2",
      nom: "Diallo",
      prenom: "Awa",
      email: "awa@test.com",
      telephone: "771111111",
      roles: "APPRENANT",
      actif: "Oui",
    },
  ];

  return (
    <AppLayout>
      <PageHeader
        title="Utilisateurs"
        subtitle="Liste des comptes utilisateurs"
      />
      <DataTable columns={columns} data={data} />
    </AppLayout>
  );
}

export default UsersPage;
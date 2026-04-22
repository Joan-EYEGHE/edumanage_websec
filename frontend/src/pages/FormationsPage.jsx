import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";

function FormationsPage() {
  const columns = [
    { key: "titre", label: "Titre" },
    { key: "description", label: "Description" },
    { key: "duree", label: "Durée" },
    { key: "prix", label: "Prix" },
    { key: "statut", label: "Statut" },
  ];

  const data = [
    {
      id: "1",
      titre: "Cybersécurité",
      description: "Formation sur les normes et protocoles",
      duree: "30 h",
      prix: "50000 FCFA",
      statut: "PUBLIEE",
    },
    {
      id: "2",
      titre: "Spring Boot",
      description: "API sécurisées avec JWT",
      duree: "20 h",
      prix: "40000 FCFA",
      statut: "BROUILLON",
    },
  ];

  return (
    <AppLayout>
      <PageHeader
        title="Formations"
        subtitle="Catalogue des formations disponibles"
      />
      <DataTable columns={columns} data={data} />
    </AppLayout>
  );
}

export default FormationsPage;
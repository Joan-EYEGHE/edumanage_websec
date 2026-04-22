import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";

function InscriptionsPage() {
  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "formationTitre", label: "Formation" },
    { key: "dateInscription", label: "Date inscription" },
    { key: "statut", label: "Statut" },
  ];

  const data = [
    {
      id: "1",
      apprenantNom: "Awa Diallo",
      formationTitre: "Cybersécurité",
      dateInscription: "2026-04-22",
      statut: "VALIDEE",
    },
    {
      id: "2",
      apprenantNom: "Moussa Ba",
      formationTitre: "Spring Boot",
      dateInscription: "2026-04-21",
      statut: "EN_ATTENTE",
    },
  ];

  return (
    <AppLayout>
      <PageHeader
        title="Inscriptions"
        subtitle="Liste des apprenants inscrits"
      />
      <DataTable columns={columns} data={data} />
    </AppLayout>
  );
}

export default InscriptionsPage;
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";

function PaiementsPage() {
  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "montant", label: "Montant" },
    { key: "modePaiement", label: "Mode de paiement" },
    { key: "referenceTransaction", label: "Référence" },
    { key: "statut", label: "Statut" },
    { key: "datePaiement", label: "Date" },
  ];

  const data = [
    {
      id: "1",
      apprenantNom: "Awa Diallo",
      montant: "50000 FCFA",
      modePaiement: "Wave",
      referenceTransaction: "TXN-001",
      statut: "PAYE",
      datePaiement: "2026-04-22",
    },
    {
      id: "2",
      apprenantNom: "Moussa Ba",
      montant: "40000 FCFA",
      modePaiement: "Orange Money",
      referenceTransaction: "TXN-002",
      statut: "EN_ATTENTE",
      datePaiement: "2026-04-21",
    },
  ];

  return (
    <AppLayout>
      <PageHeader
        title="Paiements"
        subtitle="Suivi des transactions et paiements"
      />
      <DataTable columns={columns} data={data} />
    </AppLayout>
  );
}

export default PaiementsPage;
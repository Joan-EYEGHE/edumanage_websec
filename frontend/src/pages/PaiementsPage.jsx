import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import { getPaiements } from "../api/paiementsApi";

function PaiementsPage() {
  const [paiements, setPaiements] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "montant", label: "Montant" },
    { key: "modePaiement", label: "Mode de paiement" },
    { key: "referenceTransaction", label: "Référence" },
    { key: "statut", label: "Statut" },
    { key: "datePaiement", label: "Date" },
  ];

  useEffect(() => {
    const fetchPaiements = async () => {
      try {
        const data = await getPaiements();

        const formattedData = data.map((paiement) => ({
          id: paiement.id,
          apprenantNom: paiement.apprenantNom,
          montant: paiement.montant,
          modePaiement: paiement.modePaiement,
          referenceTransaction: paiement.referenceTransaction,
          statut: paiement.statut,
          datePaiement: paiement.datePaiement,
        }));

        setPaiements(formattedData);
      } catch (err) {
        console.error(err);
        setError("Impossible de charger les paiements.");
      } finally {
        setLoading(false);
      }
    };

    fetchPaiements();
  }, []);

  return (
    <AppLayout>
      <PageHeader
        title="Paiements"
        subtitle="Suivi des transactions et paiements"
      />

      {loading && <LoadingMessage message="Chargement des paiements..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && <DataTable columns={columns} data={paiements} />}
    </AppLayout>
  );
}

export default PaiementsPage;
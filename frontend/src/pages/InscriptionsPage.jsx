import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import { getInscriptions } from "../api/inscriptionsApi";

function InscriptionsPage() {
  const [inscriptions, setInscriptions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "formationTitre", label: "Formation" },
    { key: "dateInscription", label: "Date inscription" },
    { key: "statut", label: "Statut" },
  ];

  useEffect(() => {
    const fetchInscriptions = async () => {
      try {
        const data = await getInscriptions();

        const formattedData = data.map((inscription) => ({
          id: inscription.id,
          apprenantNom: inscription.apprenantNom,
          formationTitre: inscription.formationTitre,
          dateInscription: inscription.dateInscription,
          statut: inscription.statut,
        }));

        setInscriptions(formattedData);
      } catch (err) {
        console.error(err);
        setError("Impossible de charger les inscriptions.");
      } finally {
        setLoading(false);
      }
    };

    fetchInscriptions();
  }, []);

  return (
    <AppLayout>
      <PageHeader
        title="Inscriptions"
        subtitle="Liste des apprenants inscrits"
      />

      {loading && <LoadingMessage message="Chargement des inscriptions..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && (
        <DataTable columns={columns} data={inscriptions} />
      )}
    </AppLayout>
  );
}

export default InscriptionsPage;
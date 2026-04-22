import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import { getFormations } from "../api/formationsApi";

function FormationsPage() {
  const [formations, setFormations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const columns = [
    { key: "titre", label: "Titre" },
    { key: "description", label: "Description" },
    { key: "duree", label: "Durée" },
    { key: "prix", label: "Prix" },
    { key: "statut", label: "Statut" },
  ];

  useEffect(() => {
    const fetchFormations = async () => {
      try {
        const data = await getFormations();

        const formattedData = data.map((formation) => ({
          id: formation.id,
          titre: formation.titre,
          description: formation.description,
          duree: formation.duree,
          prix: formation.prix,
          statut: formation.statut,
        }));

        setFormations(formattedData);
      } catch (err) {
        console.error(err);
        setError("Impossible de charger les formations.");
      } finally {
        setLoading(false);
      }
    };

    fetchFormations();
  }, []);

  return (
    <AppLayout>
      <PageHeader
        title="Formations"
        subtitle="Catalogue des formations disponibles"
      />

      {loading && <LoadingMessage message="Chargement des formations..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && (
        <DataTable columns={columns} data={formations} />
      )}
    </AppLayout>
  );
}

export default FormationsPage;
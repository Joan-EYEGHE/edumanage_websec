import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import Pagination from "../components/common/Pagination";
import SearchBar from "../components/common/SearchBar";
import Modal from "../components/common/Modal";
import FormInput from "../components/common/FormInput";
import StatusBadge from "../components/common/StatusBadge";
import { getInscriptions, createInscription } from "../api/inscriptionsApi";
import { getReadableError } from "../utils/errorHandler";
import { useAuth } from "../context/AuthContext";
import { hasAnyRole } from "../utils/roles";

function InscriptionsPage() {
  const { user } = useAuth();

  const [inscriptions, setInscriptions] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [formData, setFormData] = useState({
    apprenantId: "",
    formationId: "",
  });

  const canManageInscriptions = hasAnyRole(user, [
    "ADMIN",
    "GESTIONNAIRE",
    "FORMATEUR",
  ]);

  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "formationTitre", label: "Formation" },
    { key: "dateInscription", label: "Date inscription" },
    {
      key: "statut",
      label: "Statut",
      render: (row) => <StatusBadge value={row.statut} />,
    },
  ];

  const fetchInscriptions = async () => {
    setLoading(true);
    setError("");

    try {
      const result = await getInscriptions({
        page,
        size: 10,
        keyword: search,
      });

      const formattedData = result.payload.map((inscription) => ({
        id: inscription.id,
        apprenantNom: inscription.apprenantNom,
        formationTitre: inscription.formationTitre,
        dateInscription: inscription.dateInscription,
        statut: inscription.statut,
      }));

      setInscriptions(formattedData);
      setMetadata(result.metadata);
    } catch (err) {
      setError(getReadableError(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInscriptions();
  }, [page, search]);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const resetForm = () => {
    setFormData({
      apprenantId: "",
      formationId: "",
    });
  };

  const handleCreateInscription = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    try {
      await createInscription(formData);
      setSuccessMessage("Inscription créée avec succès.");
      setIsModalOpen(false);
      resetForm();
      fetchInscriptions();
    } catch (err) {
      setError(getReadableError(err));
    }
  };

  return (
    <AppLayout>
      <PageHeader
        title="Inscriptions"
        subtitle="Liste des apprenants inscrits"
        action={
          canManageInscriptions ? (
            <button
              style={styles.primaryButton}
              onClick={() => setIsModalOpen(true)}
            >
              + Nouvelle inscription
            </button>
          ) : null
        }
      />

      <div style={styles.toolbar}>
        <SearchBar
          value={search}
          onChange={setSearch}
          placeholder="Rechercher une inscription..."
        />
      </div>

      {successMessage && <div style={styles.success}>{successMessage}</div>}
      {loading && <LoadingMessage message="Chargement des inscriptions..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && (
        <>
          <DataTable columns={columns} data={inscriptions} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title="Créer une inscription"
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleCreateInscription} style={styles.form}>
          <FormInput
            label="Apprenant ID"
            name="apprenantId"
            value={formData.apprenantId}
            onChange={handleChange}
          />

          <FormInput
            label="Formation ID"
            name="formationId"
            value={formData.formationId}
            onChange={handleChange}
          />

          <button type="submit" style={styles.primaryButton}>
            Enregistrer
          </button>
        </form>
      </Modal>
    </AppLayout>
  );
}

const styles = {
  toolbar: {
    marginBottom: "1rem",
  },
  primaryButton: {
    backgroundColor: "#2563eb",
    color: "#fff",
    border: "none",
    padding: "0.65rem 0.9rem",
    borderRadius: "8px",
    fontSize: "0.9rem",
  },
  form: {
    display: "grid",
    gap: "0.8rem",
  },
  success: {
    backgroundColor: "#dcfce7",
    color: "#166534",
    padding: "0.8rem 1rem",
    borderRadius: "10px",
    marginBottom: "1rem",
    fontSize: "0.9rem",
  },
};

export default InscriptionsPage;
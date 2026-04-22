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
import FormSelect from "../components/common/FormSelect";
import StatusBadge from "../components/common/StatusBadge";
import { getFormations, createFormation } from "../api/formationsApi";
import { getReadableError } from "../utils/errorHandler";
import { useAuth } from "../context/AuthContext";
import { hasAnyRole } from "../utils/roles";

function FormationsPage() {
  const { user } = useAuth();

  const [formations, setFormations] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [formData, setFormData] = useState({
    titre: "",
    description: "",
    duree: "",
    prix: "",
    statut: "",
    formateurId: "",
    afficheUrl: "",
  });

  const canManageFormations = hasAnyRole(user, ["ADMIN", "GESTIONNAIRE", "FORMATEUR"]);

  const columns = [
    { key: "titre", label: "Titre" },
    { key: "description", label: "Description" },
    { key: "duree", label: "Durée" },
    { key: "prix", label: "Prix" },
    {
      key: "statut",
      label: "Statut",
      render: (row) => <StatusBadge value={row.statut} />,
    },
  ];

  const fetchFormations = async () => {
    setLoading(true);
    setError("");

    try {
      const result = await getFormations({
        page,
        size: 10,
        keyword: search,
      });

      const formattedData = result.payload.map((formation) => ({
        id: formation.id,
        titre: formation.titre,
        description: formation.description,
        duree: formation.duree,
        prix: formation.prix,
        statut: formation.statut,
      }));

      setFormations(formattedData);
      setMetadata(result.metadata);
    } catch (err) {
      setError(getReadableError(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFormations();
  }, [page, search]);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const resetForm = () => {
    setFormData({
      titre: "",
      description: "",
      duree: "",
      prix: "",
      statut: "",
      formateurId: "",
      afficheUrl: "",
    });
  };

  const handleCreateFormation = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    try {
      await createFormation(formData);
      setSuccessMessage("Formation créée avec succès.");
      setIsModalOpen(false);
      resetForm();
      fetchFormations();
    } catch (err) {
      setError(getReadableError(err));
    }
  };

  return (
    <AppLayout>
      <PageHeader
        title="Formations"
        subtitle="Catalogue des formations disponibles"
        action={
          canManageFormations ? (
            <button style={styles.primaryButton} onClick={() => setIsModalOpen(true)}>
              + Nouvelle formation
            </button>
          ) : null
        }
      />

      <div style={styles.toolbar}>
        <SearchBar
          value={search}
          onChange={setSearch}
          placeholder="Rechercher une formation..."
        />
      </div>

      {successMessage && <div style={styles.success}>{successMessage}</div>}
      {loading && <LoadingMessage message="Chargement des formations..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && (
        <>
          <DataTable columns={columns} data={formations} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title="Créer une formation"
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleCreateFormation} style={styles.form}>
          <FormInput label="Titre" name="titre" value={formData.titre} onChange={handleChange} />
          <FormInput label="Description" name="description" value={formData.description} onChange={handleChange} />
          <FormInput label="Durée" name="duree" value={formData.duree} onChange={handleChange} />
          <FormInput label="Prix" name="prix" type="number" value={formData.prix} onChange={handleChange} />
          <FormInput label="Formateur ID" name="formateurId" value={formData.formateurId} onChange={handleChange} />
          <FormInput label="Affiche URL" name="afficheUrl" value={formData.afficheUrl} onChange={handleChange} />
          <FormSelect
            label="Statut"
            name="statut"
            value={formData.statut}
            onChange={handleChange}
            options={[
              { value: "BROUILLON", label: "BROUILLON" },
              { value: "PUBLIEE", label: "PUBLIEE" },
              { value: "TERMINEE", label: "TERMINEE" },
              { value: "ANNULEE", label: "ANNULEE" },
            ]}
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

export default FormationsPage;
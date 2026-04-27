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
    formateurId: "",
    afficheUrl: "",
  });

  const canManageFormations = hasAnyRole(user, [
    "ADMINISTRATEUR",
    "GESTIONNAIRE",
    "FORMATEUR",
  ]);

  const columns = [
    { key: "titre", label: "Titre" },
    { key: "description", label: "Description" },
    { key: "duree", label: "Durée" },
    { key: "prix", label: "Prix" },
    { key: "formateurNom", label: "Formateur" },
  ];

  const fetchFormations = async () => {
    setLoading(true);
    setError("");

    try {
      const result = await getFormations({
        page,
        size: 10,
      });

      const formattedData = result.payload.map((formation) => ({
        id: formation.id,
        titre: formation.titre || "-",
        description: formation.description || "-",
        duree: formation.duree ? `${formation.duree} h` : "-",
        prix:
          formation.prix !== null && formation.prix !== undefined
            ? `${formation.prix} FCFA`
            : "-",
        formateurNom: formation.formateurNom || "-",
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
  }, [page]);

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
      formateurId: "",
      afficheUrl: "",
    });
  };

  const filteredFormations = formations.filter((item) =>
  Object.values(item).join(" ").toLowerCase().includes(search.toLowerCase())
);

  const handleCreateFormation = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    if (
      !formData.titre.trim() ||
      !formData.description.trim() ||
      !formData.duree ||
      !formData.prix
    ) {
      setError("Veuillez remplir les champs obligatoires : titre, description, durée et prix.");
      return;
    }

    const payload = {
      titre: formData.titre.trim(),
      description: formData.description.trim(),
      duree: parseInt(formData.duree, 10),
      prix: parseFloat(formData.prix),
      formateurId: formData.formateurId
        ? parseInt(formData.formateurId, 10)
        : null,
      afficheUrl: formData.afficheUrl?.trim() || null,
    };

    try {
      const response = await createFormation(payload);

      if (response?.status && response.status !== "OK") {
        setError(response.message || "Impossible de créer la formation.");
        return;
      }

      setSuccessMessage("Formation créée avec succès.");
      setIsModalOpen(false);
      resetForm();
      await fetchFormations();
    } catch (err) {
      console.error("Erreur création formation :", err);
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
            <button
              style={styles.primaryButton}
              onClick={() => {
                resetForm();
                setIsModalOpen(true);
              }}
            >
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
          <DataTable columns={columns} data={filteredFormations} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title="Créer une formation"
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleCreateFormation} style={styles.form}>
          <div style={styles.formGrid}>
            <FormInput
              label="Titre"
              name="titre"
              value={formData.titre}
              onChange={handleChange}
              required
            />

            <FormInput
              label="Durée en heures"
              name="duree"
              type="number"
              value={formData.duree}
              onChange={handleChange}
              required
            />

            <FormInput
              label="Prix"
              name="prix"
              type="number"
              value={formData.prix}
              onChange={handleChange}
              required
            />

            <FormInput
              label="Formateur ID"
              name="formateurId"
              type="number"
              value={formData.formateurId}
              onChange={handleChange}
            />
          </div>

          <FormInput
            label="Description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            required
          />

          <FormInput
            label="Affiche URL"
            name="afficheUrl"
            value={formData.afficheUrl}
            onChange={handleChange}
          />

          <button type="submit" style={styles.primaryButtonFull}>
            Enregistrer la formation
          </button>
        </form>
      </Modal>
    </AppLayout>
  );
}

const styles = {
  toolbar: {
    marginBottom: "1.2rem",
  },
  primaryButton: {
    background: "linear-gradient(135deg, #00798f, #005f70)",
    color: "#fff",
    border: "none",
    padding: "0.85rem 1.1rem",
    borderRadius: "14px",
    fontSize: "0.95rem",
    fontWeight: 900,
    boxShadow: "0 12px 24px rgba(0,121,143,0.22)",
  },
  primaryButtonFull: {
    background: "linear-gradient(135deg, #00798f, #005f70)",
    color: "#fff",
    border: "none",
    padding: "0.95rem 1rem",
    borderRadius: "16px",
    fontSize: "0.95rem",
    fontWeight: 900,
    marginTop: "0.4rem",
    boxShadow: "0 14px 28px rgba(0,121,143,0.25)",
  },
  form: {
    display: "grid",
    gap: "1.1rem",
    overflow: "visible",
  },
  formGrid: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: "1rem",
    overflow: "visible",
  },
  success: {
    backgroundColor: "#dcfce7",
    color: "#166534",
    padding: "0.9rem 1rem",
    borderRadius: "14px",
    marginBottom: "1rem",
    fontSize: "0.95rem",
    fontWeight: 800,
    border: "1px solid #bbf7d0",
  },
};

export default FormationsPage;
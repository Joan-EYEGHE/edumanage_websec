import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import Pagination from "../components/common/Pagination";
import SearchBar from "../components/common/SearchBar";
import Modal from "../components/common/Modal";
import FormSelect from "../components/common/FormSelect";
import FormInput from "../components/common/FormInput";
import ConfirmDialog from "../components/common/ConfirmDialog";
import RowActions from "../components/common/RowActions";
import {
  getInscriptions,
  createInscription,
  updateInscription,
  deleteInscription,
} from "../api/inscriptionsApi";
import { getUsers } from "../api/usersApi";
import { getFormations } from "../api/formationsApi";
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
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [selectedItemId, setSelectedItemId] = useState(null);
  const [editingId, setEditingId] = useState(null);

  const [apprenantOptions, setApprenantOptions] = useState([]);
  const [formationOptions, setFormationOptions] = useState([]);

  const [formData, setFormData] = useState({
    apprenantId: "",
    formationId: "",
    montant: "",
    modePaiement: "",
  });

  const canManageInscriptions = hasAnyRole(user, [
    "ADMINISTRATEUR",
    "GESTIONNAIRE",
    "FORMATEUR",
  ]);

  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "formationTitre", label: "Formation" },
    { key: "dateInscription", label: "Date inscription" },
    { key: "modePaiement", label: "Mode paiement" },
    {
      key: "actions",
      label: "Actions",
      render: (row) =>
        canManageInscriptions ? (
          <RowActions
            onEdit={() => handleEdit(row)}
            onDelete={() => handleDeleteClick(row.id)}
          />
        ) : null,
    },
  ];

  const formatDate = (value) => {
    if (!value) return "-";

    const text = String(value);

    if (text.includes("-")) return text;

    if (text.length === 8) {
      return `${text.slice(0, 4)}-${text.slice(4, 6)}-${text.slice(6, 8)}`;
    }

    return text;
  };

  const fetchInscriptions = async () => {
    setLoading(true);
    setError("");

    try {
      const result = await getInscriptions({
        page,
        size: 10,
      });

      const formattedData = result.payload.map((inscription) => ({
        id: inscription.id,
        apprenantId: inscription.apprenantId,
        formationId: inscription.formationId,
        apprenantNom: inscription.apprenantNom || "-",
        formationTitre: inscription.formationTitre || "-",
        dateInscription: formatDate(inscription.dateInscription),
        montant: inscription.montant ?? 0,
        modePaiement: inscription.modePaiement || "-",
      }));

      setInscriptions(formattedData);
      setMetadata(result.metadata);
    } catch (err) {
      setError(getReadableError(err));
    } finally {
      setLoading(false);
    }
  };

  const fetchReferenceData = async () => {
    try {
      const usersResult = await getUsers({ page: 0, size: 100 });
      const formationsResult = await getFormations({ page: 0, size: 100 });

      const apprenants = usersResult.payload
        .filter(
          (item) => Array.isArray(item.roles) && item.roles.includes("APPRENANT")
        )
        .map((item) => ({
          value: item.id,
          label: `${item.prenom} ${item.nom} (${item.email})`,
        }));

      const formations = formationsResult.payload.map((item) => ({
        value: item.id,
        label: item.titre,
      }));

      setApprenantOptions(apprenants);
      setFormationOptions(formations);
    } catch (err) {
      console.error("Erreur chargement listes :", err);
    }
  };

  useEffect(() => {
    fetchInscriptions();
  }, [page]);

  useEffect(() => {
    fetchReferenceData();
  }, []);

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
      montant: "",
      modePaiement: "",
    });
    setEditingId(null);
  };

  const handleEdit = (row) => {
    setEditingId(row.id);
    setFormData({
      apprenantId: row.apprenantId || "",
      formationId: row.formationId || "",
      montant: row.montant || "",
      modePaiement: row.modePaiement || "",
    });
    setIsModalOpen(true);
  };

  const filteredInscriptions = inscriptions.filter((item) =>
  Object.values(item).join(" ").toLowerCase().includes(search.toLowerCase())
);

  const handleDeleteClick = (id) => {
    setSelectedItemId(id);
    setIsConfirmOpen(true);
  };

  const handleConfirmDelete = async () => {
    try {
      await deleteInscription(selectedItemId);
      setSuccessMessage("Inscription supprimée avec succès.");
      setIsConfirmOpen(false);
      setSelectedItemId(null);
      await fetchInscriptions();
    } catch (err) {
      setError(getReadableError(err));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    if (!formData.apprenantId || !formData.formationId || !formData.modePaiement) {
      setError("Veuillez sélectionner un apprenant, une formation et un mode de paiement.");
      return;
    }

    const payload = {
      apprenantId: parseInt(formData.apprenantId, 10),
      formationId: parseInt(formData.formationId, 10),
      montant: formData.montant ? parseFloat(formData.montant) : 0,
      modePaiement: formData.modePaiement || null,
    };

    try {
      const response = editingId
        ? await updateInscription(editingId, payload)
        : await createInscription(payload);

      if (response?.status && response.status !== "OK") {
        setError(response.message || "Impossible d’enregistrer l’inscription.");
        return;
      }

      setSuccessMessage(
        editingId
          ? "Inscription modifiée avec succès."
          : "Inscription créée avec succès."
      );

      setIsModalOpen(false);
      resetForm();
      await fetchInscriptions();
    } catch (err) {
      console.error("Erreur inscription :", err);
      setError(getReadableError(err));
    }
  };

  return (
    <AppLayout>
      <PageHeader
        title="Inscriptions"
        subtitle="Gestion des apprenants inscrits aux formations"
        action={
          canManageInscriptions ? (
            <button
              style={styles.primaryButton}
              onClick={() => {
                resetForm();
                fetchReferenceData();
                setIsModalOpen(true);
              }}
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
          <DataTable columns={columns} data={filteredInscriptions} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title={editingId ? "Modifier une inscription" : "Créer une inscription"}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.formGrid}>
            <FormSelect
              label="Apprenant"
              name="apprenantId"
              value={formData.apprenantId}
              onChange={handleChange}
              options={apprenantOptions}
              required
            />

            <FormSelect
              label="Formation"
              name="formationId"
              value={formData.formationId}
              onChange={handleChange}
              options={formationOptions}
              required
            />

            <FormInput
              label="Montant"
              name="montant"
              type="number"
              value={formData.montant}
              onChange={handleChange}
            />

            <FormSelect
              label="Mode de paiement"
              name="modePaiement"
              value={formData.modePaiement}
              onChange={handleChange}
              required
              options={[
                { value: "WAVE", label: "WAVE" },
                { value: "ORANGE_MONEY", label: "ORANGE MONEY" },
                { value: "ESPECES", label: "ESPÈCES" },
                { value: "VIREMENT", label: "VIREMENT" },
              ]}
            />
          </div>

          <button type="submit" style={styles.primaryButtonFull}>
            {editingId ? "Mettre à jour l’inscription" : "Enregistrer l’inscription"}
          </button>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={isConfirmOpen}
        title="Supprimer l’inscription"
        message="Voulez-vous vraiment supprimer cette inscription ?"
        onConfirm={handleConfirmDelete}
        onCancel={() => setIsConfirmOpen(false)}
      />
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

export default InscriptionsPage;
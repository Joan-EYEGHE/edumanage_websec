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
import StatusBadge from "../components/common/StatusBadge";
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
        apprenantId: inscription.apprenantId,
        formationId: inscription.formationId,
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

  const fetchReferenceData = async () => {
    try {
      const usersResult = await getUsers({ page: 0, size: 100 });
      const formationsResult = await getFormations({ page: 0, size: 100 });

      const apprenants = usersResult.payload
        .filter((item) => Array.isArray(item.roles) && item.roles.includes("APPRENANT"))
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
      console.error(err);
    }
  };

  useEffect(() => {
    fetchInscriptions();
  }, [page, search]);

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
    });
    setEditingId(null);
  };

  const handleEdit = (row) => {
    setEditingId(row.id);
    setFormData({
      apprenantId: row.apprenantId || "",
      formationId: row.formationId || "",
    });
    setIsModalOpen(true);
  };

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
      fetchInscriptions();
    } catch (err) {
      setError(getReadableError(err));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    try {
      if (editingId) {
        await updateInscription(editingId, formData);
        setSuccessMessage("Inscription modifiée avec succès.");
      } else {
        await createInscription(formData);
        setSuccessMessage("Inscription créée avec succès.");
      }

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
              onClick={() => {
                resetForm();
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
          <DataTable columns={columns} data={inscriptions} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title={editingId ? "Modifier une inscription" : "Créer une inscription"}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSubmit} style={styles.form}>
          <FormSelect
            label="Apprenant"
            name="apprenantId"
            value={formData.apprenantId}
            onChange={handleChange}
            options={apprenantOptions}
          />

          <FormSelect
            label="Formation"
            name="formationId"
            value={formData.formationId}
            onChange={handleChange}
            options={formationOptions}
          />

          <button type="submit" style={styles.primaryButton}>
            {editingId ? "Mettre à jour" : "Enregistrer"}
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
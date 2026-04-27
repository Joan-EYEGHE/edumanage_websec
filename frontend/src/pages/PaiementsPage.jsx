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
import ConfirmDialog from "../components/common/ConfirmDialog";
import RowActions from "../components/common/RowActions";
import {
  getPaiements,
  createPaiement,
  updatePaiement,
  deletePaiement,
} from "../api/paiementsApi";
import { getInscriptions } from "../api/inscriptionsApi";
import { getReadableError } from "../utils/errorHandler";
import { useAuth } from "../context/AuthContext";
import { hasAnyRole } from "../utils/roles";

function PaiementsPage() {
  const { user } = useAuth();

  const [paiements, setPaiements] = useState([]);
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

  const [inscriptionOptions, setInscriptionOptions] = useState([]);

  const [formData, setFormData] = useState({
    inscriptionId: "",
    montant: "",
    modePaiement: "",
    referenceTransaction: "",
  });

  const canManagePaiements = hasAnyRole(user, [
    "ADMINISTRATEUR",
    "GESTIONNAIRE",
  ]);

  const formatDate = (value) => {
    if (!value) return "-";
    const text = String(value);

    if (text.includes("-")) return text;

    if (text.length === 8) {
      return `${text.slice(0, 4)}-${text.slice(4, 6)}-${text.slice(6, 8)}`;
    }

    return text;
  };

  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "montant", label: "Montant" },
    { key: "modePaiement", label: "Mode de paiement" },
    { key: "referenceTransaction", label: "Référence" },
    { key: "datePaiement", label: "Date" },
    {
      key: "actions",
      label: "Actions",
      render: (row) =>
        canManagePaiements ? (
          <RowActions
            onEdit={() => handleEdit(row)}
            onDelete={() => handleDeleteClick(row.id)}
          />
        ) : null,
    },
  ];

  const fetchPaiements = async () => {
    setLoading(true);
    setError("");

    try {
      const result = await getPaiements({
        page,
        size: 10,
      });

      const formattedData = result.payload.map((paiement) => ({
        id: paiement.id,
        inscriptionId: paiement.inscriptionId,
        apprenantNom: paiement.apprenantNom || "-",
        montant:
          paiement.montant !== null && paiement.montant !== undefined
            ? `${paiement.montant} FCFA`
            : "-",
        modePaiement: paiement.modePaiement || "-",
        referenceTransaction: paiement.referenceTransaction || "-",
        datePaiement: formatDate(paiement.datePaiement),
      }));

      setPaiements(formattedData);
      setMetadata(result.metadata);
    } catch (err) {
      setError(getReadableError(err));
    } finally {
      setLoading(false);
    }
  };

  const filteredPaiements = paiements.filter((item) =>
  Object.values(item).join(" ").toLowerCase().includes(search.toLowerCase())
);

  const fetchReferenceData = async () => {
    try {
      const result = await getInscriptions({ page: 0, size: 100 });

      const options = result.payload.map((item) => ({
        value: item.id,
        label: `${item.apprenantNom || "Apprenant"} - ${
          item.formationTitre || "Formation"
        }`,
      }));

      setInscriptionOptions(options);
    } catch (err) {
      console.error("Erreur chargement inscriptions :", err);
    }
  };

  useEffect(() => {
    fetchPaiements();
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
      inscriptionId: "",
      montant: "",
      modePaiement: "",
      referenceTransaction: "",
    });
    setEditingId(null);
  };

  const handleEdit = (row) => {
    setEditingId(row.id);

    setFormData({
      inscriptionId: row.inscriptionId || "",
      montant: String(row.montant || "").replace(" FCFA", ""),
      modePaiement: row.modePaiement === "-" ? "" : row.modePaiement || "",
      referenceTransaction:
        row.referenceTransaction === "-" ? "" : row.referenceTransaction || "",
    });

    setIsModalOpen(true);
  };

  const handleDeleteClick = (id) => {
    setSelectedItemId(id);
    setIsConfirmOpen(true);
  };

  const handleConfirmDelete = async () => {
    try {
      await deletePaiement(selectedItemId);
      setSuccessMessage("Paiement supprimé avec succès.");
      setIsConfirmOpen(false);
      setSelectedItemId(null);
      await fetchPaiements();
    } catch (err) {
      setError(getReadableError(err));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    if (
      !formData.inscriptionId ||
      !formData.montant ||
      !formData.modePaiement ||
      !formData.referenceTransaction.trim()
    ) {
      setError("Veuillez remplir tous les champs obligatoires du paiement.");
      return;
    }

    const payload = {
      inscriptionId: parseInt(formData.inscriptionId, 10),
      montant: parseFloat(formData.montant),
      modePaiement: formData.modePaiement,
      referenceTransaction: formData.referenceTransaction.trim(),
      datePaiement: new Date().toISOString().split("T")[0],
    };

    try {
      const response = editingId
        ? await updatePaiement(editingId, payload)
        : await createPaiement(payload);

      if (response?.status && response.status !== "OK") {
        setError(response.message || "Impossible d’enregistrer le paiement.");
        return;
      }

      setSuccessMessage(
        editingId
          ? "Paiement modifié avec succès."
          : "Paiement créé avec succès."
      );

      setIsModalOpen(false);
      resetForm();
      await fetchPaiements();
    } catch (err) {
      console.error("Erreur paiement :", err);
      setError(getReadableError(err));
    }
  };

  return (
    <AppLayout>
      <PageHeader
        title="Paiements"
        subtitle="Suivi sécurisé des transactions"
        action={
          canManagePaiements ? (
            <button
              style={styles.primaryButton}
              onClick={() => {
                resetForm();
                fetchReferenceData();
                setIsModalOpen(true);
              }}
            >
              + Nouveau paiement
            </button>
          ) : null
        }
      />

      <div style={styles.toolbar}>
        <SearchBar
          value={search}
          onChange={setSearch}
          placeholder="Rechercher un paiement..."
        />
      </div>

      {successMessage && <div style={styles.success}>{successMessage}</div>}
      {loading && <LoadingMessage message="Chargement des paiements..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && (
        <>
          <DataTable columns={columns} data={filteredPaiements} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title={editingId ? "Modifier un paiement" : "Créer un paiement"}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.formGrid}>
            <FormSelect
              label="Inscription"
              name="inscriptionId"
              value={formData.inscriptionId}
              onChange={handleChange}
              options={inscriptionOptions}
              required
            />

            <FormInput
              label="Montant"
              name="montant"
              type="number"
              value={formData.montant}
              onChange={handleChange}
              required
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

            <FormInput
              label="Référence transaction"
              name="referenceTransaction"
              value={formData.referenceTransaction}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" style={styles.primaryButtonFull}>
            {editingId ? "Mettre à jour le paiement" : "Enregistrer le paiement"}
          </button>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={isConfirmOpen}
        title="Supprimer le paiement"
        message="Voulez-vous vraiment supprimer ce paiement ?"
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

export default PaiementsPage;
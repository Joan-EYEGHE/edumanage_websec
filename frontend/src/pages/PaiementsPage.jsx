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
import { getPaiements, createPaiement } from "../api/paiementsApi";
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

  const [formData, setFormData] = useState({
    inscriptionId: "",
    montant: "",
    modePaiement: "",
    referenceTransaction: "",
  });

  const canManagePaiements = hasAnyRole(user, ["ADMIN", "GESTIONNAIRE"]);

  const columns = [
    { key: "apprenantNom", label: "Apprenant" },
    { key: "montant", label: "Montant" },
    { key: "modePaiement", label: "Mode de paiement" },
    { key: "referenceTransaction", label: "Référence" },
    {
      key: "statut",
      label: "Statut",
      render: (row) => <StatusBadge value={row.statut} />,
    },
    { key: "datePaiement", label: "Date" },
  ];

  const fetchPaiements = async () => {
    setLoading(true);
    setError("");

    try {
      const result = await getPaiements({
        page,
        size: 10,
        keyword: search,
      });

      const formattedData = result.payload.map((paiement) => ({
        id: paiement.id,
        apprenantNom: paiement.apprenantNom,
        montant: paiement.montant,
        modePaiement: paiement.modePaiement,
        referenceTransaction: paiement.referenceTransaction,
        statut: paiement.statut,
        datePaiement: paiement.datePaiement,
      }));

      setPaiements(formattedData);
      setMetadata(result.metadata);
    } catch (err) {
      setError(getReadableError(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPaiements();
  }, [page, search]);

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
  };

  const handleCreatePaiement = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    try {
      await createPaiement(formData);
      setSuccessMessage("Paiement créé avec succès.");
      setIsModalOpen(false);
      resetForm();
      fetchPaiements();
    } catch (err) {
      setError(getReadableError(err));
    }
  };

  return (
    <AppLayout>
      <PageHeader
        title="Paiements"
        subtitle="Suivi des transactions et paiements"
        action={
          canManagePaiements ? (
            <button
              style={styles.primaryButton}
              onClick={() => setIsModalOpen(true)}
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
          <DataTable columns={columns} data={paiements} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title="Créer un paiement"
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleCreatePaiement} style={styles.form}>
          <FormInput
            label="Inscription ID"
            name="inscriptionId"
            value={formData.inscriptionId}
            onChange={handleChange}
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
            options={[
              { value: "WAVE", label: "WAVE" },
              { value: "ORANGE_MONEY", label: "ORANGE_MONEY" },
              { value: "ESPECES", label: "ESPECES" },
              { value: "VIREMENT", label: "VIREMENT" },
            ]}
          />

          <FormInput
            label="Référence transaction"
            name="referenceTransaction"
            value={formData.referenceTransaction}
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

export default PaiementsPage;
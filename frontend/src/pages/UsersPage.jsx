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
import { getUsers, createUser } from "../api/usersApi";
import { getReadableError } from "../utils/errorHandler";
import { useAuth } from "../context/AuthContext";
import { hasAnyRole } from "../utils/roles";

function UsersPage() {
  const { user } = useAuth();

  const [users, setUsers] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [formData, setFormData] = useState({
    nom: "",
    prenom: "",
    email: "",
    telephone: "",
    password: "",
    role: "",
  });

  const canManageUsers = hasAnyRole(user, ["ADMIN", "GESTIONNAIRE"]);

  const columns = [
    { key: "nom", label: "Nom" },
    { key: "prenom", label: "Prénom" },
    { key: "email", label: "Email" },
    { key: "telephone", label: "Téléphone" },
    { key: "roles", label: "Rôle" },
    {
      key: "actif",
      label: "Statut",
      render: (row) => <StatusBadge value={row.actif ? "ACTIF" : "INACTIF"} />,
    },
  ];

  const fetchUsers = async () => {
    setLoading(true);
    setError("");

    try {
      const result = await getUsers({
        page,
        size: 10,
        keyword: search,
      });

      const formattedData = result.payload.map((userItem) => ({
        id: userItem.id,
        nom: userItem.nom,
        prenom: userItem.prenom,
        email: userItem.email,
        telephone: userItem.telephone,
        roles: Array.isArray(userItem.roles) ? userItem.roles.join(", ") : "",
        actif: userItem.actif,
      }));

      setUsers(formattedData);
      setMetadata(result.metadata);
    } catch (err) {
      setError(getReadableError(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, [page, search]);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const resetForm = () => {
    setFormData({
      nom: "",
      prenom: "",
      email: "",
      telephone: "",
      password: "",
      role: "",
    });
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    try {
      await createUser(formData);
      setSuccessMessage("Utilisateur créé avec succès.");
      setIsModalOpen(false);
      resetForm();
      fetchUsers();
    } catch (err) {
      setError(getReadableError(err));
    }
  };

  return (
    <AppLayout>
      <PageHeader
        title="Utilisateurs"
        subtitle="Liste des comptes utilisateurs"
        action={
          canManageUsers ? (
            <button style={styles.primaryButton} onClick={() => setIsModalOpen(true)}>
              + Nouvel utilisateur
            </button>
          ) : null
        }
      />

      <div style={styles.toolbar}>
        <SearchBar
          value={search}
          onChange={setSearch}
          placeholder="Rechercher un utilisateur..."
        />
      </div>

      {successMessage && <div style={styles.success}>{successMessage}</div>}
      {loading && <LoadingMessage message="Chargement des utilisateurs..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && (
        <>
          <DataTable columns={columns} data={users} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title="Créer un utilisateur"
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleCreateUser} style={styles.form}>
          <FormInput label="Nom" name="nom" value={formData.nom} onChange={handleChange} />
          <FormInput label="Prénom" name="prenom" value={formData.prenom} onChange={handleChange} />
          <FormInput label="Email" name="email" type="email" value={formData.email} onChange={handleChange} />
          <FormInput label="Téléphone" name="telephone" value={formData.telephone} onChange={handleChange} />
          <FormInput label="Mot de passe" name="password" type="password" value={formData.password} onChange={handleChange} />
          <FormSelect
            label="Rôle"
            name="role"
            value={formData.role}
            onChange={handleChange}
            options={[
              { value: "ADMIN", label: "ADMIN" },
              { value: "GESTIONNAIRE", label: "GESTIONNAIRE" },
              { value: "FORMATEUR", label: "FORMATEUR" },
              { value: "APPRENANT", label: "APPRENANT" },
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
    display: "flex",
    justifyContent: "space-between",
    gap: "1rem",
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

export default UsersPage;
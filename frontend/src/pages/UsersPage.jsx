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
    roles: [],
  });

  const canManageUsers = hasAnyRole(user, ["ADMINISTRATEUR", "GESTIONNAIRE"]);

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
      });

      const formattedData = result.payload.map((userItem) => ({
        id: userItem.id,
        nom: userItem.nom || "-",
        prenom: userItem.prenom || "-",
        email: userItem.email || "-",
        telephone: userItem.telephone || "-",
        roles: Array.isArray(userItem.roles) ? userItem.roles.join(", ") : "-",
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
  }, [page]);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "roles") {
      setFormData((prev) => ({
        ...prev,
        roles: value ? [value] : [],
      }));
      return;
    }

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const resetForm = () => {
    setFormData({
      nom: "",
      prenom: "",
      email: "",
      telephone: "",
      password: "",
      roles: [],
    });
  };

   const filteredUsers = users.filter((item) =>
  Object.values(item).join(" ").toLowerCase().includes(search.toLowerCase())
);

  const handleCreateUser = async (e) => {
  e.preventDefault();
  setError("");
  setSuccessMessage("");

  if (
    !formData.nom.trim() ||
    !formData.prenom.trim() ||
    !formData.email.trim() ||
    !formData.telephone.trim() ||
    !formData.password.trim() ||
    formData.roles.length === 0
  ) {
    setError("Veuillez remplir tous les champs obligatoires.");
    return;
  }

  const payload = {
    nom: formData.nom.trim(),
    prenom: formData.prenom.trim(),
    email: formData.email.trim(),
    telephone: formData.telephone.trim(),
    password: formData.password,
    roles: formData.roles,
    actif: true,
    statut: "ACTIF",
  };

 

  try {
    const response = await createUser(payload);

    if (response?.status && response.status !== "OK") {
      setError(response.message || "Impossible de créer l’utilisateur.");
      return;
    }

    setSuccessMessage("Utilisateur créé avec succès.");
    setIsModalOpen(false);
    resetForm();
    await fetchUsers();
  } catch (err) {
    console.error("Erreur création utilisateur :", err);
    setError(getReadableError(err));
  }
};

  return (
    <AppLayout>
      <PageHeader
        title="Utilisateurs"
        subtitle="Gestion des comptes et rôles utilisateurs"
        action={
          canManageUsers ? (
            <button
              style={styles.primaryButton}
              onClick={() => {
                resetForm();
                setIsModalOpen(true);
              }}
            >
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
          <DataTable columns={columns} data={filteredUsers} />
          <Pagination metadata={metadata} onPageChange={setPage} />
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        title="Créer un utilisateur"
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleCreateUser} style={styles.form}>
          <div style={styles.formGrid}>
            <FormInput
              label="Nom"
              name="nom"
              value={formData.nom}
              onChange={handleChange}
              required
            />

            <FormInput
              label="Prénom"
              name="prenom"
              value={formData.prenom}
              onChange={handleChange}
              required
            />

            <FormInput
              label="Email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
            />

            <FormInput
              label="Téléphone"
              name="telephone"
              value={formData.telephone}
              onChange={handleChange}
              required
            />

            <FormInput
              label="Mot de passe"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              required
            />

            <FormSelect
              label="Rôle"
              name="roles"
              value={formData.roles?.[0] || ""}
              onChange={handleChange}
              required
              options={[
                { value: "FORMATEUR", label: "FORMATEUR" },
                { value: "APPRENANT", label: "APPRENANT" },
              ]}
            />
          </div>

          <button type="submit" style={styles.primaryButtonFull}>
            Enregistrer l’utilisateur
          </button>
        </form>
      </Modal>
    </AppLayout>
  );
}

const styles = {
  toolbar: {
    marginBottom: "1.2rem",
    display: "flex",
    justifyContent: "space-between",
    gap: "1rem",
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

export default UsersPage;
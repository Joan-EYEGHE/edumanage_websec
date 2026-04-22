import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import PageHeader from "../components/common/PageHeader";
import DataTable from "../components/common/DataTable";
import LoadingMessage from "../components/common/LoadingMessage";
import ErrorMessage from "../components/common/ErrorMessage";
import { getUsers } from "../api/usersApi";

function UsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const columns = [
    { key: "nom", label: "Nom" },
    { key: "prenom", label: "Prénom" },
    { key: "email", label: "Email" },
    { key: "telephone", label: "Téléphone" },
    { key: "roles", label: "Rôle" },
    { key: "actif", label: "Actif" },
  ];

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const data = await getUsers();

        const formattedData = data.map((user) => ({
          id: user.id,
          nom: user.nom,
          prenom: user.prenom,
          email: user.email,
          telephone: user.telephone,
          roles: Array.isArray(user.roles) ? user.roles.join(", ") : "",
          actif: user.actif ? "Oui" : "Non",
        }));

        setUsers(formattedData);
      } catch (err) {
        console.error(err);
        setError("Impossible de charger les utilisateurs.");
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  return (
    <AppLayout>
      <PageHeader
        title="Utilisateurs"
        subtitle="Liste des comptes utilisateurs"
      />

      {loading && <LoadingMessage message="Chargement des utilisateurs..." />}
      {error && <ErrorMessage message={error} />}

      {!loading && !error && <DataTable columns={columns} data={users} />}
    </AppLayout>
  );
}

export default UsersPage;
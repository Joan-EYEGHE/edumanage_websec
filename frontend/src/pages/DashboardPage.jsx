import AppLayout from "../components/layout/AppLayout";
import StatCard from "../components/common/StatCard";
import PageHeader from "../components/common/PageHeader";

function DashboardPage() {
  return (
    <AppLayout>
      <PageHeader
        title="Dashboard"
        subtitle="Vue d’ensemble de l’application"
      />

      <div style={styles.grid}>
        <StatCard title="Utilisateurs" value="12" color="#2563eb" />
        <StatCard title="Formations" value="6" color="#16a34a" />
        <StatCard title="Inscriptions" value="18" color="#f59e0b" />
        <StatCard title="Paiements" value="10" color="#dc2626" />
      </div>
    </AppLayout>
  );
}

const styles = {
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
    gap: "1rem",
  },
};

export default DashboardPage;
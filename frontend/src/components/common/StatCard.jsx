function StatCard({ title, value, color = "#2563eb" }) {
  return (
    <div style={{ ...styles.card, borderLeft: `6px solid ${color}` }}>
      <p style={styles.title}>{title}</p>
      <h2 style={styles.value}>{value}</h2>
    </div>
  );
}

const styles = {
  card: {
    backgroundColor: "#fff",
    borderRadius: "12px",
    padding: "1.2rem",
    boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
  },
  title: {
    color: "#6b7280",
    marginBottom: "0.5rem",
  },
  value: {
    color: "#111827",
    fontSize: "1.7rem",
  },
};

export default StatCard;
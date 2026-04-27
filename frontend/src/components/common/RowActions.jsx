function RowActions({ onEdit, onDelete }) {
  return (
    <div style={styles.container}>
      <button style={styles.edit} onClick={onEdit} onMouseOver={(e) => (e.currentTarget.style.transform = "scale(1.05)")}>
        ✏️ Modifier
      </button>

      <button style={styles.delete} onClick={onDelete} onMouseOut={(e) => (e.currentTarget.style.transform = "scale(1)")}>
        🗑️ Supprimer
      </button>
    </div>
  );
}

const baseButton = {
  padding: "0.5rem 0.8rem",
  borderRadius: "10px",
  border: "none",
  fontSize: "0.85rem",
  fontWeight: 800,
  display: "flex",
  alignItems: "center",
  gap: "6px",
  cursor: "pointer",
  transition: "all 0.2s ease",
  
};

const styles = {
  container: {
    display: "flex",
    gap: "0.5rem",
  },

  edit: {
  ...baseButton,
  background: "linear-gradient(135deg, #00798f, #005f70)",
  color: "#fff",
  boxShadow: "0 8px 18px rgba(0,121,143,0.25)",
},
delete: {
  ...baseButton,
  background: "linear-gradient(135deg, #dc2626, #b91c1c)",
  color: "#fff",
  boxShadow: "0 8px 18px rgba(220,38,38,0.25)",
},
};

export default RowActions;
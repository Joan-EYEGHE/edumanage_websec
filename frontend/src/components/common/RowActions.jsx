function RowActions({ onEdit, onDelete, canEdit = true, canDelete = true }) {
  return (
    <div style={styles.wrapper}>
      {canEdit && (
        <button style={styles.editButton} onClick={onEdit}>
          Modifier
        </button>
      )}
      {canDelete && (
        <button style={styles.deleteButton} onClick={onDelete}>
          Supprimer
        </button>
      )}
    </div>
  );
}

const styles = {
  wrapper: {
    display: "flex",
    gap: "0.5rem",
    flexWrap: "wrap",
  },
  editButton: {
    backgroundColor: "#2563eb",
    color: "#fff",
    border: "none",
    padding: "0.45rem 0.7rem",
    borderRadius: "6px",
    fontSize: "0.82rem",
  },
  deleteButton: {
    backgroundColor: "#dc2626",
    color: "#fff",
    border: "none",
    padding: "0.45rem 0.7rem",
    borderRadius: "6px",
    fontSize: "0.82rem",
  },
};

export default RowActions;
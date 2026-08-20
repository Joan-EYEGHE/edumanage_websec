function ConfirmDialog({
  isOpen,
  title = "Confirmation",
  message = "Voulez-vous vraiment continuer ?",
  onConfirm,
  onCancel,
}) {
  if (!isOpen) return null;

  return (
    <div style={styles.overlay}>
      <div style={styles.dialog}>
        <h3 style={styles.title}>{title}</h3>
        <p style={styles.message}>{message}</p>

        <div style={styles.actions}>
          <button style={styles.cancelButton} onClick={onCancel}>
            Annuler
          </button>
          <button style={styles.confirmButton} onClick={onConfirm}>
            Confirmer
          </button>
        </div>
      </div>
    </div>
  );
}

const styles = {
  overlay: {
    position: "fixed",
    inset: 0,
    backgroundColor: "rgba(0,0,0,0.35)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    padding: "1rem",
    zIndex: 1100,
  },
  dialog: {
    backgroundColor: "#fff",
    borderRadius: "12px",
    padding: "1.2rem",
    width: "100%",
    maxWidth: "360px",
    boxShadow: "0 12px 30px rgba(0,0,0,0.18)",
  },
  title: {
    marginBottom: "0.6rem",
    fontSize: "1rem",
  },
  message: {
    color: "#4b5563",
    marginBottom: "1rem",
    fontSize: "0.92rem",
  },
  actions: {
    display: "flex",
    justifyContent: "flex-end",
    gap: "0.75rem",
  },
  cancelButton: {
    backgroundColor: "#e5e7eb",
    border: "none",
    padding: "0.65rem 0.9rem",
    borderRadius: "8px",
    fontSize: "0.88rem",
  },
  confirmButton: {
    backgroundColor: "#dc2626",
    color: "#fff",
    border: "none",
    padding: "0.65rem 0.9rem",
    borderRadius: "8px",
    fontSize: "0.88rem",
  },
};

export default ConfirmDialog;
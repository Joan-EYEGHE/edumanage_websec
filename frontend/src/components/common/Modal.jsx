function Modal({ isOpen, title, onClose, children }) {
  if (!isOpen) return null;

  return (
    <div style={styles.overlay}>
      <div style={styles.modal}>
        <div style={styles.header}>
          <h2 style={styles.title}>{title}</h2>
          <button onClick={onClose} style={styles.closeButton}>
            ×
          </button>
        </div>
        <div style={styles.content}>{children}</div>
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
    zIndex: 1000,
  },
  modal: {
    width: "100%",
    maxWidth: "420px",
    maxHeight: "85vh",
    overflowY: "auto",
    backgroundColor: "#fff",
    borderRadius: "12px",
    padding: "1rem 1rem 1.2rem",
    boxShadow: "0 12px 30px rgba(0,0,0,0.18)",
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "0.8rem",
  },
  title: {
    fontSize: "1rem",
    margin: 0,
    color: "#111827",
  },
  closeButton: {
    background: "#f3f4f6",
    border: "none",
    width: "30px",
    height: "30px",
    borderRadius: "50%",
    fontSize: "1rem",
    fontWeight: "bold",
    cursor: "pointer",
    color: "#374151",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    lineHeight: 1,
  },
  content: {
    marginTop: "0.4rem",
  },
};

export default Modal;
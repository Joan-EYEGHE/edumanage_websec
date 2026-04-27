function Modal({ isOpen, title, onClose, children }) {
  if (!isOpen) return null;

  return (
    <div style={styles.overlay}>
      <div style={styles.modal}>
        <div style={styles.header}>
          <div>
            <p style={styles.kicker}>EduManage</p>
            <h2 style={styles.title}>{title}</h2>
          </div>

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
    backgroundColor: "rgba(0, 38, 45, 0.42)",
    backdropFilter: "blur(6px)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    padding: "1rem",
    zIndex: 1000,
  },
  modal: {
    width: "100%",
    maxWidth: "760px",
    maxHeight: "none",
    backgroundColor: "rgba(255,255,255,0.98)",
    borderRadius: "26px",
    padding: "1.5rem",
    border: "1px solid rgba(0,121,143,0.14)",
    boxShadow: "0 30px 80px rgba(0,121,143,0.24)",
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "flex-start",
    marginBottom: "1rem",
  },
  kicker: {
    color: "#00798f",
    fontSize: "0.75rem",
    fontWeight: 900,
    letterSpacing: "1.4px",
    textTransform: "uppercase",
    marginBottom: "0.2rem",
  },
  title: {
    fontSize: "1.35rem",
    margin: 0,
    color: "#102a30",
  },
  closeButton: {
    background: "#f5feff",
    border: "1px solid rgba(0,121,143,0.16)",
    width: "38px",
    height: "38px",
    borderRadius: "14px",
    fontSize: "1.3rem",
    fontWeight: "bold",
    color: "#00798f",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    lineHeight: 1,
  },
  content: {
    marginTop: "0.5rem",
  },
};

export default Modal;
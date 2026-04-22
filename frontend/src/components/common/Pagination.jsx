function Pagination({ metadata, onPageChange }) {
  if (!metadata || metadata.totalPages <= 1) return null;

  const currentPage = metadata.number || 0;
  const totalPages = metadata.totalPages || 0;

  return (
    <div style={styles.wrapper}>
      <button
        style={styles.button}
        disabled={currentPage === 0}
        onClick={() => onPageChange(currentPage - 1)}
      >
        Précédent
      </button>

      <span style={styles.info}>
        Page {currentPage + 1} sur {totalPages}
      </span>

      <button
        style={styles.button}
        disabled={currentPage + 1 >= totalPages}
        onClick={() => onPageChange(currentPage + 1)}
      >
        Suivant
      </button>
    </div>
  );
}

const styles = {
  wrapper: {
    marginTop: "1rem",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    gap: "1rem",
  },
  button: {
    padding: "0.6rem 1rem",
    borderRadius: "8px",
    border: "1px solid #d1d5db",
    backgroundColor: "#fff",
  },
  info: {
    color: "#374151",
  },
};

export default Pagination;
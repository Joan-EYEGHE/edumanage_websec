function ErrorMessage({ message = "Une erreur est survenue." }) {
  return (
    <div style={styles.box}>
      <p>{message}</p>
    </div>
  );
}

const styles = {
  box: {
    backgroundColor: "#fee2e2",
    color: "#991b1b",
    padding: "1rem",
    borderRadius: "10px",
    marginBottom: "1rem",
    border: "1px solid #fecaca",
  },
};

export default ErrorMessage;
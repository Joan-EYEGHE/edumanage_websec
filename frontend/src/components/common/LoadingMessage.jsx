function LoadingMessage({ message = "Chargement..." }) {
  return (
    <div style={styles.box}>
      <p>{message}</p>
    </div>
  );
}

const styles = {
  box: {
    backgroundColor: "#fff",
    padding: "1rem",
    borderRadius: "10px",
    marginBottom: "1rem",
    boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
  },
};

export default LoadingMessage;
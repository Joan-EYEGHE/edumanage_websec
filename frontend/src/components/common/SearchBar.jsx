function SearchBar({ value, onChange, placeholder = "Rechercher..." }) {
  return (
    <input
      type="text"
      value={value}
      onChange={(e) => onChange(e.target.value)}
      placeholder={placeholder}
      style={styles.input}
    />
  );
}

const styles = {
  input: {
    width: "100%",
    maxWidth: "320px",
    padding: "0.8rem 1rem",
    borderRadius: "8px",
    border: "1px solid #d1d5db",
    backgroundColor: "#fff",
  },
};

export default SearchBar;
function FormSelect({ label, name, value, onChange, options = [] }) {
  return (
    <div style={styles.group}>
      <label style={styles.label}>{label}</label>
      <select style={styles.select} name={name} value={value} onChange={onChange}>
        <option value="">Sélectionner</option>
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </div>
  );
}

const styles = {
  group: {
    display: "flex",
    flexDirection: "column",
    gap: "0.3rem",
  },
  label: {
    fontWeight: 600,
    color: "#374151",
    fontSize: "0.85rem",
  },
  select: {
    padding: "0.65rem 0.75rem",
    borderRadius: "8px",
    border: "1px solid #d1d5db",
    backgroundColor: "#fff",
    fontSize: "0.9rem",
    height: "38px",
  },
};

export default FormSelect;
function FormSelect({
  label,
  name,
  value,
  onChange,
  options = [],
  required = false,
}) {
  return (
    <div style={styles.group}>
      <label style={styles.label}>
        {label} {required && <span style={styles.required}>*</span>}
      </label>

      <select
        style={styles.select}
        name={name}
        value={value}
        onChange={onChange}
        required={required}
      >
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
    gap: "0.45rem",
  },
  label: {
    fontWeight: 800,
    color: "#24464d",
    fontSize: "0.9rem",
  },
  required: {
    color: "#dc2626",
  },
  select: {
    padding: "0.85rem 0.95rem",
    borderRadius: "14px",
    border: "1px solid rgba(0,121,143,0.18)",
    backgroundColor: "#f9feff",
    color: "#102a30",
    fontSize: "0.95rem",
    outline: "none",
  },
};

export default FormSelect;
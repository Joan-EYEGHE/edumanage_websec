function FormInput({
  label,
  name,
  type = "text",
  value,
  onChange,
  placeholder = "",
  required = false,
}) {
  return (
    <div style={styles.group}>
      <label style={styles.label}>
        {label} {required && <span style={styles.required}>*</span>}
      </label>

      <input
        style={styles.input}
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
      />
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
  input: {
    padding: "0.85rem 0.95rem",
    borderRadius: "14px",
    border: "1px solid rgba(0,121,143,0.18)",
    fontSize: "0.95rem",
    backgroundColor: "#f9feff",
    color: "#102a30",
    outline: "none",
  },
};

export default FormInput;
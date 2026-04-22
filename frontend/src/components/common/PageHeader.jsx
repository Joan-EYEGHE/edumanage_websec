function PageHeader({ title, subtitle, action }) {
  return (
    <div style={styles.wrapper}>
      <div>
        <h1 style={styles.title}>{title}</h1>
        {subtitle && <p style={styles.subtitle}>{subtitle}</p>}
      </div>
      {action && <div>{action}</div>}
    </div>
  );
}

const styles = {
  wrapper: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "1.5rem",
  },
  title: {
    fontSize: "1.6rem",
    color: "#111827",
    marginBottom: "0.3rem",
  },
  subtitle: {
    color: "#6b7280",
  },
};

export default PageHeader;
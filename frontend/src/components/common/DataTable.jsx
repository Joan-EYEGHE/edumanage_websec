function DataTable({ columns, data }) {
  return (
    <div style={styles.wrapper}>
      <div style={styles.scroll}>
        <table style={styles.table}>
          <thead>
            <tr>
              {columns.map((col) => (
                <th key={col.key} style={styles.th}>
                  {col.label}
                </th>
              ))}
            </tr>
          </thead>

          <tbody>
            {data.length > 0 ? (
              data.map((row, index) => (
                <tr key={row.id || index} style={styles.tr}>
                  {columns.map((col) => (
                    <td key={col.key} style={styles.td}>
                      {col.render ? col.render(row) : row[col.key]}
                    </td>
                  ))}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={columns.length} style={styles.empty}>
                  <div style={styles.emptyIcon}>📭</div>
                  <strong>Aucune donnée disponible</strong>
                  <span>Aucun élément n’a encore été enregistré.</span>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

const styles = {
  wrapper: {
    backgroundColor: "#fff",
    borderRadius: "24px",
    overflow: "hidden",
    border: "1px solid rgba(0,121,143,0.12)",
    boxShadow: "0 14px 34px rgba(16,42,48,0.08)",
  },
  scroll: {
    width: "100%",
    overflowX: "auto",
  },
  table: {
    width: "100%",
    borderCollapse: "separate",
    borderSpacing: 0,
    minWidth: "760px",
  },
  th: {
    textAlign: "left",
    padding: "1rem 1.1rem",
    backgroundColor: "#f5feff",
    borderBottom: "1px solid rgba(0,121,143,0.12)",
    color: "#00798f",
    fontSize: "0.85rem",
    textTransform: "uppercase",
    letterSpacing: "0.6px",
    fontWeight: 900,
  },
  tr: {
    transition: "background 0.2s ease",
  },
  td: {
    padding: "1rem 1.1rem",
    borderBottom: "1px solid rgba(0,121,143,0.08)",
    verticalAlign: "middle",
    color: "#24464d",
    fontSize: "0.95rem",
    fontWeight: 500,
  },
  empty: {
    textAlign: "center",
    padding: "3rem 1rem",
    color: "#6b7c80",
  },
  emptyIcon: {
    fontSize: "2rem",
    marginBottom: "0.7rem",
  },
};

export default DataTable;
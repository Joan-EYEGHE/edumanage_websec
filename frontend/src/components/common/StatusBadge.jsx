function StatusBadge({ value }) {
  const normalized = String(value || "").toUpperCase();

  const colorMap = {
    ACTIVE: { bg: "#dcfce7", text: "#166534" },
    ACTIF: { bg: "#dcfce7", text: "#166534" },
    INACTIVE: { bg: "#fee2e2", text: "#991b1b" },
    INACTIF: { bg: "#fee2e2", text: "#991b1b" },
    PUBLIEE: { bg: "#dbeafe", text: "#1d4ed8" },
    BROUILLON: { bg: "#f3f4f6", text: "#374151" },
    TERMINEE: { bg: "#ede9fe", text: "#6d28d9" },
    ANNULEE: { bg: "#fee2e2", text: "#991b1b" },
    VALIDEE: { bg: "#dcfce7", text: "#166534" },
    EN_ATTENTE: { bg: "#fef3c7", text: "#92400e" },
    PAYE: { bg: "#dcfce7", text: "#166534" },
    ECHOUE: { bg: "#fee2e2", text: "#991b1b" },
    REMBOURSE: { bg: "#e0f2fe", text: "#075985" },
  };

  const style = colorMap[normalized] || {
    bg: "#f3f4f6",
    text: "#374151",
  };

  return (
    <span
      style={{
        backgroundColor: style.bg,
        color: style.text,
        padding: "0.35rem 0.7rem",
        borderRadius: "999px",
        fontSize: "0.8rem",
        fontWeight: 600,
      }}
    >
      {value}
    </span>
  );
}

export default StatusBadge;
import Sidebar from "./Sidebar";
import Topbar from "./Topbar";

function AppLayout({ children }) {
  return (
    <div style={styles.wrapper}>
      <Sidebar />
      <div style={styles.mainArea}>
        <Topbar />
        <main style={styles.content}>{children}</main>
      </div>
    </div>
  );
}

const styles = {
  wrapper: {
    display: "flex",
    minHeight: "100vh",
    backgroundColor: "#f3f4f6",
  },
  mainArea: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
  },
  content: {
    padding: "1.5rem",
  },
};

export default AppLayout;
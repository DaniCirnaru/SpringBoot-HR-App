function CandidatePage({ onLogout }) {
  return (
    <div style={{ padding: "2rem" }}>
      <h2>✅ Logged in successfully!</h2>
      <button onClick={onLogout}>Logout</button>
    </div>
  );
}

export default CandidatePage;

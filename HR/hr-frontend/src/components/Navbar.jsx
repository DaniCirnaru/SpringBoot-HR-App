import { Link, useNavigate } from "react-router-dom";
import { getRoleFromToken } from "../utils/jwt";

function Navbar({ token, onLogout }) {
  const role = getRoleFromToken(token);
  const navigate = useNavigate();

  const handleLogout = () => {
    onLogout(); // call the App-level logout
    navigate("/login"); // redirect after logout
  };

  return (
    <nav style={{ padding: "1rem", borderBottom: "1px solid #ccc" }}>
      {role === "ADMIN" && (
        <>
          {" | "}
          <Link to="/admin/interviews">📅 Manage Interviews</Link>
          {" | "}
          <Link to="/candidates">👥 Candidates</Link>
        </>
      )}

      {role === "EMPLOYEE" && (
        <>
          {" | "}
          <Link to="/assigned-interviews">💼 Assigned Interviews</Link>
        </>
      )}

      {role === "CANDIDATE" && (
        <>
          {" | "}
          <Link to="/candidate-dashboard">🧑 Candidate Dashboard</Link>
          {" | "}
          <Link to="/my-interviews">🎯 My Interviews</Link>
        </>
      )}

      {" | "}
      <Link to="/profile">🙋 Profile</Link>
      {" | "}
      <button onClick={handleLogout} style={{ marginLeft: "10px" }}>
        🚪 Logout
      </button>
    </nav>
  );
}

export default Navbar;

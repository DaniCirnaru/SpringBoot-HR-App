import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function EditCandidatePage() {
  const [candidate, setCandidate] = useState(null);
  const [name, setName] = useState("");
  const [position, setPosition] = useState("");
  const [error, setError] = useState("");
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8081/api/candidates/me", {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch candidate");
        return res.json();
      })
      .then((data) => {
        setCandidate(data);
        setName(data.name);
        setPosition(data.position);
      })
      .catch((err) => {
        console.error(err);
        setError("Could not load candidate data.");
      });
  }, [token]);

  const handleSave = () => {
    if (!candidate) return;

    fetch(`http://localhost:8081/api/candidates/${candidate.id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
        "X-Gateway-Auth": "true",
      },
      body: JSON.stringify({
        name,
        position,
        userId: candidate.userId,
        cvData: null,
      }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to update candidate");
        return res.json();
      })
      .then(() => {
        navigate("/profile");
      })
      .catch((err) => {
        console.error(err);
        setError("Update failed.");
      });
  };

  if (!candidate) return <p>Loading...</p>;

  return (
    <div style={{ padding: "1rem" }}>
      <h2>✏️ Edit Candidate Info</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <p>
        <label>Name: </label>
        <input value={name} onChange={(e) => setName(e.target.value)} />
      </p>
      <p>
        <label>Position: </label>
        <input value={position} onChange={(e) => setPosition(e.target.value)} />
      </p>
      <button onClick={handleSave}>💾 Save</button>{" "}
      <button onClick={() => navigate("/profile")}>Cancel</button>
    </div>
  );
}

export default EditCandidatePage;

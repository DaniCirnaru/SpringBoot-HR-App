import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function ProfilePage() {
  const [user, setUser] = useState(null);
  const [candidate, setCandidate] = useState(null);
  const [cvFile, setCvFile] = useState(null);
  const [uploadMessage, setUploadMessage] = useState("");
  const [isEditingSkills, setIsEditingSkills] = useState(false);
  const [newSkills, setNewSkills] = useState([]);
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8081/api/users/me", {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch user");
        return res.json();
      })
      .then((data) => {
        setUser(data);
        if (data.role.name === "CANDIDATE") {
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
            .then((cand) => {
              setCandidate(cand);
              setNewSkills(cand.recognizedSkills || []);
            })
            .catch((err) => console.error(err));
        }
      })
      .catch((err) => console.error(err));
  }, [token]);

  const handleFileChange = (e) => {
    setCvFile(e.target.files[0]);
  };

  const handleUpload = async () => {
  if (!cvFile || !candidate?.id) {
    alert("Please select a file first.");
    return;
  }

  const formData = new FormData();
  formData.append("file", cvFile);

  try {
    const res = await fetch(`http://localhost:8081/api/candidates/${candidate.id}/upload-cv`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
      body: formData,
    });

    if (!res.ok) throw new Error("Upload failed");
    setUploadMessage("✅ CV uploaded successfully!");
    setCvFile(null);

    // ✅ REFETCH candidate after upload
    const updated = await fetch("http://localhost:8081/api/candidates/me", {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
    });

    if (!updated.ok) throw new Error("Failed to refresh candidate data");
    const updatedCandidate = await updated.json();
    setCandidate(updatedCandidate);
    setNewSkills(updatedCandidate.recognizedSkills || []);

  } catch (err) {
    console.error(err);
    setUploadMessage("❌ CV upload failed.");
  }
};


  const handleSkillChange = (e, index) => {
    const updated = [...newSkills];
    updated[index] = e.target.value;
    setNewSkills(updated);
  };

  const handleAddSkill = () => {
    setNewSkills([...newSkills, ""]);
  };

  const handleRemoveSkill = (index) => {
    setNewSkills(newSkills.filter((_, i) => i !== index));
  };

  const saveSkills = async () => {
  try {
    const res = await fetch(`http://localhost:8081/api/candidates/${candidate.id}/skills`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
      body: JSON.stringify({ recognizedSkills: newSkills.filter(s => s.trim() !== "") }),
    });

    if (!res.ok) throw new Error("Failed to update skills");

    const updated = await res.json();
    setCandidate(updated);
    setIsEditingSkills(false);
  } catch (err) {
    console.error(err);
  }
};


  if (!user) return <p>Loading profile...</p>;

  return (
    <div style={{ padding: "1rem" }}>
      <h2>👤 Profile</h2>
      <p><strong>Username:</strong> {user.username}</p>
      <p><strong>Email:</strong> {user.email}</p>
      <p><strong>Role:</strong> {user.role.name}</p>

      <button onClick={() => navigate("/edit-user")}>⚙️ Edit User Info</button>

      {candidate && (
        <div style={{ marginTop: "2rem" }}>
          <h3>🎯 Candidate Info</h3>
          <p><strong>Name:</strong> {candidate.name}</p>
          <p><strong>Position:</strong> {candidate.position}</p>
          <p><strong>CV Status:</strong> {candidate.cvParseStatus || "📭 Not uploaded"}</p>

          {/* ✅ Skills Section */}
          <div style={{ marginTop: "1rem" }}>
            <p><strong>Skills:</strong></p>
            {!isEditingSkills ? (
              <>
                <ul>
                  {candidate.recognizedSkills?.map((skill, idx) => (
                    <li key={idx}>🛠 {skill}</li>
                  ))}
                </ul>
                <button onClick={() => setIsEditingSkills(true)}>✏️ Edit Skills</button>
              </>
            ) : (
              <>
                {newSkills.map((skill, idx) => (
                  <div key={idx}>
                    <input
                      type="text"
                      value={skill}
                      onChange={(e) => handleSkillChange(e, idx)}
                      placeholder={`Skill ${idx + 1}`}
                    />
                    <button onClick={() => handleRemoveSkill(idx)}>❌</button>
                  </div>
                ))}
                <button onClick={handleAddSkill}>➕ Add Skill</button>
                <button onClick={saveSkills}>💾 Save Skills</button>
              </>
            )}
          </div>

          <button onClick={() => navigate("/edit-candidate")} style={{ marginTop: "1rem" }}>
            ✏️ Edit Candidate Info
          </button>

          <div style={{ marginTop: "2rem" }}>
            <h4>📄 CV Upload</h4>
            <input type="file" accept=".pdf" onChange={handleFileChange} />
            <button onClick={handleUpload} style={{ marginLeft: "10px" }}>
              ⬆️ Upload CV
            </button>
            {uploadMessage && <p style={{ marginTop: "10px" }}>{uploadMessage}</p>}
          </div>
        </div>
      )}
    </div>
  );
}

export default ProfilePage;

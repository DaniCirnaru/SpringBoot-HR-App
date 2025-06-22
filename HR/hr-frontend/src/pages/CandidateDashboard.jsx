import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function CandidateDashboard({ token }) {
  const [candidate, setCandidate] = useState(null);
  const [interviews, setInterviews] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch candidate info
    fetch("http://localhost:8081/api/candidates/me", {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
    })
      .then((res) => res.json())
      .then(setCandidate)
      .catch((err) => console.error("❌ Failed to fetch candidate", err));

    // Fetch interviews
    fetch("http://localhost:8081/api/interviews/my", {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
    })
      .then((res) => res.json())
      .then(setInterviews)
      .catch((err) => console.error("❌ Failed to fetch interviews", err));
  }, [token]);

  // Helper: Get the next upcoming interview
  const getNextInterview = () => {
    const now = new Date();
    const upcoming = interviews
      .filter((i) => new Date(i.scheduledAt) > now)
      .sort((a, b) => new Date(a.scheduledAt) - new Date(b.scheduledAt));
    return upcoming.length > 0 ? upcoming[0] : null;
  };

  const nextInterview = getNextInterview();

  return (
    <div style={{ padding: "2rem" }}>
      <h2>🧑 Candidate Dashboard</h2>

      {candidate ? (
        <>
          {/* CV Status */}
          <h4>📄 CV Status</h4>
          <p>
            {candidate.cvParseStatus === "PARSED" && "✅ Parsed"}
            {candidate.cvParseStatus === "PENDING" && "⏳ Parsing"}
            {candidate.cvParseStatus === "FAILED" && "❌ Failed to parse"}
            {!candidate.cvParseStatus && "📭 No CV uploaded"}
          </p>

          {/* Skills */}
          <h4>🛠 Top Skills</h4>
          {candidate.recognizedSkills?.length > 0 ? (
            <ul>
              {candidate.recognizedSkills.slice(0, 3).map((skill, idx) => (
                <li key={idx}>🔹 {skill}</li>
              ))}
            </ul>
          ) : (
            <p>— No skills yet</p>
          )}

          {/* Next Interview */}
          <h4 style={{ marginTop: "1.5rem" }}>📅 Next Interview</h4>
          {nextInterview ? (
            <p>
              {new Date(nextInterview.scheduledAt).toLocaleString()}  
              {nextInterview.status === "CANCELLED" && " (❌ Cancelled)"}
            </p>
          ) : (
            <p>— No upcoming interviews</p>
          )}

          {/* Links */}
          <h4 style={{ marginTop: "1.5rem" }}>🚀 Quick Links</h4>
          <button onClick={() => navigate("/profile")} style={{ marginRight: "1rem" }}>
            🙋 Profile
          </button>
          <button onClick={() => navigate("/my-interviews")}>
            🎯 My Interviews
          </button>
        </>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
}

export default CandidateDashboard;

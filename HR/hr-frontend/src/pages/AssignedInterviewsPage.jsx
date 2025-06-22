import { useEffect, useState } from "react";

function AssignedInterviewsPage() {
  const token = localStorage.getItem("token");
  const [interviews, setInterviews] = useState([]);
  const [feedbacks, setFeedbacks] = useState({});

  useEffect(() => {
    fetch("http://localhost:8081/api/interviews/assigned", {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
    })
      .then((res) => res.json())
      .then(setInterviews)
      .catch((err) => console.error("❌ Failed to fetch assigned interviews", err));
  }, [token]);

  const handleFeedbackSubmit = (interviewId) => {
    const feedback = feedbacks[interviewId];
    fetch(`http://localhost:8081/api/interviews/${interviewId}/feedback`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
        "X-Gateway-Auth": "true",
      },
      body: JSON.stringify({ feedback }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to submit feedback");
        alert("✅ Feedback submitted!");
      })
      .catch((err) => console.error(err));
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>💼 Assigned Interviews</h2>

      {interviews.length === 0 ? (
        <p>No interviews assigned.</p>
      ) : (
        interviews.map((i) => (
          <div key={i.id} style={{ border: "1px solid #ccc", padding: "1rem", marginBottom: "1rem" }}>
            <p><strong>Candidate:</strong> {i.candidateName}</p>
            <p><strong>Scheduled:</strong> {new Date(i.scheduledAt).toLocaleString()}</p>


            <textarea
              placeholder="Write feedback here..."
              rows={3}
              style={{ width: "100%", marginTop: "0.5rem" }}
              value={feedbacks[i.id] || ""}
              onChange={(e) => setFeedbacks({ ...feedbacks, [i.id]: e.target.value })}
            />

            <button
              style={{ marginTop: "0.5rem" }}
              onClick={() => handleFeedbackSubmit(i.id)}
            >
              💬 Submit Feedback
            </button>
          </div>
        ))
      )}
    </div>
  );
}

export default AssignedInterviewsPage;

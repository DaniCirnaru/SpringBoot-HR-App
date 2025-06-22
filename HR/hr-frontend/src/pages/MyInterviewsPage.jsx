import { useEffect, useState } from "react";

function MyInterviewsPage() {
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchInterviews = async () => {
      try {
        const res = await fetch("http://localhost:8081/api/interviews/my", {
          headers: {
            Authorization: `Bearer ${token}`,
            "X-Gateway-Auth": "true",
          },
        });

        if (!res.ok) throw new Error("Failed to fetch interviews");
        const data = await res.json();
        setInterviews(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchInterviews();
  }, [token]);

  if (loading) return <p>Loading your interviews...</p>;
  if (error) return <p style={{ color: "red" }}>❌ {error}</p>;
  if (interviews.length === 0) return <p>No interviews found.</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h2>🎯 My Interviews</h2>
      <table border="1" cellPadding="10" style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Scheduled At</th>
            <th>Status</th>
            <th>Interviewers</th>
            <th>Feedback</th>
          </tr>
        </thead>
        <tbody>
          {interviews.map((interview) => (
            <tr key={interview.id}>
              <td>{interview.id}</td>
              <td>{new Date(interview.scheduledAt).toLocaleString()}</td>
              <td>{interview.status}</td>
              <td>{interview.employeeNames?.join(", ") || "—"}</td>
              <td>{interview.feedback || "—"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default MyInterviewsPage;

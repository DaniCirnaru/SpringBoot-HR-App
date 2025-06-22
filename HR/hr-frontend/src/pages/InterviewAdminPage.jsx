import { useEffect, useState } from "react";

function InterviewAdminPage({ token }) {
  const [interviews, setInterviews] = useState([]);
  const [candidates, setCandidates] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [form, setForm] = useState({
    candidateId: "",
    employeeIds: [],
    scheduledAt: "",
  });
  const [message, setMessage] = useState("");

  const fetchData = async () => {
    try {
      const [iRes, cRes, eRes] = await Promise.all([
        fetch("http://localhost:8081/api/interviews", {
          headers: { Authorization: `Bearer ${token}` },
        }),
        fetch("http://localhost:8081/api/candidates", {
          headers: { Authorization: `Bearer ${token}` },
        }),
        fetch("http://localhost:8081/api/employees", {
          headers: { Authorization: `Bearer ${token}` },
        }),
      ]);
      
      const [iData, cData, eData] = await Promise.all([
        iRes.json(),
        cRes.json(),
        eRes.json(),
      ]);
console.log("🔍 Employees raw data:", eData);
      if (!Array.isArray(iData) || !Array.isArray(cData) || !Array.isArray(eData)) {
        throw new Error("One or more responses are not arrays");
      }

      setInterviews(iData);
      setCandidates(cData);
      setEmployees(eData);
    } catch (err) {
      console.error("❌ Fetch error:", err);
      setMessage("❌ Failed to fetch data. Check console for details.");
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "employeeIds") {
      // ✅ Fix: make sure all selected values are strings
      const selected = Array.from(e.target.selectedOptions, (opt) => opt.value);
      setForm((prev) => ({ ...prev, employeeIds: selected }));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSchedule = async () => {
    // ✅ Fix: Convert employeeIds to numbers for backend
    const payload = {
      ...form,
      employeeIds: form.employeeIds.map((id) => Number(id)),
    };

    try {
      const res = await fetch("http://localhost:8081/api/interviews", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}` },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error("Failed to schedule interview");
      setMessage("✅ Interview scheduled!");
      setForm({ candidateId: "", employeeIds: [], scheduledAt: "" });
      fetchData();
    } catch (err) {
      setMessage(`❌ ${err.message}`);
    }
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>Admin: Interview Management</h2>

      {/* ✅ Candidate dropdown */}
      <label>
        Candidate:
        <select name="candidateId" value={form.candidateId} onChange={handleChange}>
          <option value="">-- Select --</option>
          {candidates.map((c) => (
            <option key={c.id} value={c.id.toString()}>
              {c.name}
            </option>
          ))}
        </select>
      </label>
      <br /><br />

      {/* ✅ Employees multi-select with validation */}
      <label>
        Employees:
        <select
          name="employeeIds"
          multiple
          size={Math.min(employees.length, 6)}
          value={form.employeeIds}
          onChange={handleChange}
          style={{ minWidth: "200px", minHeight: "100px" }}
        >
          {employees.map((e, index) => {
  if (!e || typeof e.userId === "undefined") return null;
  return (
    <option key={e.id} value={e.id.toString()}>
  {e.name}
</option>

  );
})}

        </select>
      </label>
      <br /><br />

      <label>
        Schedule:
        <input
          type="datetime-local"
          name="scheduledAt"
          value={form.scheduledAt}
          onChange={handleChange}
        />
      </label>
      <br /><br />

      <button onClick={handleSchedule}>Schedule Interview</button>
      <p style={{ color: message.startsWith("❌") ? "red" : "green" }}>{message}</p>

      <hr />
      <h3>All Interviews</h3>
      <ul>
        {interviews.map((i) => (
          <li key={`interview-${i.id}`}>
            Candidate #{i.candidateId} — {new Date(i.scheduledAt).toLocaleString()} — Status: {i.status}
            {Array.isArray(i.employeeIds) && i.employeeIds.length > 0 && (
              <ul>
                {i.employeeIds.map((emp) => {
                  const empId = typeof emp === "object" ? emp.id : emp;
                  return (
                    <li key={`interview-${i.id}-emp-${empId}`}>Employee #{empId}</li>
                  );
                })}
              </ul>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default InterviewAdminPage;

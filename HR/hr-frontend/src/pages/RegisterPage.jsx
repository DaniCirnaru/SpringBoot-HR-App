import { useState } from "react";
import { useNavigate } from "react-router-dom";

function RegisterPage() {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    roleId: "2", // default to CANDIDATE
  });
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const res = await fetch("http://localhost:8081/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Registration failed");
      }

      setMessage("✅ Registered successfully! Redirecting to login...");
      setTimeout(() => navigate("/login"), 1500);
    } catch (err) {
      setMessage(`❌ ${err.message}`);
    }
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>User Registration</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Username:
          <input name="username" value={form.username} onChange={handleChange} required />
        </label><br /><br />
        <label>
          Email:
          <input name="email" type="email" value={form.email} onChange={handleChange} required />
        </label><br /><br />
        <label>
          Password:
          <input name="password" type="password" value={form.password} onChange={handleChange} required />
        </label><br /><br />
        <label>
          Role:
          <select name="roleId" value={form.roleId} onChange={handleChange}>
            <option value="4">Candidate</option>
            <option value="2">User</option>
          </select>
        </label><br /><br />
        <button type="submit">Register</button>
      </form>
      {message && <p style={{ marginTop: "1rem", color: message.startsWith("❌") ? "red" : "green" }}>{message}</p>}
    </div>
  );
}

export default RegisterPage;

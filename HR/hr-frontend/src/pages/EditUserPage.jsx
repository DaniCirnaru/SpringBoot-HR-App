import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function EditUserPage() {
  const [user, setUser] = useState(null);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

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
        setUsername(data.username);
        setEmail(data.email);
      })
      .catch((err) => console.error(err));
  }, [token]);

  const handleUpdate = async () => {
  if (!user) return;

  const currentEmail = user.email; // 👈 use existing email for login
  const loginPassword =
    password !== "" ? password : prompt("Please enter your current password:");

  if (!loginPassword) {
    alert("Password is required to confirm your identity.");
    return;
  }

  // Step 1: Authenticate with original credentials
  const authResponse = await fetch("http://localhost:8081/api/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "X-Gateway-Auth": "true",
    },
    body: JSON.stringify({
      email: currentEmail, // 👈 use old email, not updated
      password: loginPassword,
    }),
  });

  if (!authResponse.ok) {
    alert("❌ Authentication failed. Please check your password.");
    return;
  }

  const { token: newToken } = await authResponse.json();

  // Step 2: Proceed with the update
  const updateRes = await fetch("http://localhost:8081/api/users/me", {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
      "X-Gateway-Auth": "true",
    },
    body: JSON.stringify({
      username,
      email,
      password: password !== "" ? password : null,
    }),
  });

  if (!updateRes.ok) {
    alert("❌ Failed to update user info.");
    return;
  }

  // Step 3: Re-login with new credentials
  const finalLoginRes = await fetch("http://localhost:8081/api/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "X-Gateway-Auth": "true",
    },
    body: JSON.stringify({
      email, // 👈 now use updated email
      password: password !== "" ? password : loginPassword,
    }),
  });

  if (!finalLoginRes.ok) {
    alert("Update succeeded, but login with new credentials failed. Please login manually.");
    localStorage.removeItem("token");
    navigate("/login");
    return;
  }

  const { token: finalToken, user: updatedUser } = await finalLoginRes.json();
  localStorage.setItem("token", finalToken);
  setUser(updatedUser);
  alert("✅ User info updated and re-login successful!");
  navigate("/profile");
};

  if (!user) return <p>Loading user info...</p>;

  return (
    <div style={{ padding: "1rem" }}>
      <h2>⚙️ Edit User Info</h2>

      <p>
        <label>Username: </label>
        <input value={username} onChange={(e) => setUsername(e.target.value)} />
      </p>

      <p>
        <label>Email: </label>
        <input value={email} onChange={(e) => setEmail(e.target.value)} />
      </p>

      <p>
        <label>New Password (leave blank to keep current): </label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </p>

      <button onClick={handleUpdate}>💾 Update</button>{" "}
      <button onClick={() => navigate("/profile")}>Cancel</button>
    </div>
  );
}

export default EditUserPage;

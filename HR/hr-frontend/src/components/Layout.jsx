import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";

function Layout({ token, onLogout }) {
  return (
    <div>
      {token && <Navbar token={token} onLogout={onLogout} />}
      <Outlet />
    </div>
  );
}

export default Layout;
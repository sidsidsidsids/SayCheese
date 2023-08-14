import "./User.css";

import { Outlet } from "react-router-dom";

function User() {
  return (
    <div className="UserBox">
      <Outlet />
    </div>
  );
}

export default User;

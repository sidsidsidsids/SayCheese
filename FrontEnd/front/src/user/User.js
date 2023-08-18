// third party
import { Outlet } from "react-router-dom";
// local
import "./User.css";

function User() {
  return (
    <div className="UserBox">
      <Outlet />
    </div>
  );
}

export default User;

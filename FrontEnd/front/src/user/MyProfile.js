import React from "react";
import "./MyProfile.css";

function MyProfile({ name }) {
  return (
    <div>
      <div className="MyProfileBox"></div>
      <p style={{ fontSize: "20px" }}>🙋‍♀️반가워요, {name}님!</p>
    </div>
  );
}

export default MyProfile;

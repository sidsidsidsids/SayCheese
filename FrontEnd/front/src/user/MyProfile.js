import React from "react";
import "./MyProfile.css";
import { useDispatch } from "react-redux";
import { openModal } from "../redux/features/modal/modalSlice";

function MyProfile({ email, nickname, genderFm, age, name, profile, payload }) {
  const dispatch = useDispatch();
  return (
    <div>
      <div
        className="MyProfileBox"
        onClick={(event) => {
          dispatch(openModal(payload));
        }}
      ></div>
      <p style={{ fontSize: "20px", margin: "20px 0" }}>
        🙋‍♀️반가워요, {nickname}님!
      </p>
    </div>
  );
}

export default MyProfile;

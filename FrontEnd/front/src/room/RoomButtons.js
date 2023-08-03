import React from "react";
import "./RoomButtons.css";
function RoomButtons({ onConfirm, onClose, buttonName1, buttonName2 }) {
  return (
    <div className="room-button">
      <button className="button" onClick={onConfirm}>
        {buttonName1}
      </button>
      <button className="button" onClick={onClose}>
        {buttonName2}
      </button>
    </div>
  );
}

export default RoomButtons;

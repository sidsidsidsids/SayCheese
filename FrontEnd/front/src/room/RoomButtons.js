import React from "react";
import "./RoomButtons.css";
function RoomButtons({
  onConfirm,
  onClose,
  buttonName1,
  buttonName2,
  option1,
  option2,
}) {
  return (
    <div className="room-button">
      <button className="button" onClick={onConfirm} disabled={option1}>
        {buttonName1}
      </button>
      <button className="button" onClick={onClose} disabled={option2}>
        {buttonName2}
      </button>
    </div>
  );
}

export default RoomButtons;

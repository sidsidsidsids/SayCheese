import React from "react";
import "./RoomButtons.css";
function RoomButtons({
  onButton1,
  onButton2,
  onButton3,
  buttonName1,
  buttonName2,
  buttonName3,
  option1,
  option2,
  option3,
}) {
  return (
    <div className="room-button">
      <button className="button button1" onClick={onButton1} disabled={option1}>
        {buttonName1}
      </button>
      <button className="button button2" onClick={onButton2} disabled={option2}>
        {buttonName2}
      </button>
      <button className="button button3" onClick={onButton3} disabled={option3}>
        {buttonName3}
      </button>
    </div>
  );
}

export default RoomButtons;

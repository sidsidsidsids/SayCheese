import { useState } from "react";
import "./RoomHeader.css";
import logo from "./assets/favicon.ico";

function RoomHeader({ status }) {
  // 주제(sample)
  // const concepts =
  // const [selectedConcept, setSelectedConcept] = useState(concepts_sample[0]);
  // 주제 내용물(sample)
  // const titles =
  // const [selectedTitles, setSelectedTitles] = useState([titles_sample[0]]);
  // 방 종료 시간
  // const roomDead =
  const roomDead_sample = 5;

  return (
    <div className="room-header">
      <img src={logo} alt="LOGO" />
      <br />
      {status === 2 ? (
        <div>
          <p>방이 {roomDead_sample}분 후 종료됩니다</p>
        </div>
      ) : status === 1 ? (
        <div>
          <p>주제 : {}</p>
        </div>
      ) : (
        <div>
          <p>대기중</p>
          <div className="concepts-list" style={{ display: "flex" }}></div>
        </div>
      )}
    </div>
  );
}

export default RoomHeader;

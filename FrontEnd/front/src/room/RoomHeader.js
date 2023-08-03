import { useState } from "react";
import "./RoomHeader.css";
import logo from "./assets/favicon.ico";

function RoomHeader({ status }) {
  // 주제(sample)
  // const concepts =
  const concepts_sample = ["Concept1", "Concept2", "Concept3"];
  const [selectedConcept, setSelectedConcept] = useState(concepts_sample[0]);
  // 주제 내용물(sample)
  // const titles =
  const titles_sample = [
    ["1_title1", "1_title2", "1_title3", "1_title4"],
    ["2_title1", "2_title2", "2_title3", "2_title4"],
    ["3_title1", "3_title2", "3_title3", "3_title4"],
  ];
  const [selectedTitles, setSelectedTitles] = useState([titles_sample[0]]);
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
          <p>주제 : {selectedConcept}</p>
          {selectedTitles.map((item, index) => (
            <span> {item} </span>
          ))}
        </div>
      ) : (
        <div>
          <p>주제를 선택해주세요</p>
          <div className="concepts-list" style={{ display: "flex" }}>
            {concepts_sample.map((item, index) => (
              <p
                key={index}
                className={selectedConcept === item ? "selected" : ""}
                onClick={() => {
                  setSelectedConcept(item);
                  setSelectedTitles(titles_sample[index]);
                }}
                style={{ margin: "0 5px" }}
              >
                {item === selectedConcept ? <p>sel: {item}</p> : <p>{item}</p>}
              </p>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default RoomHeader;

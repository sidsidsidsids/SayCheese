import { useState, useEffect } from "react";
import "./RoomJoinModal.css";

function RoomJoinModal({ open, close }) {
  const [inputRoomCode, setInputRoomCode] = useState("");
  const [inputRoomPassword, setInputRoomPassword] = useState("");

  if (!open) {
    return null;
  }
  const handleConfirm = () => {
    console.log("방 코드(inputRoomCode): ", inputRoomCode);
    console.log("방 비밀번호(inputRoomPassword): ", inputRoomPassword);
    close();
  };
  return (
    <div className="room-join-modal">
      <div className="room-join-modal-content">
        <h2>방 입장</h2>
        <p>
          방 코드:
          <input
            type="text"
            placeholder="방 코드를 입력해주세요"
            value={inputRoomCode}
            onChange={(event) => {
              setInputRoomCode(event.target.value);
            }}
            maxLength={10}
          />
        </p>
        <p>
          방 비밀번호:
          <input
            type="text"
            placeholder="비밀번호를 입력해주세요"
            value={inputRoomPassword}
            onChange={(event) => {
              setInputRoomPassword(event.target.value);
            }}
            maxLength={10}
          />
        </p>
        <button onClick={handleConfirm}>확인</button>
        <button onClick={close}>나가기</button>
      </div>
    </div>
  );
}

export default RoomJoinModal;

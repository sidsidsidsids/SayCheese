import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./RoomJoinModal.css";
import ModalButtons from "./ModalButtons";

function RoomJoinModal({ open, close }) {
  const navigate = useNavigate();

  const [inputRoomCode, setInputRoomCode] = useState("");
  const [inputRoomPassword, setInputRoomPassword] = useState("");

  if (!open) {
    return null;
  }
  const handleConfirm = () => {
    console.log("방 코드(inputRoomCode): ", inputRoomCode);
    console.log("방 비밀번호(inputRoomPassword): ", inputRoomPassword);
    console.log("해당 방 코드가 있는지 확인");
    console.log("해당 방 코드가 존재한다면 => 비밀번호 일치 확인");
    console.log("비밀번호가 일치한다면 => 이후 로그인 여부에 따라 진행");
    checkJoinable(inputRoomCode, inputRoomPassword);
  };

  const checkJoinable = async (roomCode, roomPassword) => {
    try {
      const request = await axios.post(
        "/api/room/check/" + roomCode,
        {
          password: roomPassword,
        },
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
      navigate(`/room/${roomCode}`);
      close();
    } catch (error) {
      alert(error);
    }
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
        <ModalButtons onConfirm={handleConfirm} onClose={close} />
      </div>
    </div>
  );
}

export default RoomJoinModal;

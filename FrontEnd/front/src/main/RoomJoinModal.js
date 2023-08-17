import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { setRoom } from "../redux/features/room/roomSlice";
import axios from "axios";
import Swal from "sweetalert2"
import "./RoomJoinModal.css";
import ModalButtons from "./ModalButtons";

function RoomJoinModal({ open, close }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { userInfo } = useSelector((store) => store.login);

  const [inputRoomCode, setInputRoomCode] = useState("");
  const [inputRoomPassword, setInputRoomPassword] = useState("");

  if (!userInfo) {
    close();
  }
  if (!open) {
    return null;
  }
  const checkAvailable = async () => {
    try {
      const response = await axios
        .post(
          "/api/room/check",
          {},
          {
            headers: {
              Authorization: `${localStorage.getItem("accessToken")}`,
            },
          }
        )
        .then(() => {
          // handleConfirm();
          checkJoinable(inputRoomCode, inputRoomPassword);
        })
        .catch(() => {
          Swal.fire("이미 접속 중 입니다");
        });
    } catch (error) {
      Swal.fire("비정상적 접근");
      console.log(error);
    }
  };
  const checkJoinable = (roomCode, roomPassword) => {
    console.log(roomCode, roomPassword);
    try {
      axios
        .post(
          `/api/room/check/${roomCode}`,
          {
            password: roomPassword,
          },
          {
            headers: {
              Authorization: `${localStorage.getItem("accessToken")}`,
              "Content-Type": "application/json;charset=UTF-8",
            },
          }
        )
        .then(() => {
          dispatch(
            setRoom({
              owner: false,
            })
          );
          navigate(`/room/${roomCode}`);
          close();
        })
        .catch((error) => {
          console.log(error);
          Swal.fire(error.response.data.message);
        });
    } catch (error) {
      console.log(error);
      Swal.fire("정확한 방 코드와 비밀번호를 입력해주세요");
    }
  };
  return (
    <div className="room-join-modal">
      <div className="room-join-modal-content">
        <h2>방 입장</h2>
        <p>
          방 코드
          <input
            type="text"
            placeholder="방 코드를 입력해주세요"
            value={inputRoomCode}
            onChange={(event) => {
              setInputRoomCode(event.target.value);
            }}
            maxLength={10}
            id="roomCode"
          />
        </p>
        <p>
          방 비밀번호
          <input
            type="text"
            placeholder="비밀번호를 입력해주세요"
            value={inputRoomPassword}
            onChange={(event) => {
              setInputRoomPassword(event.target.value);
            }}
            maxLength={10}
            id="roomPW"
          />
        </p>
        <ModalButtons
          onConfirm={() => {
            checkAvailable();
          }}
          onClose={close}
        />
      </div>
    </div>
  );
}

export default RoomJoinModal;

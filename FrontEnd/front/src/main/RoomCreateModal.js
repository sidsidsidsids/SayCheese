import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { setRoom } from "../redux/features/room/roomSlice";
import axios from "axios";
import Swal from "sweetalert2"
import "./RoomCreateModal.css";
import ModalButtons from "./ModalButtons";
import l_Frame from "./assets/ladder_shape.svg";
import w_Frame from "./assets/window_shape.png";

function RoomCreateModal({ open, close }) {
  const dispatch = useDispatch();
  // 이동 위한 navigate 선언
  const navigate = useNavigate();
  // 모달 설정
  const [isComplete, setIsComplete] = useState(false);

  const { userInfo } = useSelector((store) => store.login);
  // 방 설정들
  // 방 모드
  const [isModeActive, setIsModeActive] = useState(true);
  // 방 인원
  const [roomLimit, setRoomLimit] = useState(4);
  // 프레임 격자
  const [isWindowFrame, setIsWindowFrame] = useState(true);
  // 방 비밀번호
  const [roomPassword, setRoomPassword] = useState("");
  // 방 코드
  const [roomCode, setRoomCode] = useState("");

  if (!userInfo) {
    close();
  }
  async function codeCreation() {
    let isValid = false;
    while (!isValid) {
      let tmpCode = Math.random().toString(36).substring(2, 10);
      isValid = await isValidCode(tmpCode);
      if (isValid) {
        if (isWindowFrame) {
          setRoomCode(roomLimit + "1" + tmpCode);
        } else {
          setRoomCode(roomLimit + "0" + tmpCode);
        }
      }
    }
  }
  async function isValidCode(code) {
    try {
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  }
  const checkAvailable = () => {
    try {
      const response = axios
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
          handleConfirm();
        })
        .catch((error) => {
          Swal.fire(error.response.data.message);
          console.log(error);
        });
    } catch (error) {
      Swal.fire("비정상적 접근");
      console.log(error);
    }
  };

  // 방 초대링크
  let roomInvite;
  // open이 아닐 때 출력 x
  if (!open) {
    return null;
  }
  // 최종 제출
  const handleConfirm = () => {
    codeCreation();
    setIsComplete(true);
  };

  const sendRoomData = () => {
    let selectedMode;
    let selectedFrame;
    if (isModeActive === true) {
      selectedMode = "game";
    } else {
      selectedMode = "normal";
    }
    if (isWindowFrame === true) {
      selectedFrame = "horizontal";
    } else {
      selectedFrame = "vertical";
    }
    dispatch(
      setRoom({
        password: roomPassword,
        maxCount: roomLimit,
        mode: selectedMode,
        // roomCode: "sessionC",
        roomCode: roomCode,
        specification: selectedFrame,
        owner: true,
      })
    );
    navigate(`/room/${roomCode}`);
  };

  return (
    <div className="room-create-modal">
      {isComplete ? (
        <div className="finish-create-modal-content">
          <h2>방 생성</h2>
          <div className="room-code">
            <span>방 코드</span>
            <input
              id="createdCode"
              className={roomCode}
              value={roomCode ? roomCode : "Sample Room Code"}
              readOnly
            />
          </div>
          <div className="room-invite">
            <span>방 모드</span>
            <input
              className={roomInvite}
              value={isModeActive ? "Game" : "Normal"}
              id="inviteLink"
              readOnly
            />
          </div>
          <ModalButtons
            onConfirm={() => {
              sendRoomData();
              setIsComplete(false);
            }}
            onClose={close}
          />
        </div>
      ) : (
        <div className="room-create-modal-content">
          <h2>방 생성</h2>
          {/* 모드 설정 */}
          <div className="mode-selection">
            <p>
              <label>
                <input
                  id="gameMode"
                  type="radio"
                  checked={isModeActive}
                  onChange={() => setIsModeActive(!isModeActive)}
                />
                게임 모드
              </label>
              <label>
                <input
                  id="normalMode"
                  type="radio"
                  checked={!isModeActive}
                  onChange={() => setIsModeActive(!isModeActive)}
                />
                일반 모드
              </label>
            </p>
          </div>
          {/* 방 인원수 설정 */}
          <div className="limit-settings">
            <p>
              인원 수 설정
              <input type="number" value={roomLimit} readOnly />명
              <button
                onClick={() => {
                  if (roomLimit < 4) {
                    setRoomLimit(roomLimit + 1);
                  }
                }}
              >
                +
              </button>
              <button
                onClick={() => {
                  if (roomLimit > 1) {
                    setRoomLimit(roomLimit - 1);
                  }
                }}
              >
                -
              </button>
            </p>
          </div>

          {/* 프레임 설정 */}
          <div className="frame-type">
            <p>
              프레임 규격 선택
              <button
                className="frame-btn"
                onClick={() => {
                  setIsWindowFrame(!isWindowFrame);
                }}
              >
                {isWindowFrame ? (
                  <img src={w_Frame} alt="창문 모양" />
                ) : (
                  <img src={l_Frame} alt="사다리 모양" />
                )}
              </button>
            </p>
          </div>
          {/* 비밀번호 설정 */}
          <div className="password-settings">
            <input
              type="password"
              placeholder="비밀번호를 입력해주세요(필수)"
              value={roomPassword}
              onChange={(event) => {
                setRoomPassword(event.target.value);
              }}
              maxLength={10}
            />
          </div>
          <ModalButtons
            onConfirm={() => {
              checkAvailable();
            }}
            onClose={close}
          />
        </div>
      )}
    </div>
  );
}

export default RoomCreateModal;

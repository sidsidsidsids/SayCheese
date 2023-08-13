import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RoomCreateModal.css";
import ModalButtons from "./ModalButtons";
import l_Frame from "./assets/ladder_shape.svg";
import w_Frame from "./assets/window_shape.png";

function RoomCreateModal({ open, close }) {
  // 이동 위한 navigate 선언
  const navigate = useNavigate();
  // 모달 설정
  const [isComplete, setIsComplete] = useState(false);

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
      await console.log("이 코드", code, " 중복 체크");
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  }
  // 방 초대링크
  let roomInvite;
  // open이 아닐 때 출력 x
  if (!open) {
    return null;
  }
  // 최종 제출
  const handleConfirm = () => {
    console.log("## ## ## ## ## ## ## ");
    console.log("방 모드(isModeActive): ", isModeActive);
    console.log("방 인원(roomLimit): ", roomLimit);
    console.log("방 프레임(isWindowFrame): ", isWindowFrame);
    console.log("방 비밀번호(roomPassword): ", roomPassword);
    console.log("## ## ## ## ## ## ## ");
    codeCreation();
    setIsComplete(true);
  };

  return (
    <div className="room-create-modal">
      {isComplete ? (
        <div className="finish-create-modal-content">
          <h2>방 생성</h2>
          <div className="room-code">
            <p>
              <b>방 코드: </b>
              <input
                className={roomCode}
                value={roomCode ? roomCode : "Sample Room Code"}
                readOnly
              />
            </p>
          </div>
          <div className="room-invite">
            <p>
              <b>방 초대 링크: </b>
              <input
                className={roomInvite}
                value={
                  roomInvite
                    ? roomInvite
                    : "www.sample.com/sample123/?sample456?/sample789?"
                }
                readOnly
              />
            </p>
          </div>
          <ModalButtons
            onConfirm={() => {
              console.log("방 코드(roomCode): ", roomCode);
              console.log("방 초대링크(roomInvite): ", roomInvite);
              navigate(`/room/${roomCode}`);
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
                  type="checkbox"
                  checked={isModeActive}
                  onChange={() => setIsModeActive(!isModeActive)}
                />
                게임 모드
              </label>
              <label>
                <input
                  type="checkbox"
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
              type="text"
              placeholder="비밀번호를 입력해주세요(선택)"
              value={roomPassword}
              onChange={(event) => {
                setRoomPassword(event.target.value);
              }}
              maxLength={10}
            />
          </div>
          <ModalButtons onConfirm={handleConfirm} onClose={close} />
        </div>
      )}
    </div>
  );
}

export default RoomCreateModal;

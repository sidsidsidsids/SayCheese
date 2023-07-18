import { useState, useEffect } from "react";
import "./RoomCreateModal.css";
import l_Frame from "./assets/ladder_shape.PNG";
import w_Frame from "./assets/window_shape.PNG";

function RoomCreateModal({ open, close }) {
  // 방 설정들
  const [modeActive, setModeActive] = useState(true);
  const [roomTime, setRoomTime] = useState(30);
  const [squareFrame, setSquareFrame] = useState(true);
  const [roomPassword, setRoomPassword] = useState("");
  // 모달 설정
  const [isComplete, setIsComplete] = useState(false);

  // open이 아닐 때 출력 x
  if (!open) {
    return null;
  }
  // 시간 설정
  const handleRoomTime = (event) => {
    setRoomTime(event.target.value);
  };
  // 패스워드 설정
  const handleRoomPassword = (event) => {
    setRoomPassword(event.target.value);
  };
  // 프레임 설정
  const handleFrame = () => {
    setSquareFrame(!squareFrame);
  };
  // 최종 제출
  const handleConfirm = () => {
    // 유효성 검사
    if ((roomTime === null) | (roomTime < 1)) {
      alert("촬영 시간이 올바르게 설정되지 않아 기본값(30s)으로 설정합니다");
      setRoomTime(30);
    }
    console.log("방 모드(modeActive): ", modeActive);
    console.log("방 프레임(squareFrame): ", squareFrame);
    console.log("방 시간(roomTime): ", roomTime);
    console.log("방 비밀번호(roomPassword): ", roomPassword);
    close();
  };

  return (
    <div className="room-create-modal">
      <div className="room-create-modal-content">
        <h2>방 생성</h2>
        {/* 모드 설정 */}
        <div className="mode-selection">
          <p>
            <label>
              <input
                type="checkbox"
                checked={modeActive}
                onChange={() => setModeActive(!modeActive)}
              />
              게임 모드
            </label>
            <label>
              <input
                type="checkbox"
                checked={!modeActive}
                onChange={() => setModeActive(!modeActive)}
              />
              일반 모드
            </label>
          </p>
        </div>
        {/* 시간 설정 */}
        <div className="time-settings">
          <p>
            촬영 시간 설정
            <input
              // onInput : maxLength 적용 위한 코드
              onInput={(event) => {
                if (event.target.value.length > event.target.maxLength)
                  event.target.value = event.target.value.slice(
                    0,
                    event.target.maxLength
                  );
              }}
              type="number"
              value={roomTime}
              onChange={handleRoomTime}
              maxLength={3}
            />
            초
          </p>
        </div>
        {/* 프레임 설정 */}
        <div className="frame-type">
          <p>
            프레임 규격 선택
            <button className="frame-btn" onClick={handleFrame}>
              {squareFrame ? (
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
            placeholder="비밀번호를 입력해주세요"
            value={roomPassword}
            onChange={handleRoomPassword}
            maxLength={10}
          />
        </div>
        <button onClick={handleConfirm}>확인</button>
        <button onClick={close}>나가기</button>
      </div>
    </div>
  );
}

export default RoomCreateModal;

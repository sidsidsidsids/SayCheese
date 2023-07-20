import "./RoomHeader.css";
function RoomHeader(status) {
  return (
    <div className="room-header">
      {status === "result" ? (
        <p>방이 5분 후 종료됩니다</p>
      ) : status === "run" ? (
        <p>주제 : </p>
      ) : (
        <p>주제를 선택해주세요</p>
      )}
    </div>
  );
}

export default RoomHeader;

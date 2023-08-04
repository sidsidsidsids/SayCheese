import "./RoomFooter.css";
function RoomFooter({ status }) {
  return (
    <div className="room-footer">
      <span>푸터</span>
      {status === 2 ? <div></div> : status === 1 ? null : null}
    </div>
  );
}

export default RoomFooter;

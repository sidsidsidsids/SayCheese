import { useSelector } from "react-redux";
import "./RoomFooter.css";
import WebCam from "./WebCam";
function RoomFooter() {
  const roomStatus = useSelector((state) => state.status);

  return (
    <div className="room-footer">
      <span>푸터</span>
      {roomStatus.startAction ? (
        <div>
          <WebCam width={"7.68%"} height={"4.32%"} />
        </div>
      ) : null}
    </div>
  );
}

export default RoomFooter;

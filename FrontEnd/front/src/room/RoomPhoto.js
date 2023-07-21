import "./RoomPhoto.css";
import WebCam from "./WebCam";
function RoomPhoto() {
  return (
    <div className="room-photo">
      <span>센터</span>
      <WebCam width={"24%"} height={"13.5%"} />
    </div>
  );
}

export default RoomPhoto;

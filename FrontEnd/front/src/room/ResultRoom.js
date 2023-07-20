import RoomButtons from "./RoomButtons";
import RoomChat from "./RoomChat";
import RoomFooter from "./RoomFooter";
import RoomHeader from "./RoomHeader";
import RoomPhoto from "./RoomPhoto";
import "./Room.css";

function ResultRoom() {
  return (
    // {roomStatus == "before" ? () : ()}
    <div className="room-active">
      <RoomHeader />
      <RoomButtons
        onConfirm={() => {}}
        onClose={() => {}}
        buttonName1="버튼1"
        buttonName2="버튼2"
      />
      <RoomPhoto />
      <RoomChat />
      <RoomFooter />
    </div>
  );
}

export default ResultRoom;

import RoomButtons from "./RoomButtons";
import RoomChat from "./RoomChat";
import RoomFooter from "./RoomFooter";
import RoomHeader from "./RoomHeader";
import RoomPhoto from "./RoomPhoto";
import "./Room.css";

function RunRoom() {
  return (
    // {roomStatus == "before" ? () : ()}
    <div className="room-active">
      <div>
        <RoomHeader />
        <RoomButtons
          onConfirm={() => {}}
          onClose={() => {}}
          buttonName1="촬영"
          buttonName2="버튼2"
        />
      </div>
      <div>
        <RoomPhoto />
        <RoomChat />
      </div>

      <RoomFooter />
    </div>
  );
}

export default RunRoom;

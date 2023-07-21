import { useDispatch } from "react-redux";
import { finish, r_start } from "../redux/roomStatus";
import RoomButtons from "./RoomButtons";
import RoomChat from "./RoomChat";
import RoomFooter from "./RoomFooter";
import RoomHeader from "./RoomHeader";
import RoomPhoto from "./RoomPhoto";
import "./Room.css";

function RunRoom() {
  const dispatch = useDispatch();
  return (
    // {roomStatus == "before" ? () : ()}
    <div className="room-active">
      <div className="room-top">
        <RoomHeader />
        <RoomButtons
          onConfirm={() => {
            dispatch(finish());
          }}
          onClose={() => {
            dispatch(r_start());
          }}
          buttonName1="촬영"
          buttonName2="버튼2"
        />
      </div>
      <div className="room-mid">
        <RoomPhoto />
        <RoomChat />
      </div>
      <div className="room-bot">
        <RoomFooter />
      </div>
    </div>
  );
}

export default RunRoom;

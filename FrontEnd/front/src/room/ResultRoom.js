import { useDispatch } from "react-redux";
import { statusAction } from "../redux/roomStates";
import RoomButtons from "./RoomButtons";
import RoomChat from "./RoomChat";
import RoomFooter from "./RoomFooter";
import RoomHeader from "./RoomHeader";
import RoomPhoto from "./RoomPhoto";
import "./Room.css";

function ResultRoom() {
  const dispatch = useDispatch();
  return (
    <div className="room-active">
      <div className="room-top">
        <RoomHeader />
        <RoomButtons
          onConfirm={() => {}}
          onClose={() => {
            dispatch(statusAction.end_room());
          }}
          buttonName1="버튼1"
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

export default ResultRoom;

import { useDispatch } from "react-redux";
import { r_finish } from "../redux/roomStatus";
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
            dispatch(r_finish())
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

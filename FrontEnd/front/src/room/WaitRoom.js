import { useDispatch, useSelector } from "react-redux";
import { statusAction } from "../redux/roomStates";
import RoomButtons from "./RoomButtons";
import RoomChat from "./RoomChat";
import RoomFooter from "./RoomFooter";
import RoomHeader from "./RoomHeader";
import RoomPhoto from "./RoomPhoto";
import "./Room.css";

function WaitRoom() {
  const dispatch = useDispatch();
  const stat = useSelector((state) => state.roomState);
  return (
    // {roomState == "before" ? () : ()}
    <div className="room-active">
      <div className="room-top">
        <RoomHeader />
        <RoomButtons
          onConfirm={() => {
            dispatch(statusAction.start());
            console.log(stat);
          }}
          onClose={() => {}}
          buttonName1="대기"
          buttonName2="취소"
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

export default WaitRoom;

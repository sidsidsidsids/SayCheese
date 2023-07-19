import { useState } from "react";
import "./Room.css";
import RunRoom from "./RunRoom";
import ResultRoom from "./ResultRoom";
import WaitRoom from "./WaitRoom";

function Room() {
  const [roomStatus, setRoomStatus] = useState({
    startAction: false,
    afterAction: false,
  });

  return (
    // {roomStatus == "before" ? () : ()}
    <div className="room">
      {roomStatus.afterAction ? (
        <ResultRoom />
      ) : roomStatus.startAction ? (
        <RunRoom />
      ) : (
        <WaitRoom />
      )}
    </div>
  );
}

export default Room;

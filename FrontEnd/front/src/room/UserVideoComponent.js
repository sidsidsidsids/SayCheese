import React from "react";
import OpenViduVideoComponent from "./OvVideo";
import "./UserVideoComponent.css";

export default function TargetVideoComponent({ streamManager, myName }) {
  const getNicknameTag = () => {
    // Gets the nickName of the user
    // return JSON.parse(streamManager.stream.connection.data).clientData;
    // return myName;
  };

  return (
    <div>
      {streamManager !== undefined ? (
        <div className="streamcomponent">
          <OpenViduVideoComponent streamManager={streamManager} />
          <div>
            <p>{getNicknameTag()}</p>
          </div>
        </div>
      ) : null}
    </div>
  );
}

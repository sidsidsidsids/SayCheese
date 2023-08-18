import React from "react";
import OpenViduVideoComponent from "./OvVideo";
import "./UserVideoComponent.css";

export default function UserVideoComponent({
  streamManager,
  locationX,
  locationY,
  selectedSpec,
}) {
  const getNicknameTag = () => {
    // Gets the nickName of the user
    // return JSON.parse(streamManager.stream.connection.data).clientData;
    // return myName;
  };

  return (
    <div>
      {streamManager !== undefined ? (
        <div className="streamcomponent">
          <OpenViduVideoComponent
            streamManager={streamManager}
            locationX={locationX}
            locationY={locationY}
            selectedSpec={selectedSpec}
          />
          <div>
            <p>{getNicknameTag()}</p>
          </div>
        </div>
      ) : null}
    </div>
  );
}

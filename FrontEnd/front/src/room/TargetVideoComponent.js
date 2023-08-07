import React from "react";
import OpenViduVideoComponent from "./OvVideo";
import "./TargetVideoComponent.css";

export default function UserVideoComponent({
  streamManager,
  locationX,
  locationY,
}) {
  const getNicknameTag = () => {
    // Gets the nickName of the user
    // return JSON.parse(streamManager.stream.connection.data).clientData;
    // return myName;
  };

  return (
    <div>
      {streamManager !== undefined ? (
        <div
          className="streamcomponent_t"
          // style={{ left: `100px`, top: `200px` }}
        >
          <OpenViduVideoComponent
            streamManager={streamManager}
            locationX={locationX}
            locationY={locationY}
          />
          <div>
            <p>{getNicknameTag()}</p>
          </div>
        </div>
      ) : null}
    </div>
  );
}

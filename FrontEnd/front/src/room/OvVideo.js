import React, { useRef, useEffect } from "react";

export default function OpenViduVideoComponent({
  streamManager,
  locationX,
  locationY,
}) {
  const videoRef = useRef();

  useEffect(() => {
    if (streamManager && videoRef.current) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

  return (
    <video
      style={{ position:"absolute", top: `${locationY}px`, left: `${locationX}px`,
    width: "217.5px", height: "165.5px" }}
      autoPlay={true}
      ref={videoRef}
    />
  );
}

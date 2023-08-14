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
      style={{ top: `${locationY}px`, left: `${locationX}px` }}
      autoPlay={true}
      ref={videoRef}
    />
  );
}

import React, { useRef, useEffect } from "react";

export default function OpenViduVideoComponent({
  streamManager,
  locationX,
  locationY,
  selectedSpec,
}) {
  const videoRef = useRef();
  let videoSizes = {
    VERTICAL: {
      width: "170px",
      height: "114px",
    },
    HORIZONTAL: {
      width: "217px",
      height: "165px",
    },
  };
  let videoWidth = "170px";
  let videoHeight = "114px";

  if (selectedSpec) {
    videoWidth = videoSizes[selectedSpec].width;
    videoHeight = videoSizes[selectedSpec].height;
  }
  useEffect(() => {
    if (streamManager && videoRef.current) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

  return (
    <video
      style={{
        position: "absolute",
        top: `${locationY}px`,
        left: `${locationX}px`,
        width: videoWidth,
        height: videoHeight,
        zndex: 1000
      }}
      autoPlay={true}
      ref={videoRef}
    />
  );
}

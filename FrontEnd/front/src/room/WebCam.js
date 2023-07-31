import { useEffect, useRef } from "react";
import io from "socket.io-client";

function WebCam({ width, height }) {
  const socket = io();
  const videoRef = useRef(null);

  useEffect(() => {
    navigator.mediaDevices
      .getUserMedia({ video: true, audio: true })
      .then((stream) => {
        if (videoRef.current) {
          videoRef.current.srcObject = stream;
        }
      })
      .catch((error) => {
        console.error("스트림을 가져오지 못했습니다:", error);
      });

    return () => {
      if (videoRef.current && videoRef.current.srcObject) {
        const tracks = videoRef.current.srcObject.getTracks();
        tracks.forEach((track) => track.stop());
      }
    };
  }, []);

  return (
    <div style={{ border: "1px dashed black" }}>
      {/* <video
        ref={videoRef}
        width={width}
        height={height}
        autoPlay
        playsInline
        style={{ transform: "scaleX(-1)" }}
      /> */}
    </div>
  );
}

export default WebCam;

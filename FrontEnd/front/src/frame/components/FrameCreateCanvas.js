import React, { useEffect, useRef } from "react";
import { fabric } from "fabric";

import { useDispatch, useSelector } from "react-redux";

export default function CanvasArea() {
  const canvasRef = useRef(null);
  const canvas = useRef(null);
  //store에서 width, height 가져오기
  const { width, height } = useSelector((store) => store.frame);

  // 사다리형 사진 네모 구멍 펀칭
  function punchingVerticalPlainHole() {
    const bg = new fabric.Rect({
      width: width,
      height: height,
      left: 0,
      top: 0,
      fill: "#C4FFBB",
      lockMovementX: true, // 움직이지 않도록 합니다
      lockMovementY: true,
      lockRotation: true,
      selectable: false, // 선택 불가능
    });

    const hole1 = new fabric.Rect({
      left: 19,
      top: 19,
      width: 170,
      height: 114,
      // fill: "transparent",
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다.
    });

    const hole2 = new fabric.Rect({
      left: 19,
      top: 139,
      width: 170,
      height: 114,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out",
    });

    const hole3 = new fabric.Rect({
      left: 19,
      top: 259,
      width: 170,
      height: 114,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out",
    });

    const hole4 = new fabric.Rect({
      left: 19,
      top: 379,
      width: 170,
      height: 114,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out",
    });

    canvas.current.add(bg).add(hole1).add(hole2).add(hole3).add(hole4);
  }

  useEffect(() => {
    // 캔버스 초기화
    canvas.current = new fabric.Canvas(canvasRef.current, {
      height: height, // 초기 크기는 사다리형
      width: width,
      // draggable: false,
    });

    // 기본 설정 및 이벤트 처리 등을 구현합니다.

    punchingVerticalPlainHole();
    // 언마운트 시 캔버스 정리
    return () => {
      canvas.current.dispose();
    };
  }, [width, height]);

  // 툴바에서 호출되는 함수로 캔버스의 크기를 변경합니다.
  function handleChangeCanvasSize(width, height) {
    canvas.current.setDimensions({ width, height });
    canvas.current.renderAll();
  }

  function down() {
    const canvas = document.getElementById("canvas");
    const img = canvas.toDataURL("image/png");
    document.write('<img src="' + img + '"/>');
  }
  return (
    <>
      <canvas ref={canvasRef} className="createCanvas" id="canvas" />
      <button onClick={down}>필포든</button>
    </>
  );
}

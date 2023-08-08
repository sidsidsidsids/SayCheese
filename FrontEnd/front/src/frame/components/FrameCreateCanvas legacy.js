// 프레임의 기초가 되는 골격을 잡는 툴바의 디테일 컴포넌트입니다.
import React, { useEffect, useRef, useState } from "react";
import { fabric } from "fabric";

import { useDispatch, useSelector } from "react-redux";

export default function CanvasArea() {
  const canvasRef = useRef(null);
  const canvas = useRef(null);

  // store에서 프레임의 width, height 가져오기
  // 기본은 사다리형
  const { width, height } = useSelector((store) => store.frame);
  
  // 기본 프레임 골격 잡기 로직 시작
  // 1. 배경 깔기 (공통)
  const bg = new fabric.Rect({
    width: width,
    height: height,
    left: 0,
    top: 0,
    fill: '#324512',
    lockMovementX: true, // 움직이지 않도록 합니다
    lockMovementY: true,
    lockRotation: true,
    selectable: false, // 선택 불가능
  });



  // 2. 얼굴 나올 네모 칸 투명하게 만들기
  // 2-1. 사다리형 사진 네모칸 펀칭
  // 네모칸은 위에서부터 아래로
  function punchingVerticalPlainBlock() {

    const block1 = new fabric.Rect({
      left: 19,
      top: 19,
      width: 170,
      height: 114,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다.
    });

    const block2 = new fabric.Rect({
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

    const block3 = new fabric.Rect({
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

    const block4 = new fabric.Rect({
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

    canvas.current.add(bg).add(block1).add(block2).add(block3).add(block4);

  }

  // 2-2. 창문형 액자 펀칭
  // 블록의 방향은 좌상단, 우상단, 우하단, 좌하단(시계방향)
  function punchingHorizontalPlainBlock() {

    const block1 = new fabric.Rect({
      left: 32,
      top: 29,
      width: 217,
      height: 165,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다.
    });

    const block2 = new fabric.Rect({
      left: 261,
      top: 29,
      width: 217,
      height: 165,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out",
    });

    const block3 = new fabric.Rect({
      left: 261,
      top: 206,
      width: 217,
      height: 165,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out",
    });

    const block4 = new fabric.Rect({
      left: 32,
      top: 206,
      width: 217,
      height: 165,
      fill: "#7767AC",
      lockMovementX: true,
      lockMovementY: true,
      lockRotation: true,
      selectable: false,
      globalCompositeOperation: "destination-out",
    });

    canvas.current.add(bg).add(block1).add(block2).add(block3).add(block4);

  }
  // 3. 사다리형인지 창문형인지 가로 세로 길이를 비교하여 구분 한다
  function punching() {
    if (height > width) { //사다리형
      punchingVerticalPlainBlock();
    } else {
      punchingHorizontalPlainBlock();
    }
  }
  useEffect(() => {
    // 4. 캔버스 초기화 및 생성
    canvas.current = new fabric.Canvas(canvasRef.current, {
      height: height, // 초기 크기는 사다리형
      width: width,

    });
    // 5. 캔버스 배경 및 펀칭
    punching()

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

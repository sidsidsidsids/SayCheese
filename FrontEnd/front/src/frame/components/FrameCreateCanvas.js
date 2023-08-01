// 프레임의 기초가 되는 골격을 잡는 툴바의 디테일 컴포넌트입니다.
import React, { useEffect, useRef } from "react";
// third party
import { fabric } from "fabric";
import { useSelector } from "react-redux";

//사다리 기본형 프레임 투명 칸 만드는 함수입니다
const VerticalPlainBlock = (left, top) =>
  new fabric.Rect({
    left: left,
    top: top,
    width: 170,
    height: 114,
    fill: "#7767AC",
    lockMovementX: true, // 움직이지 않도록 합니다
    lockMovementY: true,
    lockRotation: true,
    selectable: false, // 선택 불가능
    globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
  });

// 창문형 프레임 투명 칸 만드는 함수입니다
const HorizontalPlainBlock = (left, top) =>
  new fabric.Rect({
    left: left,
    top: top,
    width: 217,
    height: 165,
    fill: "#7767AC",
    lockMovementX: true, // 움직이지 않도록 합니다
    lockMovementY: true,
    lockRotation: true,
    selectable: false, // 선택 불가능
    globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
  });

const CanvasArea = () => {
  const canvasRef = useRef(null);
  const canvas = useRef(null);
  // useSelector를 통해 Redux store에서 프레임의 width와 height 값을 가져옵니다
  // 기본은 사다리형
  const { width, height } = useSelector((store) => store.frame);

  // 기본 프레임 골격 잡기 로직 시작
  // 1. 배경 깔기 (공통)

  const bg = new fabric.Rect({
    width: width,
    height: height,
    left: 0,
    top: 0,
    fill: "#324512",
    lockMovementX: true, // 움직이지 않도록 합니다
    lockMovementY: true,
    lockRotation: true,
    selectable: false, // 선택 불가능
  });

  useEffect(() => {
    // useEffect를 사용하여 캔버스를 초기화하고 사다리형과 창문형에 맞게 투명한 블록들을 추가합니다. height와 width의 변화에 따라 캔버스의 크기를 조정합니다.
    canvas.current = new fabric.Canvas(canvasRef.current, {
      height: height, // 초기 크기는 사다리형
      width: width,
    });

    const addPlainBlocks = () => {
      canvas.current.add(bg);

      if (height > width) {
        // 사다리형
        for (let i = 0; i < 4; i++) {
          const t = 19 + i * 120;
          console.log(t);
          canvas.current.add(VerticalPlainBlock(19, 19 + i * 120));
        }
      } else {
        // 창문형
        for (let i = 0; i < 4; i++) {
          canvas.current.add(
            HorizontalPlainBlock(
              32 + (i % 2) * 229,
              29 + Math.floor(i / 2) * 176
            )
          );
        }
      }
    };

    addPlainBlocks();

    return () => {
      canvas.current.dispose();
    };
  }, [width, height, bg]); // width, height, bg 바뀔 때마다 리렌더

  // handleDownload 함수를 통해 캔버스 이미지를 다운로드할 수 있습니다
  function handleDownload() {
    const dataURL = canvas.current.toDataURL("image/png");
    const link = document.createElement("a");
    link.download = "frame.png";
    link.href = dataURL;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  return (
    <>
      <canvas ref={canvasRef} className="createCanvas" id="canvas" />
      <button onClick={handleDownload}>다운로드</button>
    </>
  );
};

export default CanvasArea;

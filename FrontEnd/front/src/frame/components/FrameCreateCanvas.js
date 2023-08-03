// 프레임의 기초가 되는 골격을 잡는 툴바의 디테일 컴포넌트입니다.
import React, { useEffect, useRef } from "react";
import "../css/FrameCreateCanvas.css";
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

// Canvas를 만들기 시작
const CanvasArea = () => {
  const canvasRef = useRef(null);
  const canvas = useRef(null);
  // store에서 canvas에 사용할 재료들을 가져옴
  const { width, height, bgColor, bgImg } = useSelector((store) => store.frame);
  // 배경 만들기
  console.log(width, height, bgColor, bgImg);

  useEffect(() => {
    // useEffect를 사용하여 캔버스를 초기화하고 사다리형과 창문형에 맞게 투명한 블록들을 추가합니다. height와 width의 변화에 따라 캔버스의 크기를 조정합니다.
    canvas.current = new fabric.Canvas(canvasRef.current, {
      height: height, // 초기 크기는 사다리형
      width: width,
    });
    // 배경 만들기
    const makeBackground = (bgImg, width, height, bgColor) => {
      return new Promise((resolve, reject) => {
        if (bgImg !== false) {
          fabric.Image.fromURL(
            bgImg,
            function (Img) {
              // 업로드된 이미지가 프레임 보다 작으면 확대합니다
              if (Img.height < height) {
                Img.scaleToHeight(height);
              }
              if (Img.width < width) {
                Img.scaleToWidth(width);
              }
              Img.set({
                lockMovementX: true, // 움직이지 않도록 합니다
                lockMovementY: true,
                lockRotation: true,
                selectable: false, // 선택 불가능
              });
              resolve(Img);
            },
            null,
            { crossOrigin: "Anonymous" }
          ); // CORS 이슈를 처리하기 위한 옵션
        } else {
          const rect = new fabric.Rect({
            width: width,
            height: height,
            left: 0,
            top: 0,
            fill: bgColor,
            lockMovementX: true,
            lockMovementY: true,
            lockRotation: true,
            selectable: false,
          });
          resolve(rect);
        }
      });
    };

    makeBackground(bgImg, width, height, bgColor)
      .then((bg) => {
        canvas.current.add(bg);
        addPlainBlocks();
      })
      .catch((error) => {
        console.error(error);
      });

    // 만들어진 배경에 빈칸 뚫기
    const addPlainBlocks = () => {
      if (height > width) {
        // 사다리형
        for (let i = 0; i < 4; i++) {
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
  }, [width, height, bgColor, bgImg]); // width, height, bg 바뀔 때마다 리렌더

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
    <div className="canvasBackground">
      <canvas
        ref={canvasRef}
        className="createCanvas"
        name="canvas"
        id="canvas"
      />
      <button onClick={handleDownload}>다운로드</button>
    </div>
  );
};

export default CanvasArea;

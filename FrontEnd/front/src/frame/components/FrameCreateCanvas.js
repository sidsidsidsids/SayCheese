// 프레임을 만드는 캔버스 영역 컴포넌트입니다.
import React, { useState, useEffect, useRef } from "react";
import "../css/FrameCreateCanvas.css";
// third party
import { fabric } from "fabric";

import { useSelector } from "react-redux";

// handleDownload 함수를 통해 캔버스 이미지를 다운로드할 수 있습니다
function handleDownload(canvas) {
  const dataURL = canvas.toDataURL("image/png");
  const link = document.createElement("a");
  link.download = "frame.png";
  link.href = dataURL;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// 이미지 배경 만들기 함수입니다
const makeBackground = (bgImg, width, height, bgColor, canvas) => {
  return new Promise((resolve, reject) => {
    if (bgImg !== "false") {
      fabric.Image.fromURL(
        bgImg,
        function (Img) {
          // 업로드된 이미지가 프레임 규격보다 작으면 확대합니다
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
      // Properly set the background color for canvas when there's no bgImg
      canvas.setBackgroundColor(bgColor, () => {
        // 이러한 변경 사항을 적용하면 캔버스 객체가 null인 상태에서 메서드를 호출하는 문제를 방지할 수 있을 것입니다.
        // null 일때 에러가 남.주의 필요함.
        if (canvas) canvas.renderAll.bind(canvas);
      });
      alert("배경 이미지 다시 선택해서 제출해주세요");
      resolve(null);
    }
  });
};

// 만들어진 배경에 투명한 네모칸 뚫기
const addPlainBlocks = (canvas, height, width) => {
  if (canvas) {
    if (height > width) {
      // 사다리형
      for (let i = 0; i < 4; i++) {
        canvas.add(VerticalPlainBlock(19, 19 + i * 120));
      }
    } else {
      // 창문형
      for (let i = 0; i < 4; i++) {
        canvas.add(
          HorizontalPlainBlock(32 + (i % 2) * 229, 29 + Math.floor(i / 2) * 176)
        );
      }
    }
  }
};

//사다리 기본형 프레임 투명한 네모칸 만드는 함수입니다
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

// 창문형 프레임 투명한 네모칸 만드는 함수입니다
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

// 프레임 꾸미기 함수입니다
const DecorateObjects = (objects, canvas) => {
  console.log(objects.length);
  if (objects.length > 0 && canvas) {
    canvas.renderOnAddRemove = false; // 추가된 객체가 자동으로 렌더링되지 않도록 설정합니다.
    // objects.forEach((item, index) => {
    //   fabric.Image.fromURL(item, function (Img) {
    //     Img.set({
    //       scaleToHeight: 20,
    //     });
    const object = objects;
    fabric.Image.fromURL(object, function (Img) {
      Img.set({
        scaleToHeight: 1,
      });
      canvas.add(Img);
      console.log("이미지 추가:");
      canvas.renderAll(); // 객체가 추가된 후 수동으로 렌더링합니다.
    });
  }
  canvas.renderOnAddRemove = true; // 렌더링 설정을 원래대로 복원합니다.
};

// 프레임 꾸미기 제거 함수입니다.
const UndecorateObjects = (canvas) => {
  if (canvas) {
    // canvas 유효성 검사

    let activeObjects = canvas.getActiveObjects(); // getActiveObject()가 아닌 getActiveObjects()로 수정
    if (activeObjects) {
      activeObjects.forEach(function (object) {
        // canvas.activeObjects가 아닌 activeObjects로 수정
        canvas.remove(object);
      }); // forEach 함수 닫는 괄호 추가
    }
  }
};

// 프레임 텍스트 꾸미기 함수입니다
//{customText: '', customTextColor: '#fff', customTextSize: '20', customTextFont: 'Roboto'}
const DecorateText = (text, canvas) => {
  if (text) {
    canvas.add(
      new fabric.Text(text.customText, {
        fontFamily: text.customTextFont,
        fontSize: text.customTextSize,
        fill: text.customTextColor,
      })
    );
  }
};
// 캔버스 드로잉 함수입니다
const addDrawing = (canvas, brush, drawingMode) => {
  if (canvas) {
    if (drawingMode) {
      // 캔버스의 드로잉 모드를 true로 바꿔줍니다
      // canvas.freeDrawingBrush = new fabric.PencilBrush(canvas);
      const freebrush = new fabric.PencilBrush(canvas);

      if (canvas.freeDrawingBrush) {
        console.log(brush.brushColor);
        freebrush.color = brush.brushColor;
        freebrush.width = brush.brushValue;
        freebrush.shadow = new fabric.Shadow({
          blur: parseInt(brush.brushShadowValue, 10) || 0,
          offsetX: parseInt(brush.brushShadowOffset, 10) || 0,
          offsetY: parseInt(brush.brushShadowOffset, 10) || 0,
          affectStroke: true,
          color: brush.brushShadowColor, // Corrected property name
        });

        canvas.freeDrawingBrush = freebrush;
        canvas.freeDrawingBrush.inverted = true;
        // canvas.isDrawingMode = true; // Enable drawing mode
        // canvas.requestRenderAll(); // Refresh the canvas
      }
    }
  }
};

// 브러시 이미지 생성

// Canvas
const CanvasArea = () => {
  const canvasRef = useRef(null);
  const [canvasInstance, setCanvasInstance] = useState(null);

  // store에서 canvas에 사용할 재료들을 가져옴
  const { width, height, bgColor, bgImg, objects, text, drawingMode, brush } =
    useSelector((store) => store.frame);

  useEffect(() => {
    // useEffect를 사용하여 캔버스를 초기화하고 사다리형과 창문형에 맞게 투명한 블록들을 추가합니다. height와 width의 변화에 따라 캔버스의 크기를 조정합니다.
    const newCanvas = new fabric.Canvas(canvasRef.current, {
      height: height,
      width: width,
      hoverCursor: "pointer",
    });

    // 컬러 백그라운드 만들기
    if (bgColor) {
      newCanvas.backgroundColor = bgColor;
      addPlainBlocks(newCanvas, height, width);
    }

    // 이미지 있으면 이미지 백그라운드 만들기
    if (bgImg) {
      // bgImg가 유효한 이미지 URL일 때만 실행
      makeBackground(bgImg, width, height, bgColor, newCanvas)
        .then((bg) => {
          newCanvas.add(bg);
          addPlainBlocks(newCanvas, height, width);
        })
        .catch((error) => {
          console.error(error);
        });
    }

    setCanvasInstance(newCanvas, brush);

    return () => {
      // `newCanvas`가 유효한지 확인하고
      if (newCanvas) {
        newCanvas.dispose();
      }
    };
  }, [width, height, bgColor, bgImg]); // width, height, bg 바뀔 때마다 리렌더

  // DecorateObjects를 호출하기 전에 newCanvas가 유효한지 확인합니다.

  useEffect(() => {
    if (canvasInstance) {
      DecorateObjects(objects, canvasInstance);
    }
  }, [objects]); // objects가 바뀔 때만 리렌더합

  useEffect(() => {
    if (canvasInstance) {
      DecorateText(text, canvasInstance);
    }
  }, [text]); // text가 바뀔 때만 리렌더합
  // 드로잉 모드가 실행되는지 지켜보고 있다가 캔버스의 속성을 바꿉니다
  useEffect(() => {
    if (canvasInstance) {
      console.log(drawingMode);
      if (!drawingMode) {
        canvasInstance.isDrawingMode = drawingMode;
      } else {
        canvasInstance.isDrawingMode = drawingMode;
        addDrawing(canvasInstance, brush, drawingMode);
      }
    }
  }, [drawingMode]);
  // 드로잉 모드가 실행되었을 brush 데이터가 봐뀌면 brush를 바꿉니다
  useEffect(() => {
    if (canvasInstance) {
      if (drawingMode) {
        addDrawing(canvasInstance, brush, drawingMode);
      }
    }
  }, [brush]);

  return (
    <div className="canvasBackground">
      <canvas
        ref={canvasRef}
        className="createCanvas"
        name="canvas"
        id="canvas"
      />
      <button onClick={() => handleDownload(canvasInstance)}>다운로드</button>
      <br />
      <div onClick={() => UndecorateObjects(canvasInstance)}>지우기</div>
    </div>
  );
};

export default CanvasArea;

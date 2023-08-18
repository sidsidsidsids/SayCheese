// 프레임을 만드는 캔버스 영역 컴포넌트입니다.
import React, { useState, useEffect, useRef } from "react";
// third party
import { debounce } from "lodash";
import { fabric } from "fabric";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import Swal from "sweetalert2";

import { LuRotateCcw, LuRotateCw } from "react-icons/lu";
import { ResetSignal } from "../../redux/features/frame/frameSlice";
import "../css/FrameCreateCanvas.css";

/*
프레임을 만들기 위해서는
STEP 1. 배경을 만듭니다
STEP 2. 이미지를 스티커처럼 붙여서 꾸밉니다
STEP 3. 글자를 스티커처럼 붙여서 꾸밉니다
STEP 4. 포스트하거나 로컬에 저장합니다
*/

// Canvas에 사용될 tool 함수들 //
// STEP 1-1. 이미지 배경 만들기 함수입니다
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
            selectable: false, // 마우스 선택 불가능
          });
          resolve(Img);
        },
        null,
        { crossOrigin: "Anonymous" }
      ); // CORS 이슈를 처리하기 위한 옵션
    } else {
      // Properly set the background color for canvas when there's no bgImg
      canvas.backgroundColor = bgColor;
      alert("배경 이미지 다시 선택해서 제출해주세요");
      resolve(null);
    }
  });
};

// STEP 2-2. 만들어진 배경에 얼굴이 나올 투명칸을 만듭니다.

// 가장 기본형 사각형 프레임 투명칸을 만드는 함수입니다
const addPlainBlocks = (canvas, height, width) => {
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
      selectable: false, // 마우스 선택 불가능
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
      selectable: false, // 마우스 선택 불가능
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
    });

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

// 가장 기본형 사각형 프레임 투명칸을 만드는 함수입니다
const addSmoothPlainBlocks = (canvas, height, width) => {
  //사다리 기본형 프레임 투명한 네모칸 만드는 함수입니다
  const VerticalSmoothPlainBlock = (left, top) =>
    new fabric.Rect({
      left: left,
      top: top,
      width: 170,
      height: 114,
      fill: "#7767AC",
      rx: 10,
      ry: 10,
      lockMovementX: true, // 움직이지 않도록 합니다
      lockMovementY: true,
      lockRotation: true,
      selectable: false, // 마우스 선택 불가능
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
    });

  // 창문형 프레임 투명한 네모칸 만드는 함수입니다
  const HorizontalSmoothPlainBlock = (left, top) =>
    new fabric.Rect({
      left: left,
      top: top,
      width: 217,
      height: 165,
      fill: "#7767AC",
      rx: 10,
      ry: 10,
      lockMovementX: true, // 움직이지 않도록 합니다
      lockMovementY: true,
      lockRotation: true,
      selectable: false, // 마우스 선택 불가능
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
    });

  if (canvas) {
    if (height > width) {
      // 사다리형
      for (let i = 0; i < 4; i++) {
        canvas.add(VerticalSmoothPlainBlock(19, 19 + i * 120));
      }
    } else {
      // 창문형
      for (let i = 0; i < 4; i++) {
        canvas.add(
          HorizontalSmoothPlainBlock(
            32 + (i % 2) * 229,
            29 + Math.floor(i / 2) * 176
          )
        );
      }
    }
  }
};

// 원형 투명칸 만드는 함수 입니다
const addCircleBlocks = (canvas, height, width) => {
  // 원형 네모칸 만드는 함수입니다
  const VerticalCircleBlock = (left, top) =>
    new fabric.Ellipse({
      left: left,
      top: top,
      // scaleX: width,
      // scaleY: height,
      rx: width / 2.42, // x축 반지름 (가로 길이의 절반)
      ry: height / 10.6, // y축 반지름 (세로 길이의 절반)
      radius: 70,
      fill: "#7767AC",
      lockMovementX: true, // 움직이지 않도록 합니다
      lockMovementY: true,
      lockRotation: true,
      selectable: false, // 마우스 선택 불가능
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
    });

  // 창문형 프레임 투명한 네모칸 만드는 함수입니다 (width, height) = 217, 165
  const HorizontalCircleBlock = (left, top) =>
    new fabric.Ellipse({
      left: left,
      top: top,
      // scaleX: width,
      // scaleY: height,
      rx: 111, // x축 반지름 (가로 길이의 절반)
      ry: 81, // y축 반지름 (세로 길이의 절반)
      radius: 70,
      fill: "#7767AC",
      lockMovementX: true, // 움직이지 않도록 합니다
      lockMovementY: true,
      lockRotation: true,
      selectable: false, // 마우스 선택 불가능
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
    });

  if (canvas) {
    if (height > width) {
      // 사다리형
      for (let i = 0; i < 4; i++) {
        canvas.add(VerticalCircleBlock(17, 19 + i * 120));
      }
    } else {
      // 창문형
      for (let i = 0; i < 4; i++) {
        canvas.add(
          HorizontalCircleBlock(
            30 + (i % 2) * 229,
            29 + Math.floor(i / 2) * 176
          )
        );
      }
    }
  }
};

// 하트모양 투명칸 만드는 함수 입니다
const addHeartBlocks = (canvas, height, width) => {
  // 원형 네모칸 만드는 함수입니다

  const VerticalHeartBlock = (left, top) => {
    let heartPath = new fabric.Path(
      "M352.92,80C288,80,256,144,256,144s-32-64-96.92-64C106.32,80,64.54,124.14,64,176.81c-1.1,109.33,86.73,187.08,183,252.42a16,16,0,0,0,18,0c96.26-65.34,184.09-143.09,183-252.42C447.46,124.14,405.68,80,352.92,80Z"
    );

    const scaleWidth = 170 / heartPath.width;
    const scaleHeight = 111 / heartPath.height;
    heartPath.set({
      left: left,
      top: top,
      scaleX: scaleWidth,
      scaleY: scaleHeight,
      fill: "#7767AC",
      lockMovementX: true, // 움직이지 않도록 합니다
      lockMovementY: true,
      lockRotation: true,
      selectable: false, // 마우스 선택 불가능
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
    });
    return heartPath;
  };

  // 창문형 프레임 투명한 네모칸 만드는 함수입니다 (width, height) = 217, 165
  const HorizontalHeartBlock = (left, top) => {
    let heartPath = new fabric.Path(
      "M352.92,80C288,80,256,144,256,144s-32-64-96.92-64C106.32,80,64.54,124.14,64,176.81c-1.1,109.33,86.73,187.08,183,252.42a16,16,0,0,0,18,0c96.26-65.34,184.09-143.09,183-252.42C447.46,124.14,405.68,80,352.92,80Z"
    );

    const scaleWidth = 217 / heartPath.width;
    const scaleHeight = 165 / heartPath.height;
    heartPath.set({
      left: left,
      top: top,
      scaleX: scaleWidth,
      scaleY: scaleHeight,
      fill: "#7767AC",
      lockMovementX: true, // 움직이지 않도록 합니다
      lockMovementY: true,
      lockRotation: true,
      selectable: false, // 마우스 선택 불가능
      globalCompositeOperation: "destination-out", // 이 도형이 겹쳐지는 부분은 사라집니다
    });
    return heartPath;
  };

  if (canvas) {
    if (height > width) {
      // 사다리형
      for (let i = 0; i < 4; i++) {
        canvas.add(VerticalHeartBlock(19, 20 + i * 120));
      }
    } else {
      // 창문형
      for (let i = 0; i < 4; i++) {
        canvas.add(
          HorizontalHeartBlock(30 + (i % 2) * 229, 29 + Math.floor(i / 2) * 176)
        );
      }
    }
  }
};

// STEP 2. 이미지로 프레임을 꾸미기 함수입니다
const DecorateObjects = (objects, canvas) => {
  if (objects && canvas) {
    canvas.renderOnAddRemove = false; // 추가된 객체가 자동으로 렌더링되지 않도록 설정합니다.

    fabric.Image.fromURL(objects, function (Img) {
      // 캔버스 크기에 적절하게 이미지 오브젝트를 로드하기 위해 필요한 과정 입니다
      // 이미지 비율을 구해서 캔버스 비율과 비교하여 scale을 조정할 것 입니다
      const imgAspectRatio = Img.width / Img.height;

      // canvas의 가로 세로 비율
      const canvasAspectRatio = canvas.width / canvas.height;
      let scaleX, scaleY;

      if (imgAspectRatio > canvasAspectRatio) {
        // 이미지의 가로 비율이 더 큰 경우
        scaleX = canvas.width / Img.width;
        scaleY = canvas.width / Img.width;
      } else {
        // 이미지의 세로 비율이 더 큰 경우
        scaleX = canvas.height / Img.height;
        scaleY = canvas.height / Img.height;
      }

      Img.set({
        scaleX: scaleX,
        scaleY: scaleY,
      });
      canvas.add(Img);
      canvas.renderAll(); // 객체가 추가된 후 수동으로 렌더링합니다.
    });
  }
  canvas.renderOnAddRemove = true; // 렌더링 설정을 원래대로 복원합니다.
};

// 프레임 꾸미기 제거 함수입니다.
const UndecorateObjects = (canvas) => {
  if (canvas) {
    let activeObjects = canvas.getActiveObjects(); // 다중 선택 삭제를 위해 getActiveObject()가 아닌 getActiveObjects()로 수정
    if (activeObjects) {
      activeObjects.forEach(function (object) {
        canvas.remove(object);
      });
    }
  }
};

// 프레임 텍스트 꾸미기 함수입니다
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

// STEP4.
// STEP4-1. handleDownload 함수를 통해 캔버스 이미지를 다운로드할 수 있습니다
function handleDownload(canvas) {
  const dataURL = canvas.toDataURL("image/png");
  const link = document.createElement("a");
  link.download = "frame.png";
  link.href = dataURL;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// STEP4-2. 사용자는 서버에 프레임을 업로드 할 수 있습니다.
function handleUpload(canvas, frameInfo, frameSpecification) {
  let preSignUrl = ""; // 여기서만 사용하니까 LET 가능
  let fileName = "";
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    axios
      .post(
        "/api/amazon/presigned",
        {
          fileName: `${frameInfo.frameName}.png`,
          fileType: "frame",
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        }
      )
      .then(function (response) {
        fileName = response.data.fileName;
        // base64 형태 url을 가진 image를 File 객체로
        // imageURL : data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAFIQAAA...
        const dataURL = canvas.toDataURL("image/png");
        const binaryImageData = atob(dataURL.split(",")[1]);
        const arrayBufferData = new Uint8Array(binaryImageData.length);
        for (let i = 0; i < binaryImageData.length; i++) {
          arrayBufferData[i] = binaryImageData.charCodeAt(i);
        }
        const blob = new Blob([arrayBufferData], { type: "image/png" });
        const imageFile = new File([blob], `${frameInfo.frameName}.png`, {
          type: "image/png",
        });
        preSignUrl = response.data.preSignUrl;
        fetch(preSignUrl, {
          method: "PUT",
          headers: {
            "Content-Type": " image/png",
          },
          body: imageFile,
        }).then(function (response) {
          axios.post(
            "/api/article/frame",
            {
              name: fileName,
              fileType: "frame",
              isPublic: !frameInfo.privateCheck,
              frameSpecification: frameSpecification,
              subject: frameInfo.frameName,
            },
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `${accessToken}`,
              },
            }
          );
        });
      });
  }
}

// Canvas //
const CanvasArea = () => {
  const canvasRef = useRef(null);
  const [canvasInstance, setCanvasInstance] = useState(null);
  const dispatch = useDispatch();
  const {
    width,
    height,
    bgColor,
    block,
    bgImg,
    objects,
    text,
    drawingMode,
    brush,
    deleteSignal,
    downloadSignal,
    frameInfo,
    postSignal,
  } = useSelector((store) => store.frame); // store에서 canvas에 사용할 재료들을 가져옴
  let frameSpecification = ""; // DOM을 조작해야하는 데이터가 아니라면 일반 변수로 사용하는 것이 관리하기 낫다
  let blockSpecification = "Plain"; // 투명한 칸이 어떠한 모양인지 담고있는 변수입니다 종류는 Plain, SmoothPlain, Circle
  if (block) {
    blockSpecification = block;
  }
  const [isRedoing, setIsRedoing] = useState(false);
  const [history, setHistory] = useState([]);
  const [redoHistory, setRedoHistory] = useState([]);
  const undo = () => {
    // undo는 history stack을 이용하여 합니다
    if (!bgImg) {
      // 사용자  배경 이미지가 없다면
      if (canvasInstance._objects.length > 4) {
        // 기본 생성되는 색 배경(1) + 투명 4칸(4) = 5번의 동작 이후의 변화만 redo undo 할 수 있도록 5번 이후의 기록만 사용합니다
        const removedObject = canvasInstance._objects.pop(); // removedObject는 undo 하는 바로 최근의 활동입니다
        setRedoHistory([...redoHistory, removedObject]); // redo stack 마지막에 removedObject를 추가합니다
        canvasInstance.renderAll();
      }
    } else {
      // 사용자 배경 이미지가 있다면
      if (canvasInstance._objects.length > 9) {
        // 색 배경(1) + 투명 4칸(4) + 이미지 배경(1) + 투명 4칸(4) = 9이후의 변화만 redo undo 할 수 있도록 10번 이후의 기록만 사용합니다
        const removedObject = canvasInstance._objects.pop(); // removedObject는 undo 하는 바로 최근의 활동입니다
        setRedoHistory([...redoHistory, removedObject]); // redo stack 마지막에 removedObject를 추가합니다
        canvasInstance.renderAll();
      }
    }
  };

  const redo = () => {
    if (redoHistory.length > 0) {
      setIsRedoing(true);
      const lastObject = redoHistory.pop(); // RedoHistory 마지막 값을 가져와 다시 실행합니다.
      canvasInstance.add(lastObject);
      canvasInstance.renderAll();
    }
  };

  async function handleSpecification() {
    if (width > height) {
      frameSpecification = "horizontal";
    } else {
      frameSpecification = "vertical";
    }
  }

  const handleObjectAdded = () => {
    if (!isRedoing) {
      setHistory([]);
    }
    setIsRedoing(false);
  };
  const makeHistoryEmpty = () => {
    setHistory([]);
    setRedoHistory([]);
  };

  useEffect(() => {
    // useEffect를 사용하여 캔버스를 초기화하고 사다리형과 창문형에 맞게 투명한 블록들을 추가합니다. height와 width의 변화에 따라 캔버스의 크기를 조정합니다.
    const newCanvas = new fabric.Canvas(canvasRef.current, {
      height: height,
      width: width,
      hoverCursor: "pointer",
    });
    // useEffect를 사용하여 캔버스를 초기화하고 사다리형과 창문형에 맞게 투명한 블록들을 추가합니다. height와 width의 변화에 따라 캔버스의 크기를 조정합니다.
    //위의 코드는 리렌더링을 일으키지 않도록 이펙트 내에 두어야 합니다. 안 그러면 too many re redner 에러가 납니다
    makeHistoryEmpty(); // 캔버스 재생성 될 때마다 history 기억을 비운다

    // Create a debounced function to handle canvas updates
    const debouncedCanvasUpdate = debounce((bgColor) => {
      // Your canvas update logic here
      // This function will be called after a short delay when there are no more rapid changes

      if (newCanvas && bgImg) {
        newCanvas.backgroundColor = bgColor;
        makeBackground(bgImg, width, height, bgColor, newCanvas)
          .then((bg) => {
            newCanvas.add(bg);
            setHistory([]);
            handleSpecification();
          })
          .then(() => {
            // 이미지 있으면 이미지 백그라운드 만들기
            if (newCanvas && bgImg) {
              // 빈 캔버스에 투명칸을 만들수 없다는 에러를 해결하기 위해 조건문으로 작성
              if (newCanvas._objects.length > 0) {
                if (block === "Heart") {
                  addHeartBlocks(newCanvas, height, width);
                } else if (block === "SmoothPlain") {
                  addSmoothPlainBlocks(newCanvas, height, width);
                } else if (block === "Circle") {
                  addCircleBlocks(newCanvas, height, width);
                } else {
                  addPlainBlocks(newCanvas, height, width);
                }
              }
            }
          });
      }
    }, 300); // Adjust the debounce delay as needed

    // 컬러 백그라운드 만들기
    if (newCanvas && bgColor) {
      newCanvas.backgroundColor = bgColor;
      if (newCanvas.backgroundColor) {
        if (block === "Heart") {
          addHeartBlocks(newCanvas, height, width);
        } else if (block === "SmoothPlain") {
          addSmoothPlainBlocks(newCanvas, height, width);
        } else if (block === "Circle") {
          addCircleBlocks(newCanvas, height, width);
        } else {
          addPlainBlocks(newCanvas, height, width);
        }
      }

      handleSpecification();
    }
    if (newCanvas && bgImg) {
      debouncedCanvasUpdate(bgColor);
    }

    // Swal.fire("천천히 스크롤");

    // 객체 추가 또는 수정 시 상태 저장

    // canvasInstance.on("object:modified", handleObjectAdded);

    setCanvasInstance(newCanvas);
    // 캔버스 객체 초기화

    handleSpecification();
    return () => {
      // `newCanvas`가 유효한지 확인하고
      if (newCanvas) {
        newCanvas.dispose();
      }
    };
  }, [width, height, bgColor, bgImg, block]); // width, height, bg 바뀔 때마다 리렌더

  // 객체 추가 또는 수정 시 상태 저장
  useEffect(() => {
    if (canvasInstance) {
      canvasInstance.on("object:added", () => {
        handleObjectAdded();
      });
      canvasInstance.on("object:modified", () => {
        handleObjectAdded();
      });
    }
  }, [canvasInstance]); // 캔버스가 바뀔때마다 HISTORY STACK에 추가합니다

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

  // 지우기 요청이 들어오면  UndecorateObjects(canvasInstance)를 실행합니다
  useEffect(() => {
    async function remove() {
      UndecorateObjects(canvasInstance);
      return "success";
    }
    if (deleteSignal && canvasInstance) {
      remove().then(() => dispatch(ResetSignal("deleteSignal")));
    }
  }, [deleteSignal]);

  // 다운로드 요청이 들어오면 handleDownload(canvasInstance)를 실행합니다
  useEffect(() => {
    async function download() {
      handleDownload(canvasInstance);
      return "success";
    }
    if (downloadSignal && canvasInstance) {
      download().then(() => dispatch(ResetSignal("downloadSignal")));
    }
  }, [downloadSignal]);

  // 업로드 요청이 들어오면 handleUpload(canvasInstance)를 실행합니다
  useEffect(() => {
    async function upload() {
      handleUpload(canvasInstance, frameInfo, frameSpecification);
      return "success";
    }
    if (postSignal && canvasInstance) {
      handleSpecification() // 프레임규격을 검사하고 나서
        .then(() => upload()) // 업로드 하고
        .then(() => dispatch(ResetSignal("postSignal"))); // 업로드 요청 시그널 다시 0으로 리셋합니다
    }
  }, [postSignal, frameSpecification]);

  return (
    <div>
      <div className="canvasBackground">
        <canvas
          ref={canvasRef}
          className="createCanvas"
          name="canvas"
          id="canvas"
        />
      </div>
      <div
        id="redoUndo"
        // className="twoButtonAlign"
      >
        <LuRotateCcw
          onClick={(event) => {
            event.stopPropagation();
            undo();
          }}
        />
        <LuRotateCw
          onClick={(event) => {
            event.stopPropagation();
            redo();
          }}
        />
      </div>
      {/* <button onClick={() => RemoveWhiteBackground(canvasInstance)} /> */}
    </div>
  );
};

export default CanvasArea;

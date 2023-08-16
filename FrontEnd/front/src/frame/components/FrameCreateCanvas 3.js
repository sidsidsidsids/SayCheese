// 프레임을 만드는 캔버스 영역 컴포넌트입니다.
import React, { useState, useEffect, useRef } from "react";
// third party
import { fabric } from "fabric";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import { ResetSignal } from "../../redux/features/frame/frameSlice";
// CSS
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
    if (bgImg) {
      // 배경 이미지가 있으면
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
      // 배경 이미지가 없으면
      // Properly set the background color for canvas when there's no bgImg
      canvas.setBackgroundColor(bgColor, () => {
        // (주의)캔버스가 null 이라는 에러가 자주 남
        // 캔버스 객체가 null인 상태에서 메서드를 호출하는 문제를 방지기 위해
        if (canvas) canvas.renderAll.bind(canvas);
      });
      // alert("배경 이미지 다시 선택해서 제출해주세요");
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
      rx: width / 2.3, // x축 반지름 (가로 길이의 절반)
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
      ry: 84, // y축 반지름 (세로 길이의 절반)
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
        canvas.add(VerticalCircleBlock(13, 19 + i * 120));
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

// STEP 2. 이미지로 프레임을 꾸미기 함수입니다
const DecorateObjects = (objects, canvas) => {
  if (objects && canvas) {
    canvas.renderOnAddRemove = false; // 추가된 객체가 자동으로 렌더링되지 않도록 설정합니다.
    const object = objects;
    fabric.Image.fromURL(object, function (Img) {
      // 아래는 큰 첨부 사진을 캔버스에 맞게 조정하기 위한 로직
      const imgAspectRatio = Img.width / Img.height;
      // canvas의 가로 세로 비율
      const canvasAspectRatio = canvas.width / canvas.height;
      let scaleX, scaleY;
      if (imgAspectRatio > canvasAspectRatio) {
        // 이미지의 가로 비율이 더 큰 경우
        scaleX = canvas.width / Img.width;
        scaleY = canvas.width / Img.width; // 가로 비율을 유지하기 위해 같은 값을 사용
      } else {
        // 이미지의 세로 비율이 더 큰 경우
        scaleX = canvas.height / Img.height;
        scaleY = canvas.height / Img.height; // 세로 비율을 유지하기 위해 같은 값을 사용
      }
      Img.set({
        // scaleX: canvas.width / Img.width,
        scaleX: scaleX,
        scaleY: scaleY,
        left: 19,
        top: 10,
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
  console.log("업로드 시작");
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
          console.log("url에 이미지 올렸음");
          console.log({
            name: fileName,
            fileType: "frame",
            isPublic: !frameInfo.privateCheck,
            frameSpecification: frameSpecification,
            subject: frameInfo.frameName,
          });
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
  const [frameSpecification, setFrameSpecification] = useState("vertical");
  const dispatch = useDispatch();
  const [doing, setDoing] = useState(true);
  const [undoHistory, setUndoHistory] = useState([]);
  const [redoHistory, setRedoHistory] = useState([]);
  // store에서 canvas에 사용할 재료들을 가져옴
  const {
    width,
    height,
    bgColor,
    bgImg,
    objects,
    text,
    drawingMode,
    brush,
    deleteSignal,
    downloadSignal,
    frameInfo,
    postSignal,
  } = useSelector((store) => store.frame);

  useEffect(() => {
    console.log("undohistory length in useEffect:", undoHistory.length);
    // undoHistory 상태가 변경될 때마다 실행되는 로직을 작성할 수 있습니다.
    // 예: undoHistory의 길이에 따라 적절한 동작 수행
    // 예: undoHistory가 변경될 때 로그 출력 등
    console.log("현재 리두잉은", doing);
  }, [undoHistory.length]);

  useEffect(() => {
    console.log("Redohistory length in useEffect:", redoHistory.length);
    // undoHistory 상태가 변경될 때마다 실행되는 로직을 작성할 수 있습니다.
    // 예: undoHistory의 길이에 따라 적절한 동작 수행
    // 예: undoHistory가 변경될 때 로그 출력 등
    console.log("현재 리두잉은", doing);
  }, [redoHistory.length]);

  async function doingIsTrue() {
    console.log("2. doing을 true로 만드는 동기함수를 실행합니다");
    setDoing(true);
  }
  async function undo() {
    console.log("1. 언두 클릭함");
    setDoing(true);
    if (canvasInstance && undoHistory.length > 1) {
      // canvasInstance.off("object:added", handleObjectAdded);
      // canvasInstance.off("object:modified", handleObjectAdded);
      // canvasInstance.on("object:added", () => {
      //   console.log("제발");
      // });
      // canvasInstance.on("object:modified", () => {
      //   console.log("제발");
      // });

      await canvasInstance.off("object:added", handleObjectAdded);
      await canvasInstance.off("object:modified", handleObjectAdded);

      const lastObject = undoHistory.pop();
      const prevObject = undoHistory[undoHistory.length - 1];

      setRedoHistory([...redoHistory, lastObject]);

      await canvasInstance.clear();
      await canvasInstance.loadFromJSON(prevObject, () => {
        canvasInstance.renderAll();
      });
    } else {
      console.warn("Nothing to undo.");
    }
  }
  useEffect(() => {
    console.log("0. doing을 지켜보는 useEffect doing:", doing);
  }, [doing]);

  async function redo() {
    setDoing(true);
    if (canvasInstance && redoHistory.length > 0) {
      const nextObject = redoHistory.pop();
      setUndoHistory([...undoHistory, nextObject]);

      await canvasInstance.clear();
      await canvasInstance.loadFromJSON(nextObject, () => {
        canvasInstance.renderAll();
      });
    } else {
      console.warn("Nothing to redo.");
    }
    // setDoing(false);
  }

  function handleObjectAdded() {
    console.log(
      `handleObjectAdded ${doing}${!doing ? "실행 안될거고" : "실행될거고"}`
    );
    if (!doing) {
      console.log("히스토리 기억에 추가");
      // 현재 상태의 JSON 표현을 새로운 상태로 저장
      const newState = JSON.stringify(
        canvasInstance.toJSON([
          "selectable",
          "globalCompositeOperation",
          "lockMovementX",
          "lockMovementY",
          "lockRotation",
          "lockScalingX",
          "lockScalingY",
        ]) // 속성이 유지되기 위해 이 속성값들을 꼭 추가해야합니다
      );
      // 기존 상태에 새로운 상태 추가
      setUndoHistory((prevUndoHistory) => [...prevUndoHistory, newState]);
      setRedoHistory([]);
    }
  }
  const makeHistoryEmpty = () => {
    setUndoHistory([]);
    setRedoHistory([]);
    console.log("reset");
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
    console.log("★★★★★★★★★★★★★★★★ 첫 이펙트 ★★★★★★★★★★★★★★");
    // 컬러 백그라운드 만들기
    if (newCanvas && bgColor) {
      try {
        makeBackground(bgImg, width, height, bgColor, newCanvas);
        addPlainBlocks(newCanvas, height, width);
        setDoing(false);

        // setDoing(false);
        console.log(
          `1. (예상값은 펄스인데 트루가 나온다면 비동기라 그런가봐요)캔버스 새로 생성하고 doing ${doing}으로 만들었어요`
        );
      } catch (error) {
        console.error(error);
      }
    }

    // 이미지 있으면 이미지 백그라운드 만들기
    if (newCanvas && bgImg) {
      // bgImg가 유효한 이미지 URL일 때만 실행
      makeBackground(bgImg, width, height, bgColor, newCanvas)
        .then((bg) => {
          newCanvas.add(bg);
          addPlainBlocks(newCanvas, height, width);
        })
        .then(() => {
          setDoing(false);
          console.log(
            `2. 여기는 그러면 펄스가 꼭나와야 해요 캔버스 새로 생성하고 doing ${doing}으로 만들었어요`
          );
        })
        .catch((error) => {
          console.error(error);
        });
    }

    setCanvasInstance(newCanvas);

    newCanvas.on("object:added", handleObjectAdded);
    newCanvas.on("object:modified", handleObjectAdded);
    console.log("3.", doing);
    return () => {
      // 언마운트 되면
      console.log("언마운트");
      if (newCanvas) {
        newCanvas.off("object:added", handleObjectAdded);
        newCanvas.off("object:modified", handleObjectAdded);
        newCanvas.dispose();
      }
      if (width > height) {
        setFrameSpecification("horizontal");
      }
    };
  }, [width, height, bgColor, bgImg]); // width, height, bg 바뀔 때마다 리렌더

  useEffect(() => {
    makeHistoryEmpty();
  }, [width, height]); // width, height 변할때마다 값 비워줘야함 json 에는 캔버스 그자체 값이 없어서 에러 난다

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
      upload().then(() => dispatch(ResetSignal("postSignal")));
    }
  }, [postSignal]);

  // reUndoTrue 함수
  async function reUndoTrue() {
    setDoing("true");
    console.log("Set 실행");
  }

  return (
    <>
      <div className="canvasBackground">
        <canvas
          ref={canvasRef}
          className="createCanvas"
          name="canvas"
          id="canvas"
        />
      </div>
      <button
        onClick={() => {
          reUndoTrue().then(() => undo().then());
        }}
      >
        Undo
      </button>
      <button
        onClick={() =>
          redo().then(() => {
            console.log("리두 버튼을 눌렀고 isRedo", doing);
            // setDoing(false);
          })
        }
      >
        Redo
      </button>
    </>
  );
};

export default CanvasArea;

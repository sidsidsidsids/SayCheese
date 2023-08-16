// modal 관련  redux tookit 모듈 파일
import { createSlice } from "@reduxjs/toolkit";

// 초기 상태 정의
const initialState = {
  height: 589.6,
  width: 207.8,
  bgColor: "#000000",
  bgImg: false,
  objects: false,
  text: false,
  brush: false,
  drawingMode: false,
  deleteSignal: 0,
  downloadSignal: 0,
  frameInfo: false,
  postSignal: 0,
};

// 액션 생성 함수
const frameSlice = createSlice({
  // 액션 타입 정의
  name: "frame",
  // 초기 상태
  initialState,
  // 리듀서 맵
  reducers: {
    ResetAll: (state) => {
      state.height = 589.6;
      state.width = 207.8;
      state.bgColor = "#000000";
      state.bgImg = false;
      state.objects = false;
      state.text = false;
      state.brush = false;
      state.drawingMode = false;
      state.deleteSignal = 0;
      state.downloadSignal = 0;
      state.frameInfo = false;
      state.postSignal = 0;
    },
    // 프레임 규격
    Resize: (state, action) => {
      state.drawingMode = false;
      state.width = action.payload.width;
      state.height = action.payload.height;
    },
    // 프레임 배경 색과 배경 이미지
    Repaint: (state, action) => {
      if (action.payload.image !== undefined) {
        state.bgImg = action.payload.image;
      }
      if (
        action.payload.color !== undefined &&
        state.bgColor !== action.payload.Color
      ) {
        console.log(action.payload.color);
        state.bgColor = action.payload.color;
      }
    },

    RemoveBgImg: (state) => {
      state.bgImg = false;
    },
    // 프레임 오브젝트
    Decorate: (state, action) => {
      state.objects = action.payload;
    },
    Undecorate: (state) => {
      state.deleteSignal = 1;
      console.log("지워", state.deleteSignal);
    },
    AddText: (state, action) => {
      state.text = action.payload;
    },
    SwitchDrawingMode: (state) => {
      state.drawingMode = !state.drawingMode;
    },
    AddDrawing: (state, action) => {
      state.brush = action.payload;
    },
    DoDownload: (state) => {
      state.downloadSignal = 1;
    },
    ResetSignal: (state, action) => {
      state[action.payload] = 0;
      console.log("(*ᴗ͈ˬᴗ͈)ꕤ*.ﾟ");
      console.log(`state[action.payload] 리셋합니다`);
      /* download 및 delete signal을
      0으로 리셋해야 하는 이유는
      캔버스 리렌더시 
      의도하지 않은 동작이 실행 되는 사이드 이펙트를
      방지하기 위해서입니다 */
    },
    PostSignal: (state, action) => {
      state.frameInfo = action.payload;
      console.log(action.payload);
      state.postSignal = 1;
    },
    ResetPostSignal: (state) => {
      state.postSignal = 0;
    },
  },
});

export const {
  ResetAll,
  Resize,
  Repaint,
  RemoveBgImg,
  Decorate,
  Undecorate,
  AddText,
  AddDrawing,
  SwitchDrawingMode,
  DoDownload,
  ResetSignal,
  PostSignal,
  ResetPostSignal,
} = frameSlice.actions;

export default frameSlice.reducer;

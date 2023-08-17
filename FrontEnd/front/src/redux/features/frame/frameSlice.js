// modal 관련  redux tookit 모듈 파일
import { createSlice } from "@reduxjs/toolkit";

// 초기 상태 정의
const initialState = {
  height: 589.6,
  width: 207.8,
  bgColor: "#000000",
  block: "Plain",
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
      state.block = "Plain";
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
        state.bgColor = action.payload.color;
      }
    },
    // 프레임 투명칸 만들기
    ReBlock: (state, action) => {
      state.block = action.payload;
    },
    // 배경 제거하기
    RemoveBgImg: (state) => {
      state.bgImg = false;
    },
    // 프레임 오브젝트
    Decorate: (state, action) => {
      state.objects = action.payload;
    },
    Undecorate: (state) => {
      state.deleteSignal = 1;
    },
    // 텍스트
    AddText: (state, action) => {
      state.text = action.payload;
    },
    // 드로잉
    SwitchDrawingMode: (state) => {
      state.drawingMode = !state.drawingMode;
    },
    AddDrawing: (state, action) => {
      state.brush = action.payload;
    },
    // 로컬 다운로드
    DoDownload: (state) => {
      state.downloadSignal = 1;
    },
    // 시그널 리셋
    ResetSignal: (state, action) => {
      state[action.payload] = 0;
      /* download 및 delete signal을
      0으로 리셋해야 하는 이유는
      캔버스 리렌더시 
      의도하지 않은 동작이 실행 되는 사이드 이펙트를
      방지하기 위해서입니다 */
    },
    // 서버 POST
    PostSignal: (state, action) => {
      state.frameInfo = action.payload;
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
  ReBlock,
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

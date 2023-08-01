// modal 관련  redux tookit 모듈 파일
import { createSlice } from "@reduxjs/toolkit";

// 초기 상태 정의
const initialState = {
  height: 589.6,
  width: 207.8,
  bgColor: "",
  bgImg: "",
};

// 액션 생성 함수
const frameSlice = createSlice({
  // 액션 타입 정의
  name: "frame",
  // 초기 상태
  initialState,
  // 리듀서 맵
  reducers: {
    // 리듀서 함수
    // 아래 코드는 리듀서와 액션 생성자 함수가 분리되지 않은 형태로 작성함
    Resize: (state, action) => {
      state.width = action.payload.width;
      state.height = action.payload.height;
    },
    Repaint: (state, action) => {
      state.bgColor = action.payload.color;
      state.bgImg = action.payload.image;
    },
  },
});

export const { Resize, Repaint } = frameSlice.actions;

export default frameSlice.reducer;

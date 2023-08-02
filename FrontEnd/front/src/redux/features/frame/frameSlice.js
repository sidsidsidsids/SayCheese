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
    // 프레임 규격
    Resize: (state, action) => {
      state.width = action.payload.width;
      state.height = action.payload.height;
    },
    // 프레임 배경 색과 배경 이미지
    Repaint: (state, action) => {
      if (
        state.bgImg !== action.payload.image ||
        state.bgColor !== action.payload.color
      ) {
        state.bgColor = action.payload.color;
        state.bgImg = action.payload.image;
      }
    },

    RemoveBgImg: (state) => {
      state.bgImg = "";
    },
  },
});

export const { Resize, Repaint, RemoveBgImg } = frameSlice.actions;

export default frameSlice.reducer;

// modal 관련  redux tookit 모듈 파일
import { createSlice } from "@reduxjs/toolkit";

// 초기 상태 정의
const initialState = {
  height: 589.6,
  width: 207.8,
  bgColor: "#000000",
  bgImg: false,
  objects: [],
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
        action.payload.image !== undefined &&
        state.bgImg !== action.payload.image
      ) {
        state.bgImg = action.payload.image;
      }
      if (state.bgColor !== action.payload.Color) {
        state.bgColor = action.payload.color;
      }
    },

    RemoveBgImg: (state) => {
      state.bgImg = false;
    },
    // 프레임 오브젝트
    Decorate: (state, action) => {
      state.objects.push(action.payload);
      console.log("(*ᴗ͈ˬᴗ͈)ꕤ*.ﾟ");
      console.log(state.objects.length);
    },
    Undecorate: (state) => {
      console.log("언데코");
    },
  },
});

export const { Resize, Repaint, RemoveBgImg, Decorate, Undecorate } =
  frameSlice.actions;

export default frameSlice.reducer;

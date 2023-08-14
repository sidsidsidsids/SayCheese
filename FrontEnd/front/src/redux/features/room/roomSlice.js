// modal 관련  redux tookit 모듈 파일
import { createSlice } from "@reduxjs/toolkit";

// 초기 상태 정의
const initialState = {
    roomInfo: {},
  };

// 액션 생성 함수
const roomSlice = createSlice({
    // 액션 타입 정의
    name: "room",
    // 초기 상태
    initialState,
    // 리듀서 맵
    reducers: {
      // 리듀서 함수
      // 아래 코드는 리듀서와 액션 생성자 함수가 분리되지 않은 형태로 작성함
      setRoom: (state, action) => {
        state.roomInfo = action.payload; // 모달에 표시할 콘텐츠를 액션 페이로드로 전달 받음
      },
      closeRoom: (state, action) => {
        state.roomInfo = {};
      },
    },
  });
  
  export const { setRoom, closeRoom } = roomSlice.actions;
  
  export default roomSlice.reducer;
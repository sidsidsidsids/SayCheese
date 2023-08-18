// modal 관련  redux tookit 모듈 파일
import { createSlice } from "@reduxjs/toolkit";

// 초기 상태 정의
const initialState = {
  isOpen: false, // 모달의 초기 상태는 닫혀있음
  modalContent: null, // 모달에 뜰 콘텐츠
};

// 액션 생성 함수
const modalSlice = createSlice({
  // 액션 타입 정의
  name: "modal",
  // 초기 상태
  initialState,
  // 리듀서 맵
  reducers: {
    // 리듀서 함수
    // 아래 코드는 리듀서와 액션 생성자 함수가 분리되지 않은 형태로 작성함
    openModal: (state, action) => {
      state.modalContent = action.payload; // 모달에 표시할 콘텐츠를 액션 페이로드로 전달 받음
      state.isOpen = true;
    },
    closeModal: (state, action) => {
      state.modalContent = null;
      state.isOpen = false;
    },
  },
});

export const { openModal, closeModal } = modalSlice.actions;

export default modalSlice.reducer;

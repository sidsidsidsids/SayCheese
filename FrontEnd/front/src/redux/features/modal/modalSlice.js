// modal 관련 모듈
import { createSlice } from "@reduxjs/toolkit";

// 초기 상태 정의
const initialState = {
  isOpen: false, // 모달의 초기 상태는 닫혀있음
  modalContent: "", // 모달에 뜰 콘텐츠
};

// 액션 생성 함수
const modalSlice = createSlice({
  // 액션 타입 정의
  name: "modal",
  // 초기 상태
  // initialState: [{ id: "", title: "", imgSrc: "", likes:"", openModal: false }],
  initialState,
  // 리듀서 함수
  reducers: {
    openModal: (state, action) => {
      state.modalContent = action.payload; // 모달에 표시할 콘텐츠를 액션 페이로드로 전달 받음
      state.isOpen = true;
      console.log(state.modalContent);
    },
  },
  closeModal: (state, action) => {
    state.modalContent = "";
    state.isOpen = false;
  },
});

export const { openModal, closeModal } = modalSlice.actions;

export default modalSlice.reducer;

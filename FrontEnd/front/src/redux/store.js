// store.js
import { configureStore } from "@reduxjs/toolkit";
import modalReducer from "./features/modal/modalSlice";
import frameReducer from "./features/frame/frameSlice";

const store = configureStore({
  reducer: {
    modal: modalReducer,
    frame: frameReducer,
  },
});

export default store;

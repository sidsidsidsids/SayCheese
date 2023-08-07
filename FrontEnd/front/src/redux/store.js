// store.js
import { configureStore } from "@reduxjs/toolkit";
import modalReducer from "./features/modal/modalSlice";
import frameReducer from "./features/frame/frameSlice";
import loginReducer from "./features/login/loginSlice";

const store = configureStore({
  reducer: {
    modal: modalReducer,
    frame: frameReducer,
    login: loginReducer,
  },
});

export default store;

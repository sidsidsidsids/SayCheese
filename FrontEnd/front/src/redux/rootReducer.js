// rootReducer.js
import { combineReducers } from "redux";
import modalReducer from "./features/modal/modalSlice";
import frameReducer from "./features/frame/frameSlice";
import loginReducer from "./features/login/loginSlice";
import roomReducer from "./features/room/roomSlice"

const rootReducer = combineReducers({
  modal: modalReducer,
  frame: frameReducer,
  // 추가적인 리듀서가 있다면 여기에 추가
  login: loginReducer,
  room: roomReducer,
});

export default rootReducer;

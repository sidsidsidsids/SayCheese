// rootReducer.js
import { combineReducers } from "redux";
import modalReducer from "./features/modal/modalSlice";
const rootReducer = combineReducers({
  modal: modalReducer,
  // 추가적인 리듀서가 있다면 여기에 추가
});

export default rootReducer;

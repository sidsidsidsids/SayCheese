import { combineReducers } from "@reduxjs/toolkit";
import roomStatusReducer from "./roomStatus";

const rootReducer = combineReducers({
  status: roomStatusReducer,
});

export default rootReducer;

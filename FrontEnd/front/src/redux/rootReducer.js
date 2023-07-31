import { combineReducers } from "@reduxjs/toolkit";
import roomStatesReducer from "./roomStates";
import ovReducer from "./ovStates";

const rootReducer = combineReducers({
  roomState: roomStatesReducer,
  ovState: ovReducer,
});

export default rootReducer;

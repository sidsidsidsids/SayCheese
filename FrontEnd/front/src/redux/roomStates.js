import { createSlice } from "@reduxjs/toolkit";
export const statusSlice = createSlice({
  name: "roomState",
  initialState: {
    startAction: false,
    afterAction: false,
  },
  reducers: {
    start: (state) => {
      state.startAction = true;
    },
    r_start: (state) => {
      state.startAction = false;
    },
    finish: (state) => {
      state.afterAction = true;
    },
    r_finish: (state) => {
      state.afterAction = false;
    },
    end_room: (state) => {
      state.afterAction = false;
      state.startAction = false;
    },
  },
});

export const statusAction = statusSlice.actions;
export default statusSlice.reducer;

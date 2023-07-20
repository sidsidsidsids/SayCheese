import { createSlice } from "@reduxjs/toolkit";
export const statusSlice = createSlice({
  name: "status",
  initialState: {
    startAction: false,
    afterAction: false,
  },
  reducers: {
    start: (state) => {
      state.startAction = true;
    },
    tmp_start: (state) => {
      state.startAction = false;
    },
    finish: (state) => {
      state.afterAction = true;
    },
    tmp_finish: (state) => {
      state.afterAction = false;
    },
  },
});

export const { start, finish } = statusSlice.actions;
export default statusSlice.reducer;

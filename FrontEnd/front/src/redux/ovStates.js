import { createSlice } from "@reduxjs/toolkit";
import { OpenVidu } from "openvidu-browser";

export const ovSlice = createSlice({
  name: "ovState",
  initialState: {
    OV: null,
    mySessionId: undefined,
    myUserName: undefined,
    session: undefined,
    mainStreamManager: undefined,
    publisher: undefined,
    subscribers: [],
    devices: undefined,
    currentVideoDevice: undefined,
  },
  reducers: {
    createOpenvidu: (state, { payload }) => {
      if (!state.OV) {
        state.myUserName = payload.nickname;
        state.mySessionId = payload.roomId;
        state.OV = new OpenVidu();
        state.session = state.OV.initSession();
        state.devices = state.OV.getDevices();
      }
    },

    createPublisher: (state, { payload }) => {
      state.session.publish(payload.publisher);
      state.currentVideoDevice = payload.currentVideoDevice;
      state.mainStreamManager = payload.publisher;
      state.publisher = payload.publisher;
    },

    enteredSubscriber: (state, action) => {
      const subscriber = state.session.subscribe(action.payload, undefined);
      state.subscribers.push(subscriber);
    },

    deleteSubscriber: (state, action) => {
      let index = state.subscribers.indexOf(action.payload, 0);
      if (index > -1) {
        state.subscribers.splice(index, 1);
      }
    },

    leaveSession(state, { payload }) {
      const mySession = state.session;
      if (mySession) {
        mySession.disconnect();
      }

      state.OV = null;
      state.session = undefined;
      state.subscribers = [];
      state.mySessionId = undefined;
      state.myUserName = undefined;
      state.mainStreamManager = undefined;
      state.publisher = undefined;
      state.devices = undefined;
      state.currentVideoDevice = undefined;

      // return payload
    },
  },
});

export const ovAction = ovSlice.actions;
export default ovSlice.reducer;

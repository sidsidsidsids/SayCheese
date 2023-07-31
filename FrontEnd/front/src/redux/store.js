import { configureStore } from "@reduxjs/toolkit";
import rootReducer from "./rootReducer";
import { getDefaultMiddleware } from "@reduxjs/toolkit";

const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware({
    serializableCheck: false,
  }),
});

export default store;

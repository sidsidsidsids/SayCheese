// store.js
import { configureStore } from "@reduxjs/toolkit";
import modalReducer from "./features/modal/modalSlice";
import frameReducer from "./features/frame/frameSlice";
import loginReducer from "./features/login/loginSlice";
import { persistStore, persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage"; // 로컬스토리지 사용
import rootReducer from "./rootReducer";

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["login"],
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);

export default store;

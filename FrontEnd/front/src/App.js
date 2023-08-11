import { Outlet } from "react-router-dom";
import "./App.css";
import Header from "./header/Header";
import { useContext, useEffect, useState } from "react";
import { AuthContext, AuthProvider } from "./contexts/AuthContext";

import { useDispatch, useSelector } from "react-redux";
import { getUserInfo } from "./redux/features/login/loginSlice";

import { persistStore } from "redux-persist";
import store from "./redux/store";

function App() {
  const dispatch = useDispatch();

  // useEffect(() => {
  //   const accessToken = localStorage.getItem("accessToken");
  //   if (accessToken) {
  //     dispatch(getUserInfo());
  //   }
  // }, [dispatch]);

  return (
    <div className="App">
      <AuthProvider>
        <Header />
        <div className="contents">
          <Outlet />
        </div>
      </AuthProvider>
      <p>app.js</p>
    </div>
  );
}

export default App;

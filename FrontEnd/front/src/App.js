import { useContext, useEffect, useState } from "react";
// third party
import { Outlet } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { AuthContext, AuthProvider } from "./contexts/AuthContext";
import { QueryClientProvider, QueryClient } from "@tanstack/react-query";

import Header from "./header/Header";
import { getUserInfo } from "./redux/features/login/loginSlice";
import "./App.css";
function App() {
  const { userInfo } = useSelector((store) => store.login);
  const dispatch = useDispatch();

  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      dispatch(getUserInfo());
    }
  }, [dispatch]);

  // Create a client
  const queryClient = new QueryClient();

  return (
    <div className="App">
      <AuthProvider>
        <Header />
        <div className="contents">
          <QueryClientProvider client={queryClient}>
            <Outlet />
          </QueryClientProvider>
        </div>
      </AuthProvider>

      <p>app.js</p>
    </div>
  );
}

export default App;

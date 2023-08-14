import { useContext, useEffect, useState } from "react";
// third party
import { Outlet } from "react-router-dom";
import { useDispatch } from "react-redux";
import { AuthProvider } from "./contexts/AuthContext";
import { QueryClientProvider, QueryClient } from "@tanstack/react-query";

import Header from "./header/Header";
import "./App.css";

function App() {
  const dispatch = useDispatch();

  // useEffect(() => {
  //   const accessToken = localStorage.getItem("accessToken");
  //   if (accessToken) {
  //     dispatch(getUserInfo());
  //   }
  // }, [dispatch]);

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

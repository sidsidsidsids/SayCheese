<<<<<<< HEAD
import { useContext, useEffect, useState } from "react";
=======
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
// third party
import { Outlet } from "react-router-dom";
import { useDispatch } from "react-redux";
import { AuthProvider } from "./contexts/AuthContext";
import { QueryClientProvider, QueryClient } from "@tanstack/react-query";

import Header from "./header/Header";
import "./App.css";

import { getUserInfo } from "./redux/features/login/loginSlice";
import Loading from "./Loading";

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
<<<<<<< HEAD
      <p>app.js</p>
=======
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
    </div>
  );
}

export default App;

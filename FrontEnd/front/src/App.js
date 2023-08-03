import { Outlet } from "react-router-dom";
import "./App.css";
import Header from "./header/Header";
import { useState } from "react";
import { AuthProvider } from "./contexts/AuthContext";

function App() {
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

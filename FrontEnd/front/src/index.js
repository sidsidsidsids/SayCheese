import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import NoticeList from "./customercenter/NoticeList";
import Customercenter from "./customercenter/CustomerCenter";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <div className="BackColor">
      <Customercenter />
    </div>
  </React.StrictMode>
);

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import NoticeList from "./customercenter/NoticeList";
import Customercenter from "./customercenter/CustomerCenter";

import User from "./user/User";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <div>
      <User />
    </div>
  </React.StrictMode>
);

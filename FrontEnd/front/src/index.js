import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import "./index.css";
import App from "./App";
import Main from "./Main";
import Frame from "./frame/pages/Frame";
import ErrorPage from "./error-page";

const router = createBrowserRouter([
  {
    path: "main",
    element: <Main />,
  },
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "frame/",
        element: <Frame />,
      },
      // 이제 이미지 게시판, 공지 게시판, 로그인 페이지, 마이페이지를 연결하면 됩니다.
    ],
  },
  // ERROR 페이지 라우터
  {
    path: "/*",
    element: <ErrorPage />,
    errorElement: <ErrorPage />,
  },
]);

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

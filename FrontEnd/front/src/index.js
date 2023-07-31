import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

// 라우터
import "./index.css";
import App from "./App";
import Main from "./Main";
import Frame from "./frame/pages/Frame";
import FrameCreate from "./frame/pages/FrameCreate";
import ErrorPage from "./error-page";

// Redux
import { Provider } from "react-redux"; // React 앱에 Redux 스토어를 연결하기 위한 Provider
import store from "./redux/store";

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
        children: [{ path: "create/", element: <Frame/> }],
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
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </React.StrictMode>
);

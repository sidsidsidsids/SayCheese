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

import CustomerCenter from "./customercenter/CustomerCenter";
import NoticeList from "./customercenter/NoticeList";
import Notice from "./customercenter/Notice";
import NoticeWrite from "./customercenter/NoticeWrite";
import Faq from "./customercenter/Faq";

import User from "./user/User";
import Login from "./user/Login";
import SignUp from "./user/SignUp";
import MyPage from "./user/MyPage";

import Photo from "./photo/Photo";

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
        children: [{ path: "create/", element: <Frame /> }],
      },
      // 이제 이미지 게시판, 공지 게시판, 로그인 페이지, 마이페이지를 연결하면 됩니다.
      {
        path: "customercenter/",
        element: <CustomerCenter />,
        children: [
          {
            path: "notice/",
            element: <NoticeList />,
          },
          {
            path: "notice/:id",
            element: <Notice />,
          },
          {
            path: "notice/write/",
            element: <NoticeWrite />,
          },
          {
            path: "faq/",
            element: <Faq />,
          },
        ],
      },
      {
        path: "user/",
        element: <User />,
        children: [
          {
            path: "login/",
            element: <Login />,
          },
          {
            path: "signup/",
            element: <SignUp />,
          },
          {
            path: "mypage/",
            element: <MyPage />,
          },
        ],
      },
      {
        path: "photo/",
        element: <Photo />,
      },
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

import React, { useState } from "react";
// third party
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { PersistGate } from "redux-persist/integration/react";
import { persistStore } from "redux-persist";

// 라우터
import "./index.css";
import App from "./App";
import Main from "./main/Main";
import Frame from "./frame/pages/Frame";
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
import Room from "./room/Room";
import MyInfoModify from "./user/MyInfoModify";
import MyPagePhoto from "./user/MyPagePhoto";
import MyPageFrame from "./user/MyPageFrame";

// Redux
import { Provider } from "react-redux"; // React 앱에 Redux 스토어를 연결하기 위한 Provider
import store from "./redux/store";
import axios from "axios";
// local
import Loading from "./Loading";

const router = createBrowserRouter([
  {
    path: "main",
    element: <Main />,
  },
  {
    path: "",
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
        children: [{ path: "create", element: <Frame /> }],
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
            path: "mypage/:email",
            element: <MyPage />,
          },
          {
            path: "mypage/myphoto/:email",
            element: <MyPagePhoto />,
          },
          {
            path: "mypage/myframe/:email",
            element: <MyPageFrame />,
          },
          {
            path: "modify/:email",
            element: <MyInfoModify />,
          },
        ],
      },
      {
        path: "photo/",
        element: <Photo />,
      },
    ],
  },
  { path: "/room/:id", element: <Room /> },
  // ERROR 페이지 라우터
  {
    path: "/*",
    element: <ErrorPage />,
    errorElement: <ErrorPage />,
  },
]);

const accessToken = localStorage.getItem("accessToken");
if (accessToken) {
  axios.defaults.headers.common["Authorization"] = accessToken;
}

export let persistor = persistStore(store);

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <Provider store={store}>
    {/* <PersistGate loading={null} persistor={persistor}> */}
    <RouterProvider router={router} />
    {/* </PersistGate> */}
  </Provider>
);

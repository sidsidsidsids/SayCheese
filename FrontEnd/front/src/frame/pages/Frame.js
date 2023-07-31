// 프레임 메인 페이지입니다.
import React, { useState } from "react";
import "../css/Frame.css";
// react router
import { Link, useLocation } from "react-router-dom";

// for 프레임 검색
import FrameSearch from "../components/FrameSearch";
// for 프레임 구경하기
import FrameList from "../components/FrameList";
// for 프레임 만들기
import FrameCreate from "./FrameCreate";

export default function Frame() {
  // tabItems 배열에 각각의 탭 항목을 정의합니다.
  const tabItems = [
    { tabItem: "프레임 구경하기", component: <FrameList/>, router: "/frame/" },
    {
      tabItem: "프레임 만들기",
      component: <FrameCreate/>,
      router: "/frame/create/",
    },
  ];
  // 활성화된 탭을 위한 data 입니다
  const location = useLocation().pathname;
  const focusedItem = tabItems.find((item) => item.router === location);

  return (
    <div className="frame">
      <div className="responsiveFrameTab">
        <FrameSearch />
      </div>
      <div className="frameTap">
        {tabItems.map((item, index) => (
          <button
            // 현재 선택된 탭인 경우에는 focused 클래스를 추가하여 배경색을 하얀색으로 변경합니다.
            className={`FrameButton ${
              focusedItem === item ? "focused" : ""
            }`}
          >
            <Link
              key={index}
              to={item.tabItem === "프레임 구경하기" ? "/frame/" : "/frame/create/"}
            >
              {item.tabItem}
            </Link>
          </button>
        ))}

        <div
          // 검색창은 프레임 만들기 일때는 숨기고 구경하기 탭이 활성화된 때 보입니다.
          className={`frameSearchTab ${
            focusedItem === tabItems[1].tabItem ? "displayHide" : ""
          }`}
        >
          <FrameSearch />
        </div>
      </div>
      {/* 활성화된 탭에 따라서 컨텐츠가 변경됩니다 */}
      <div className="frameSpace">
        {focusedItem ? focusedItem.component : null}
      </div>
    </div>
  );
}

// 프레임 메인 페이지입니다.
import React, { useState } from "react";
import "../css/Frame.css";

// for 프레임 검색
import FrameSearch from "../components/FrameSearch";

// for 프레임 구경하기
import FrameList from "../components/FrameList";

// for 프레임 만들기
import FrameCreate from "./FrameCreate";

export default function Frame() {
  // 탭 매뉴
  const TabBarList = [
    { tabItem: "프레임 구경하기", content: "FrameList" },
    { tabItem: "프레임 만들기", content: "FrameCreate" },
  ];
  // 활성화된 탭을 위한 data 입니다
  const [focusedItem, setFocusedItem] = useState(TabBarList[0].tabItem);

  return (
    <div className="frame">
      <div className="responsiveFrameTab">
        <FrameSearch />
      </div>
      <p>Frame 페이지입니다.</p>
      <div className="frameTap">
        {TabBarList.map((item, index) => (
          <button
            onClick={() => {
              setFocusedItem(item.tabItem);
            }}
            className={`FrameButton ${
              focusedItem === item.tabItem ? "focused" : ""
            }`}
          >
            {item.tabItem}
          </button>
        ))}

        <div
          className={`frameSearchTab ${
            focusedItem === TabBarList[1].tabItem ? "displayHide" : ""
          }`}
        >
          <FrameSearch />
        </div>
      </div>
      <div className="frameSpace">
        {focusedItem === TabBarList[0].tabItem && <FrameList />}
        {focusedItem === TabBarList[1].tabItem && <FrameCreate />}
      </div>
    </div>
  );
}

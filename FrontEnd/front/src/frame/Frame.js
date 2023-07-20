// 프레임 메인 페이지입니다.
import React, { useState } from "react";
import "./Frame.css";

import FrameSearchBar from "./FrameSearchBar";
import FrameList from "./FrameList";

export default function Frame() {
  const TabBarList = [
    { tabItem: "프레임 구경하기" },
    { tabItem: "프레임 만들기" },
  ];

  const [focusedItem, setFocusedItem] = useState(TabBarList[0].tabItem);

  return (
    <div className="frame">
      <p>Frame 페이지입니다.</p>
      <div className="frameHeader">
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
        <FrameSearchBar />
      </div>
      <FrameList />
    </div>
  );
}

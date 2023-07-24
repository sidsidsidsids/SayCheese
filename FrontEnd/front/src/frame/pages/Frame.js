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
  // TabBarList 배열에 각각의 탭 항목을 정의합니다.
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
      <div className="frameTap">
        {TabBarList.map((item, index) => (
          <button
            // 클릭하면 탭이 활성화 된 탭으로 저장됩니다
            onClick={() => {
              setFocusedItem(item.tabItem);
            }}
            // 현재 선택된 탭인 경우에는 focused 클래스를 추가하여 배경색을 하얀색으로 변경합니다.
            className={`FrameButton ${
              focusedItem === item.tabItem ? "focused" : ""
            }`}
          >
            {item.tabItem}
          </button>
        ))}

        <div
          // 검색창은 프레임 구경하기 탭이 활성화된 탭일 때 보이고 아닐 때는 숨깁니다.
          className={`frameSearchTab ${
            focusedItem === TabBarList[1].tabItem ? "displayHide" : ""
          }`}
        >
          <FrameSearch />
        </div>
      </div>
      {/* 활성화된 탭에 따라서 컨텐츠가 변경됩니다 */}
      <div className="frameSpace">
        {focusedItem === TabBarList[0].tabItem && <FrameList />}
        {focusedItem === TabBarList[1].tabItem && <FrameCreate />}
      </div>
    </div>
  );
}

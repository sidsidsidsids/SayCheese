// CreateToolBar 컴포넌트 : canvas 조작을 위해 사용될 툴바를 런데링하는 컴포넌트입니다.
import React, { useState } from "react";
import "../css/FrameCreateToolBar.css";
//components
import Standard from "./CreateToolBarStandard";
// import canvasArea from "./FrameCreateCanvas";

export default function CreateToolBar() {
  // ToolBarList 배열에 각각의 도구 항목을 정의합니다.
  const ToolBarList = [
    { createItem: "규격", notice: "사진 규격을 선택하세요" },
    { createItem: "배경", notice: "배경을 바꿔 보세요" },
    { createItem: "꾸미기", notice: "배경을 꾸며 보세요" },
    { createItem: "글자", notice: "글을 추가해보세요" },
    { createItem: "그리기", notice: "그림을 그려보세요" },
    { createItem: "저장", notice: "마음에 들면 저장하세요" },
  ];
  // focusedTool state를 초기화하고, 기본값으로 첫 번째 도구를 선택합니다.
  const [focusedTool, setFocusedTool] = useState(ToolBarList[0].createItem);
  // focusedTool notice
  const searchIndex = ToolBarList.findIndex(
    (item) => item.createItem === focusedTool
  );
  const notice = ToolBarList[searchIndex].notice;

  return (
    <>
      <div className="ToolBar">
        <ul className="toolBarTap">
          {ToolBarList.map((item, index) => (
            <li
              // 클릭 시 해당 도구를 선택된 도구로 설정합니다.
              onClick={(e) => {
                setFocusedTool(item.createItem);
              }}
              // 현재 선택된 도구인 경우에는 focusedTool 클래스를 추가하여 배경색을 회색으로 변경합니다.
              className={`${
                focusedTool === item.createItem ? "focusedTool" : ""
              }`}
            >
              {item.createItem}
            </li>
          ))}
        </ul>
        {/* 선택된 도구에 따른 상세 도구 */}
        <div className="detailedToolBar">
          <span className="toolNotice">{notice}</span>
          {focusedTool === ToolBarList[0].createItem && <Standard />}
        </div>
      </div>
    </>
  );
}

// CreateToolBar 컴포넌트 : canvas 조작을 위해 사용될 툴바를 런데링하는 컴포넌트입니다.
import React, { useState } from "react";
import "../css/FrameCreateToolBar.css";
//components
import Standard from "./CreateToolBarStandard";
import BgColor from "./CreateToolBarBackground";
import Objects from "./CreateToolBarObjects";
import Text from "./CreateToolBarText";
import Drawing from "./CreateToolBarDrawing";
import Saving from "./CreateToolBarSaving";

export default function CreateToolBar() {
  // toolBarItems 배열에 각각의 도구 항목을 정의합니다.
  const toolBarItems = [
    {
      toolBarItem: "규격",
      notice: "사진 규격을 선택하세요",
    },
    {
      toolBarItem: "배경",
      notice: "배경을 바꿔 보세요",
      component: <Standard />,
    },
    {
      toolBarItem: "꾸미기",
      notice: "배경을 꾸며 보세요",
      component: <BgColor />,
    },
    {
      toolBarItem: "글자",
      notice: "글을 추가해보세요",
      component: <Objects />,
    },
    { toolBarItem: "그리기", notice: "자유롭게 사용하세요" },
    { toolBarItem: "저장", notice: "마음에 들면 저장하세요" },
  ];
  // focusedTool state를 초기화하고, 기본값으로 첫 번째 도구를 선택합니다.
  const [focusedTool, setFocusedTool] = useState(toolBarItems[0].toolBarItem);
  // focusedTool notice
  const searchIndex = toolBarItems.findIndex(
    (item) => item.toolBarItem === focusedTool
  );
  const notice = toolBarItems[searchIndex].notice;

  return (
    <div className="ToolBar">
      <ul className="toolBarTap">
        {toolBarItems.map((item, index) => (
          <li
            // 클릭 시 해당 도구를 선택된 도구로 설정합니다.
            key={index}
            onClick={(event) => {
              event.stopPropagation();
              setFocusedTool(item.toolBarItem);
            }}
            // 현재 선택된 도구인 경우에는 focusedTool 클래스를 추가하여 배경색을 회색으로 변경합니다.
            className={`${
              focusedTool === item.toolBarItem ? "focusedTool" : ""
            }`}
          >
            {item.toolBarItem}
          </li>
        ))}
      </ul>
      {/* 선택된 도구에 따른 상세 도구 */}
      <div className="detailedToolBar">
        <div className="toolNotice">{notice}</div>
        {focusedTool === toolBarItems[0].toolBarItem && <Standard />}
        {focusedTool === toolBarItems[1].toolBarItem && <BgColor />}
        {focusedTool === toolBarItems[2].toolBarItem && <Objects />}
        {focusedTool === toolBarItems[3].toolBarItem && <Text />}
        {focusedTool === toolBarItems[4].toolBarItem && <Drawing />}
        {focusedTool === toolBarItems[5].toolBarItem && <Saving />}
      </div>
    </div>
  );
}

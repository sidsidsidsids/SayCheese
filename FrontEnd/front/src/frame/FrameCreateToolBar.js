// canvas 조작을 위해 사용될 툴바 입니다.
import React, { useState } from "react";
import "./FrameCreateToolBar.css";

export default function CreateToolBar() {
  const ToolBarList = [
    { createItem: "규격" },
    { createItem: "배경" },
    { createItem: "꾸미기" },
    { createItem: "드로잉" },
    { createItem: "저장" },
  ];
  const [focusedTool, setFocusedTool] = useState(ToolBarList[0].createItem);

  return (
    <>
      <div className="ToolBar">
        <ul className="toolBarTap">
          {ToolBarList.map((item, index) => (
            <li
              onClick={(e) => {
                setFocusedTool(item.createItem);
              }}
              className={`${
                focusedTool === item.createItem ? "focusedTool" : ""
              }`}
            >
              {item.createItem}
            </li>
          ))}
        </ul>
        <div className="detailedToolBar">🛠️ 디테일</div>
      </div>
    </>
  );
}

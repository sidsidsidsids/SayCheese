import React from "react";
import { useDispatch } from "react-redux";
import { Resize } from "../../redux/features/frame/frameSlice";

export default function Standard() {
  const dispatch = useDispatch();

  const handleVerticalResize = () => {
    // 세로로 589.6px, 가로로 207.8px 크기로 변경
    const payload = { width: 207.8, height: 589.6 };
    dispatch(Resize(payload));
    console.log("세로");
  };

  const handleHorizontalResize = () => {
    // 가로로 589.6px, 세로로 400.6px 크기로 변경
    const payload = { width: 589.6, height: 400.6 };
    dispatch(Resize(payload));
    console.log("가로");
  };

  return (
    <div>
      <button onClick={handleVerticalResize}>Vertical Resize</button>
      <button onClick={handleHorizontalResize}>Horizontal Resize</button>
      {/* 다른 툴바 버튼들 */}
    </div>
  );
}

// 프레임 생성할 때 프레임의 규격을 정하는 툴바의 디테일 컴포넌트입니다.
import React from "react";
import { useDispatch } from "react-redux";
import { Resize } from "../../redux/features/frame/frameSlice";
import verticalFrame from "../assets/사다리형.png";
import horizontalFrame from "../assets/창문형.png";

export default function Standard() {
  const dispatch = useDispatch();

  const handleVerticalResize = () => {
    // 세로로 589.6px, 가로로 207.8px 크기로 변경
    const payload = { width: 207.8, height: 589.6 };
    dispatch(Resize(payload));
  };

  const handleHorizontalResize = () => {
    // 가로로 589.6px, 세로로 400.6px 크기로 변경
    const payload = { width: 589.6, height: 400.6 };
    dispatch(Resize(payload));
  };

  return (
    <div className="standardWrapper">
      <img
        width="40px"
        onClick={handleVerticalResize}
        className="standardImg"
        src={verticalFrame}
        alt="사다리형 프레임"
      />

      <img
        width="90px"
        onClick={handleHorizontalResize}
        className="standardImg"
        src={horizontalFrame}
        alt="창문형 프레임"
      />

      {/* 다른 툴바 버튼들 */}
    </div>
  );
}

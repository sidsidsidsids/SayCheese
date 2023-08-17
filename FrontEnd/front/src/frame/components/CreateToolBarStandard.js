// 프레임 생성할 때 프레임의 규격을 정하는 툴바의 디테일 컴포넌트입니다.
import React from "react";
import { BsFillSuitHeartFill } from "react-icons/bs/";
import { useDispatch } from "react-redux";
import { Resize, ReBlock } from "../../redux/features/frame/frameSlice";
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
      <label className="labelForBlockChoice">
        사진 모양을 선택할 수 있어요
      </label>
      <div className="blockChoice">
        {/* 도형을 클릭하면 리덕스 스테이트 어떤 도형인지 입력되고 캔버스는 그 도형에 맞는 투명칸을 만듭니다 */}
        <div className="rect" onClick={() => dispatch(ReBlock("Plain"))}></div>
        <div
          className="smoothRect"
          onClick={(event) => {
            event.stopPropagation();
            dispatch(ReBlock("SmoothPlain"));
          }}
        ></div>
        <div
          className="circle"
          onClick={(event) => {
            event.stopPropagation();
            dispatch(ReBlock("Circle"));
          }}
        ></div>
        <BsFillSuitHeartFill
          className="heart"
          onClick={(event) => {
            event.stopPropagation();
            dispatch(ReBlock("Heart"));
          }}
        />
      </div>
      {/* 다른 툴바 버튼들 */}
    </div>
  );
}

import React, { useState, useEffect, useCallback } from "react";
// third party
import { Tooltip } from "react-tooltip";
import { useSelector, useDispatch } from "react-redux";

import {
  Undecorate,
  AddDrawing,
  SwitchDrawingMode,
} from "../../redux/features/frame/frameSlice";

export default function Drawing() {
  const [brushValue, setBrushValue] = useState(3);
  const [brushShadowValue, setBrushShadowValue] = useState(5);
  const [brushColor, setBrushColor] = useState("#000000 ");
  const [brushShadowColor, setBrushShadowColor] = useState("#000000 ");
  const [brushShadowOffset, setBrushShadowOffset] = useState(1);
  const [message, setMessage] = useState("펜 활성화");
  const { drawingMode } = useSelector((store) => store.frame);
  const dispatch = useDispatch();
  // range를 위한 min, max 값
  const min = 0;
  const max = 50;
  // range-value 값과 위치 계산
  const newValue = Number(((brushValue - min) * 100) / (max - min));
  const newPosition = 10 - newValue * 0.2;

  const newValueShadow = Number(((brushShadowValue - min) * 100) / (max - min));
  const newPositionShadow = 10 - newValueShadow * 0.2;

  const newValueOffset = Number(((brushShadowOffset - min) * 100) / (20 - min));
  const newPositionOffset = 10 - newValueOffset * 0.2;

  useEffect(
    function Message() {
      if (drawingMode) {
        // 드로잉 모드가 true이면
        setMessage("펜 비활성화"); // 펜 비활성화 버튼을 표시합니다
      } else {
        setMessage("펜 활성화");
      }
    },
    [drawingMode]
  );

  const updatePayload = useCallback(() => {
    const payload = {
      brushValue,
      brushColor,
      brushShadowValue,
      brushShadowColor,
      brushShadowOffset,
    };

    if (drawingMode) {
      dispatch(AddDrawing(payload));
    }
  }, [
    brushValue,
    brushColor,
    brushShadowValue,
    brushShadowColor,
    drawingMode,
    brushShadowOffset,
    dispatch,
  ]);

  useEffect(() => {
    updatePayload();
  }, [updatePayload]);

  return (
    <>
      <label htmlFor="brushColor">펜 색과 두께를 골라보세요</label>
      <input
        type="color"
        id="brushColor"
        value={brushColor}
        onChange={(e) => setBrushColor(e.target.value)}
      ></input>
      <div className="range-wrap">
        <div
          className="range-value"
          style={{ left: `calc(${newValue}% + (${newPosition}px))` }}
        >
          <span>{brushValue}</span>
        </div>
        <input
          id="brushRange"
          type="range"
          min={min}
          max={max}
          value={brushValue}
          step="1"
          onChange={(event) => setBrushValue(event.target.value)}
        />
      </div>

      <label htmlFor="brushShadowColor">펜 그림자 색을 골라보세요</label>
      <input
        type="color"
        id="brushShadowColor"
        value={brushShadowColor}
        onChange={(e) => {
          setBrushShadowColor(e.target.value);
        }}
      ></input>
      <label htmlFor="shadowRange">펜 그림자 두께를 정해보세요</label>
      <div className="range-wrap">
        <div
          className="range-value"
          style={{
            left: `calc(${newValueShadow}% + (${newPositionShadow}px))`,
          }}
        >
          <span>{brushShadowValue}</span>
        </div>
        <input
          id="shadowRange"
          type="range"
          min={min}
          max={max}
          step="1"
          value={brushShadowValue}
          onChange={(event) => {
            setBrushShadowValue(event.target.value);
          }}
        />
      </div>
      <label htmlFor="offsetRange">펜과 그림자의 거리를 정해보세요</label>
      <div className="range-wrap">
        <div
          className="range-value"
          style={{
            left: `calc(${newValueOffset}% + (${newPositionOffset}px))`,
          }}
        >
          <span>{brushShadowOffset}</span>
        </div>
        <input
          id="offsetRange"
          type="range"
          min="0"
          max="20"
          step="1"
          value={brushShadowOffset}
          onChange={(event) => {
            setBrushShadowOffset(event.target.value);
          }}
        />
      </div>
      <div className="alignTwoButtons">
        <button
          style={{ width: "129px" }}
          type="button"
          className="whtbtn"
          onClick={(event) => {
            event.stopPropagation();
            dispatch(SwitchDrawingMode());
          }}
        >
          {message}
        </button>
        <button
          id="deleteDrawings"
          type="button"
          value="삭제하기"
          className="btn aligncenter"
          onClick={(event) => {
            event.stopPropagation();
            dispatch(Undecorate());
          }}
          data-tooltip-place="top"
          data-tooltip-id="object-menual"
          data-tooltip-content="드로잉을 삭제하기 위해서는 먼저 펜을 비활성화 해주세요. 그리고
          삭제하고 싶은 글자를 클릭하고 삭제버튼을 누르세요"
        >
          삭제하기
        </button>
        <Tooltip id="object-menual" place="top" />
      </div>
    </>
  );
}

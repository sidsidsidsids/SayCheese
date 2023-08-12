// 꾸미기 컴포넌트입니다
import React, { useState, useRef } from "react";
// redux
import { Tooltip } from "react-tooltip";
import { useDispatch } from "react-redux";
import { Decorate, Undecorate } from "../../redux/features/frame/frameSlice";

export default function Objects() {
  const [objects, setObjects] = useState([]);
  const imgRef = useRef();
  const dispatch = useDispatch();

  function addObjects() {
    const file = imgRef.current.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      objects.push(reader.result);
      const payload = reader.result;
      dispatch(Decorate(payload));
    };
  }

  function deleteObjects() {
    dispatch(Undecorate());
  }

  return (
    <div>
      <label htmlFor="objects">이미지를 추가해서 프레임을 꾸며보세요</label>
      <div className="alignTwoButtons">
        <label htmlFor="objects" className="forFile">
          스티커 추가
        </label>
        <input
          id="objects"
          type="file"
          accept="image/*"
          ref={imgRef}
          onChange={addObjects}
        ></input>

        <button
          id="deleteObjects"
          type="button"
          value="삭제하기"
          onClick={deleteObjects}
          className="btn aligncenter"
          data-tooltip-place="bottom-start"
          data-tooltip-id="object-menual"
          data-tooltip-content="이미지를 삭제하려면
          이미지를 클릭하고
                삭제 버튼을 클릭하세요"
        >
          삭제하기
        </button>
        <Tooltip id="object-menual" place="bottom-start" />
      </div>
    </div>
  );
}

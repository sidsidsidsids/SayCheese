// 꾸미기 컴포넌트입니다
import React, { useState, useRef } from "react";
// redux
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
      <label htmlFor="objects" className="forFile">
        스티커를 추가하세요
      </label>

      <input
        id="objects"
        type="file"
        accept="image/*"
        ref={imgRef}
        onChange={addObjects}
      ></input>

      <label htmlFor="deleteObjects">
        이미지를 삭제하려면 <br /> 이미지를 클릭하고 <br />
        삭제 버튼을 클릭하세요
      </label>
      <button
        id="deleteObjects"
        type="button"
        value="삭제하기"
        onClick={deleteObjects}
      >
        삭제하기
      </button>
    </div>
  );
}

// 프레임 배경을 바꾸는 툴의 디테일 컴포넌트입니다
import React, { useState, useRef } from "react";
// third party
import { useDispatch } from "react-redux";

import { Repaint } from "../../redux/features/frame/frameSlice";

export default function BgColor() {
  const [customColor, setCustomColor] = useState();
  const [imgFile, setImgFile] = useState();
  const imgRef = useRef();

  const dispatch = useDispatch();

  // 이미지 업로드 input의 onChange
  const saveImgFile = () => {
    const file = imgRef.current.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      setImgFile(reader.result);
      console.log(reader.result);
    };
    const payload = { color: customColor, image: imgFile };
    dispatch(Repaint(payload));
  };
  // 배경색 input 의 onChange
  const colorPick = (e) => {
    setCustomColor(e.target.value);
    const payload = { color: customColor, image: imgFile };
    dispatch(Repaint(payload));
  };
  return (
    <>
      <br></br>
      <label htmlFor="bgColor">색을 선택할 수 있습니다.</label>
      <br />
      <input id="bgColor" type="color" onChange={colorPick} />

      <br></br>
      <label htmlFor="bgImage">색을 선택할 수 있습니다.</label>
      <br />
      <input
        id="bgImage"
        type="file"
        accept="image/*"
        ref={imgRef}
        onChange={saveImgFile}
      />
      <div>이미지 미리보기 </div>
      <img width="100px" src={imgFile} />
      <div>이미지 제거하기 </div>
    </>
  );
}

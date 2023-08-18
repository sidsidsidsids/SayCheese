// 프레임 배경을 바꾸는 툴의 디테일 컴포넌트입니다
import React, { useState, useRef } from "react";
// third party
import { Tooltip } from "react-tooltip";
import { useDispatch, useSelector } from "react-redux";

import { Repaint, RemoveBgImg } from "../../redux/features/frame/frameSlice";
import Button from "../../Button";

export default function BgColor() {
  const [customColor, setCustomColor] = useState();
  const [imgFile, setImgFile] = useState();
  const imgRef = useRef();

  const { bgColor, bgImg } = useSelector((store) => store.frame);
  const dispatch = useDispatch();

  // 이미지 업로드 input의 onChange
  const saveImgFile = () => {
    const file = imgRef.current.files[0];
    if (file) {
      // 파일이 있으면
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        setImgFile(reader.result);
        // image: imgFile이면 안된다.
        // 리액트 컴포넌트는 일종의 스냅샷이기때문에 지금 이상태에서
        // reader.result와 imgFile은 서로 다르다!!!
        // 그래서 바로바로 반영이 안되고 딜레이가 생기는 것이었다
        const payload = { color: customColor, image: reader.result };
        dispatch(Repaint(payload));
      };
    }
  };
  // 배경색 input 의 onChange
  const colorPick = (event) => {
    setCustomColor(event.target.value);
    const payload = { color: event.target.value, image: imgFile };
    dispatch(Repaint(payload));
  };

  // 파일 인풋 값 초기화
  const resetInput = () => {
    imgRef.current.value = ""; // 파일 선택을 리셋
    setImgFile(false); // 이미지 파일 상태 초기화
    dispatch(RemoveBgImg()); // Redux 액션을 사용하여 배경 이미지 상태 초기화
  };

  return (
    <>
      <Tooltip id="bg-menual" place="bottom" />
      <label
        htmlFor="bgColor"
        data-tooltip-place="bottom"
        data-tooltip-id="bg-menual"
        data-tooltip-content="배경을 바꾸면 프레임의 꾸미기는 모두 초기화됩니다"
      >
        색을 선택할 수 있습니다.
      </label>
      <input id="bgColor" type="color" value={bgColor} onChange={colorPick} />
      <label
        htmlFor="bgColor"
        data-tooltip-place="top"
        data-tooltip-id="bg-menual"
        data-tooltip-content="배경을 바꾸면 프레임의 꾸미기는 모두 초기화됩니다"
      >
        배경 이미지를 첨부할 수 있습니다.
      </label>
      <div className="alignTwoButtons">
        <label htmlFor="bgImage" className="forFile">
          이미지 파일
        </label>
        <button
          className="btn "
          type="button"
          onClick={() => {
            resetInput();
          }}
        >
          제거하기
        </button>
      </div>
      <input
        id="bgImage"
        type="file"
        accept="image/*"
        ref={imgRef}
        onChange={() => {
          saveImgFile();
        }}
      />
      <div className="preview">
        <div>이미지 미리보기</div>
        {bgImg ? <img src={bgImg} alt="배경 이미지 미리보기" /> : ""}
      </div>
    </>
  );
}

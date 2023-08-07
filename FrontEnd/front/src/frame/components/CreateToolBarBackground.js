// 프레임 배경을 바꾸는 툴의 디테일 컴포넌트입니다
import React, { useState, useRef } from "react";
// third party
import { useDispatch, useSelector } from "react-redux";
// redux
import { Repaint, RemoveBgImg } from "../../redux/features/frame/frameSlice";

export default function BgColor() {
  const [customColor, setCustomColor] = useState();
  const [imgFile, setImgFile] = useState();
  const imgRef = useRef();

  const { bgColor, bgImg } = useSelector((store) => store.frame);
  const dispatch = useDispatch();

  // 이미지 업로드 input의 onChange
  const saveImgFile = () => {
    const file = imgRef.current.files[0];
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
  };
  // 배경색 input 의 onChange
  const colorPick = (e) => {
    setCustomColor(e.target.value);
    const payload = { color: e.target.value, image: imgFile };
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
      <br></br>
      <label htmlFor="bgColor">색을 선택할 수 있습니다.</label>
      <br />
      <input id="bgColor" type="color" value={bgColor} onChange={colorPick} />

      <br></br>
      <label htmlFor="bgImage" className="forFile">
        배경 이미지 추가
      </label>
      <br />
      <input
        id="bgImage"
        type="file"
        accept="image/*"
        ref={imgRef}
        onChange={saveImgFile}
      />
      <div>이미지 미리보기 </div>

      {bgImg ? (
        <img width="100px" src={bgImg} alt="배경 이미지 미리보기" />
      ) : (
        <br />
      )}
      <br />
      <button
        type="buttom"
        onClick={() => {
          resetInput();
        }}
      >
        이미지 제거하기
      </button>
    </>
  );
}

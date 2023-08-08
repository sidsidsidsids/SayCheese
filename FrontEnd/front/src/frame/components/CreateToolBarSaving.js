// 프레임 저장하기 컴포넌트 입니다
import React, { useState } from "react";

import { useDispatch } from "react-redux";
import { DoDownload } from "../../redux/features/frame/frameSlice";
// 저장하기 -> 여기서 버튼 클릭하면 이벤트로 캔버스 JS에서 저장하기 만들어야함
// 저장하기 로직 1. 저장하기 누르고 로그인 되어있으면 다음단계 노로그인이면 로그인하라고 알러트..음...

// 포스트 AXIOS 요청하기
export default function Saving() {
  const dispatch = useDispatch();

  return (
    <>
      <button
        className="btn alignCenter"
        onClick={() => dispatch(DoDownload())}
      >
        파일 다운로드
      </button>
      <button className="btn alignCenter">업로드</button>
    </>
  );
}

// 프레임 저장하기 컴포넌트 입니다
import React, { useState, useEffect, useInsertionEffect } from "react";

import { useDispatch } from "react-redux";
import { DoDownload, PostSignal } from "../../redux/features/frame/frameSlice";
// 저장하기 -> 여기서 버튼 클릭하면 이벤트로 캔버스 JS에서 저장하기 만들어야함

// (0)업로드를 누르지 않은 상태, 업로드를 누르면 (1)
// (1)이미 로그인 된 유저라면 uploadStage를 바로 stage2으로 가고
// (2)로그인 안된 유저라면 로그인 유도하고 로그인하면 stage2으로 간다
// (3)업로드 되면 완료됨을 띄움

// 포스트 AXIOS 요청하기
export default function Saving() {
  const [isUser, setIsUser] = useState(false);
  const [frameName, setFrameName] = useState("");
  const [privateCheck, setPrivateCheck] = useState("");
  const [uploadStage, setUploadStage] = useState(0);
  const dispatch = useDispatch();

  function stage1() {
    return (
      <div>
        <form>
          <input id="userId" placeholder="ID"></input>
          <input id="userPw" placeholder="Password"></input>
          <button className="btn alignCenter">로그인하기</button>
          {/* TODO: 로그인 되는 즉시 return 200이라면 setisUser(true) + 그리고 setUploadStage(2) */}
        </form>
      </div>
    );
  }
  function postFrame() {
    if (frameName !== "") {
      const payload = {
        frameName,
        privateCheck,
      };
      dispatch(PostSignal(payload));
    } else {
      alert("제목을 입력하세요");
    }
  }
  function stage2() {
    return (
      <div>
        <form>
          <input
            type="text"
            placeholder="프레임 제목"
            onChange={(event) => {
              setFrameName(event.target.value);
            }}
          ></input>
          <br></br>

          <input
            type="checkbox"
            id="private"
            onChange={(event) => {
              setPrivateCheck(event.target.checked); //true || false
            }}
          ></input>
          <label htmlFor="private">혼자만 사용하기</label>
          <button
            type="button"
            onClick={() => {
              postFrame();
            }}
            className="whtbtn alignCenter"
          >
            {/* TODO: Create API + setUploadStage(3) 업로드하기 */}
            업로드하기
          </button>
        </form>
      </div>
    );
  }

  function stage3() {
    return <div>업로드가 완료되었습니다.</div>;
  }

  // useEffect(

  // ,[uploadStage])

  return (
    <>
      <button
        className="btn alignCenter"
        onClick={() => dispatch(DoDownload())}
      >
        파일 다운로드
      </button>
      <button
        className="whtbtn alignCenter"
        style={{ margin: "20px auto 20px auto" }}
        onClick={() => {
          isUser === true ? setUploadStage(2) : setUploadStage(1);
        }}
      >
        업로드
      </button>
      {uploadStage === 3
        ? stage3() // 업로드 완료
        : uploadStage === 2
        ? stage2() // 로그인한 유저가 폼 입력
        : uploadStage === 1
        ? stage1() // 로그인
        : null}
      {true ? stage2() : 0}
    </>
  );
}

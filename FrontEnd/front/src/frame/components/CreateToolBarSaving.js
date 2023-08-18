// 프레임 저장하기 컴포넌트 입니다
import React, { useState, useEffect } from "react";

import { useDispatch } from "react-redux";
import axios from "axios";
import Swal from "sweetalert2";

import { DoDownload, PostSignal } from "../../redux/features/frame/frameSlice";
import {
  getUserInfo,
  loginSuccess,
} from "../../redux/features/login/loginSlice";
// 저장하기 -> 여기서 버튼 클릭하면 이벤트로 캔버스 JS에서 저장하기 만들어야함
// 업로드 STAGE 로직 설명 //
// (0)업로드를 누르지 않은 상태, 업로드를 누르면 (1)
// (1)이미 로그인 된 유저라면 uploadStage를 바로 stage2으로 가고
// (2)로그인 안된 유저라면 로그인 유도하고 로그인하면 stage2으로 간다
// (3)업로드 되면 완료됨을 띄움

// 포스트 AXIOS 요청하기
export default function Saving() {
  const [frameName, setFrameName] = useState(""); // 유저가 작성한 프레임 이름
  const [privateCheck, setPrivateCheck] = useState(""); // 프레임 비공개 여부
  const [uploadStage, setUploadStage] = useState(0); // 업로드 논리는 순차적 단계여야 합니다.
  const [email, setEmail] = useState(""); // 유저 이메일
  const [password, setPassword] = useState(""); // 유저 비밀번호
  const accessToken = localStorage.getItem("accessToken");
  const dispatch = useDispatch();

  // stage1
  function stage1() {
    return (
      <div>
        <form>
          <label htmlFor="userId">이메일</label>
          <input
            id="userId"
            type="email"
            value={email}
            onChange={(event) => {
              event.stopPropagation();
              setEmail(event.target.value);
            }}
          ></input>
          <label htmlFor="userPW">비밀번호</label>
          <input
            id="userPw"
            type="password"
            value={password}
            onChange={(event) => {
              event.stopPropagation();
              setPassword(event.target.value);
            }}
          ></input>
          <button
            type="button"
            className="btn alignCenter"
            onClick={(event) => {
              event.stopPropagation();
              handleLogin(event);
            }}
          >
            로그인하기
          </button>
        </form>
      </div>
    );
  }
  // stage2
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
          <label htmlFor="xprivate">혼자만 사용하기</label>
          <button
            type="button"
            onClick={(event) => {
              if (frameName.length <= 2) {
                Swal.fire("프레임 이름은 꼭 3글자 이상이어야 합니다");
              } // 글자 세글자 이상 아니면  api 리턴 400
              else {
                postFrame();
              }
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
  const [callbackOK, setCallbackOK] = useState(false);
  useEffect(() => {
    if (callbackOK) {
      dispatch(loginSuccess);
    }
  });
  // stage3
  function stage3() {
    return <div>업로드가 요청 완료되었습니다.</div>;
  }
  //  프레임 정보 전역상태관리 함수 for stage2
  function postFrame() {
    // 텅 빈 제목은 허용하지 않습니다
    if (frameName !== "") {
      const payload = {
        frameName,
        privateCheck,
      };
      dispatch(PostSignal(payload));
      setUploadStage(3); // 마지막 단계로 넘어갑니다
    } else {
      alert("제목을 입력하세요");
    }
  }
  // 로그인 처리 함수 for  stage1
  function handleLogin(event) {
    event.preventDefault();
    let data = {
      email: email,
      password: password,
    };

    axios
      .post("/api/login", data, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        const accessToken = response.headers["authorization"];
        const refreshToken = response.headers["refreshtoken"];

        axios.defaults.headers.common["Authorization"] = `${accessToken}`;

        if (response.status === 200) {
          localStorage.setItem("accessToken", accessToken);
          localStorage.setItem("refreshToken", refreshToken);
          setCallbackOK(true);
          dispatch(loginSuccess());
          dispatch(getUserInfo());
          setUploadStage(2);
        }
      })
      .catch((error) => {
        console.log(error);
        if (error.response.status === 401) {
          alert("이메일이나 비밀번호를 확인해주세요.");
        }
      });
  }
  return (
    <div className="savingButton">
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
          accessToken ? setUploadStage(2) : setUploadStage(1); // 이미 로그인된 유저는 바로 프레임 입력 폼을 봅니다
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
    </div>
  );
}

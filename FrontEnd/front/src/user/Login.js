import React, { useEffect, useState } from "react";
// third party
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import Swal from "sweetalert2";
// local
import "./Login.css";
import Button from "../Button";
import { getUserInfo, loginSuccess } from "../redux/features/login/loginSlice";
import PwFindModal from "./PwFindModal";

function Login() {
  const [activeIndex, setActiveIndex] = useState(null);

  const [email, setEmail] = useState(""); // 이메일
  const [password, setPassword] = useState(""); // 비밀번호

  const [callbackOK, setCallbackOK] = useState(false);
  const [isPwFindModalOpen, setIsPwFindModalOpen] = useState(false); // 비밀번호 잊어버렸을 때 모달

  const { isLogin } = useSelector((store) => store.login); // 로그인 여부

  const movePage = useNavigate();
  const dispatch = useDispatch();

  const moveSignUpPage = () => {
    movePage("/user/signup"); // 회원가입 페이지로 이동
  };

  // 각 input 요소에 focus가 있을 때 해당 div의 index를 activeIndex 상태로 설정
  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  // input 요소가 포커스 잃었을 때 activeIndex 초기화
  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  // 비밀번호 잊어버렸을 때 모달 열기 - true일 경우 모달 open
  const handlePwFineModalOpen = () => {
    setIsPwFindModalOpen(true);
  };

  useEffect(() => {
    if (callbackOK) {
      dispatch(loginSuccess);
    }
  });

  useEffect(() => {
    if (isLogin) {
      // 이미 로그인한 상태일 경우 해당 페이지에 접근하면 "/main" 경로로 리다이렉션
      movePage("/main");
    }
  }, [isLogin, movePage]);

  // 로그인 axios
  function handleClickSubmit(event) {
    event.preventDefault();

    let data = {
      email: email,
      password: password,
    };

    axios
      .post("/api/login", JSON.stringify(data), {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        // accessToken과 refreshToken
        const accessToken = response.headers["authorization"];
        const refreshToken = response.headers["refreshtoken"];

        axios.defaults.headers.common["Authorization"] = `${accessToken}`;

        if (response.status === 200) {
          localStorage.setItem("accessToken", accessToken);
          localStorage.setItem("refreshToken", refreshToken);
          setCallbackOK(true);
          dispatch(loginSuccess());
          dispatch(getUserInfo());
          movePage("/main"); // main 페이지로 이동
        }
      })
      .catch((error) => {
        console.log(error);
        if (error.response.status === 401) {
          Swal.fire("이메일이나 비밀번호를 확인해주세요.");
        }
      });
  }

  return (
    <div className="LoginWrapper">
      <div className="LoginBox">
        <div>
          <h2 className="UserBoxText">로그인</h2>
          <form onSubmit={handleClickSubmit}>
            <div
              className={`UserInputLine ${activeIndex === 1 ? "focused" : ""}`}
            >
              <input
                type="email"
                name="email"
                placeholder="이메일"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                onFocus={() => handleInputFocus(1)}
                onBlur={handleInputBlur}
              />
            </div>
            <div
              className={`UserInputLine ${activeIndex === 2 ? "focused" : ""}`}
            >
              <input
                type="password"
                name="password"
                placeholder="비밀번호"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                onFocus={() => handleInputFocus(2)}
                onBlur={handleInputBlur}
              />
            </div>
            <Button className="LoginBtn" text={"로그인"} type="submit" />
          </form>
          <div className="BtnSort">
            <p className="PwFindBtn" onClick={handlePwFineModalOpen}>
              비밀번호를 잊어버렸나요?
            </p>
            <Button
              className="LoginEtcBtn"
              text={"회원가입"}
              onClick={moveSignUpPage}
            />
            {isPwFindModalOpen && (
              <PwFindModal close={() => setIsPwFindModalOpen(false)} />
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;

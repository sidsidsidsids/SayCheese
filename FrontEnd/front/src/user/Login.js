import React, { useEffect, useState } from "react";
import "./Login.css";
import Button from "../Button";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import { useSelector, useDispatch } from "react-redux";
import { getUserInfo, loginSuccess } from "../redux/features/login/loginSlice";

function Login() {
  const [email, setEmail] = useState(""); // 이메일
  const [password, setPassword] = useState(""); // 비밀번호

  const [callbackOK, setCallbackOK] = useState(false);

  const movePage = useNavigate();
  const { isLogin } = useSelector((store) => store.login); // 로그인 여부

  const dispatch = useDispatch();

  const moveSignUpPage = () => {
    movePage("/user/signup"); // 회원가입 페이지로 이동
  };

  const [activeIndex, setActiveIndex] = useState(null);

  // 각 input 요소에 focus가 있을 때 해당 div의 index를 activeIndex 상태로 설정
  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  // input 요소가 포커스 잃었을 때 activeIndex 초기화
  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  useEffect(() => {
    if (callbackOK) {
      dispatch(loginSuccess);
    }
  });

  useEffect(() => {
    if (isLogin) {
      // 이미 로그인한 상태일 경우 해당 페이지에 접근하면 "/" 경로로 리다이렉션
      movePage("/");
    }
  }, [isLogin, movePage]);

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
        console.log(response);
        const accessToken = response.headers["authorization"];
        const refreshToken = response.headers["refreshtoken"];

        axios.defaults.headers.common["Authorization"] = `${accessToken}`;

        console.log(axios.defaults.headers.common["Authorization"]);

        if (response.status === 200) {
          localStorage.setItem("accessToken", accessToken);
          localStorage.setItem("refreshToken", refreshToken);
          setCallbackOK(true);
          dispatch(loginSuccess());
          dispatch(getUserInfo());
          movePage("/");
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
            <Button className="LoginEtcBtn" text={"비밀번호 찾기"} />
            <Button
              className="LoginEtcBtn"
              text={"회원가입"}
              onClick={moveSignUpPage}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;

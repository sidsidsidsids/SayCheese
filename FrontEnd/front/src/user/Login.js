import React, { useContext, useState } from "react";
import "./Login.css";
import Button from "../Button";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { AuthContext } from "../contexts/AuthContext";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const { setIsLogin } = useContext(AuthContext);

  const movePage = useNavigate();

  // 회원가입 페이지로 이동
  const moveSignUpPage = () => {
    movePage("/user/signup");
  };

  const [activeIndex, setActiveIndex] = useState(null);

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };
  // 각 input 요소에 focus가 있을 때 해당 div의 index를 activeIndex 상태로 설정

  const handleInputBlur = () => {
    setActiveIndex(null);
  };
  // input 요소가 포커스 잃었을 때 activeIndex 초기화

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
        const accessTokenHeader = response.headers["authorization"];
        console.log(response.headers["authorization"]);

        const accessToken = accessTokenHeader.split("Bearer ")[1].trim();

        console.log(accessToken);

        axios.defaults.headers.common[
          "Authorization"
        ] = `Bearer ${accessToken}`;

        console.log(axios.defaults.headers.common["Authorization"]);

        if (response.status === 200) {
          setIsLogin(true);
          movePage("/");
        }
      })
      .catch((error) => {
        if (error.response.status === 401) {
          alert("이메일이나 비밀번호를 확인해주세요.");
        }
        console.log(error);
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

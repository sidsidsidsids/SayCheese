import { useState } from "react";
import "./Login.css";
import Button from "../Button";

function Login() {
  const [activeIndex, setActiveIndex] = useState(null);

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };
  // 각 input 요소에 focus가 있을 때 해당 div의 index를 activeIndex 상태로 설정

  const handleInputBlur = () => {
    setActiveIndex(null);
  };
  // input 요소가 포커스 잃었을 때 activeIndex 초기화

  return (
    <div className="LoginWrapper">
      <div className="LoginBox">
        <div>
          <h2 className="UserBoxText">로그인</h2>
          <form>
            <div
              className={`UserInputLine ${activeIndex === 1 ? "focused" : ""}`}
            >
              <input
                type="email"
                name="email"
                placeholder="이메일"
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
                onFocus={() => handleInputFocus(2)}
                onBlur={handleInputBlur}
              />
            </div>
            <Button className="LoginBtn" text={"로그인"} />
            <div className="BtnSort">
              <Button className="LoginEtcBtn" text={"비밀번호 찾기"} />
              <Button className="LoginEtcBtn" text={"회원가입"} />
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;

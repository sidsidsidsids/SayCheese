import { useState } from "react";
import "./SignUp.css";
import Button from "../Button";

function SignUp() {
  const [activeIndex, setActiveIndex] = useState(null);

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  return (
    <div className="SignUpWrapper">
      <div className="SignUpBox">
        <div>
          <h2 className="UserBoxText">회원가입</h2>
          <form>
            <div className="SignBtnInputSort">
              <div
                className={`SignUpInputLine ${
                  activeIndex === 1 ? "focused" : ""
                }`}
              >
                <input
                  type="email"
                  name="email"
                  placeholder="이메일"
                  onFocus={() => handleInputFocus(1)}
                  onBlur={handleInputBlur}
                />
              </div>
              <Button className="SignUpCheckBtn" text={"확인"} />
            </div>
            <div className="SignBtnInputSort">
              <div
                className={`SignUpInputLine ${
                  activeIndex === 2 ? "focused" : ""
                }`}
              >
                <input
                  type="text"
                  name="certification"
                  placeholder="인증번호"
                  onFocus={() => handleInputFocus(2)}
                  onBlur={handleInputBlur}
                />
              </div>
              <Button className="SignUpCheckBtn" text={"인증"} />
            </div>
            <div className="SignBtnInputSort">
              <div
                className={`SignUpInputLine ${
                  activeIndex === 3 ? "focused" : ""
                }`}
              >
                <input
                  type="text"
                  name="nickname"
                  placeholder="닉네임"
                  onFocus={() => handleInputFocus(3)}
                  onBlur={handleInputBlur}
                />
              </div>
              <Button className="SignUpCheckBtn" text={"중복확인"} />
            </div>
            <div
              className={`SignUpInputLine ${
                activeIndex === 4 ? "focused" : ""
              }`}
            >
              <input
                type="password"
                name="password"
                placeholder="비밀번호"
                className="SignUpInputSize"
                onFocus={() => handleInputFocus(4)}
                onBlur={handleInputBlur}
              />
            </div>
            <div
              className={`SignUpInputLine ${
                activeIndex === 5 ? "focused" : ""
              }`}
            >
              <input
                type="password"
                name="passwordcheck"
                placeholder="비밀번호 확인"
                className="SignUpInputSize"
                onFocus={() => handleInputFocus(5)}
                onBlur={handleInputBlur}
              />
            </div>
            <p className="pwcheckmsg">! 비밀번호를 입력해주세요 !</p>
            <div
              className={`SignUpInputLine ${
                activeIndex === 6 ? "focused" : ""
              }`}
            >
              <input
                type="text"
                name="name"
                placeholder="이름"
                className="SignUpInputSize"
                onFocus={() => handleInputFocus(6)}
                onBlur={handleInputBlur}
              />
            </div>
            <div className="SignGenderAgeSort">
              <div className="SelectGender">
                <input type="radio" id="man" name="Gender" />
                <label for="man">남자</label>
                <input type="radio" id="woman" name="Gender" />
                <label for="woman">여자</label>
              </div>
              <div
                className={`SignUpInputLine ${
                  activeIndex === 7 ? "focused" : ""
                }`}
              >
                <input
                  type="number"
                  name="age"
                  placeholder="나이"
                  className="AgeSize"
                  onFocus={() => handleInputFocus(7)}
                  onBlur={handleInputBlur}
                />
              </div>
            </div>
            <Button className="SignUpBtn" text={"회원가입"} />
          </form>
        </div>
      </div>
    </div>
  );
}

export default SignUp;

import { useState } from "react";
import "./SignUp.css";
import Button from "../Button";

function SignUp() {
  const [activeIndex, setActiveIndex] = useState(null);

  // 비밀번호 확인 및 메시지
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [passwordMessage, setPasswordMessage] =
    useState("! 비밀번호를 입력해주세요 !");

  // 이메일 메시지
  const [email, setEmail] = useState("");
  const [emailMessage, setEmailMessage] = useState("이메일을 입력해주세요.");

  // 나이
  const [age, setAge] = useState("");

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  const handlePasswordCheck = () => {
    // 비밀번호 정규 표현식 - 영어+숫자+특수기호 8~25자
    const passwordRegExp =
      /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    if (password === "" || passwordCheck === "") {
      setPasswordMessage("! 비밀번호를 입력해주세요 !");
    } else if (
      !passwordRegExp.test(password) ||
      !passwordRegExp.test(passwordCheck)
    ) {
      setPasswordMessage(
        "! 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하 입력해주세요 !"
      );
    } else if (password === passwordCheck) {
      setPasswordMessage("✔ 비밀번호가 일치합니다 ✔");
    } else {
      setPasswordMessage("🚫 비밀번호가 일치하지 않습니다 🚫");
    }
  };

  // 각각의 비밀번호 입력 요소의 값을 상태에 저장
  const handlePasswordChange = (event) => {
    const { name, value } = event.target;
    if (name === "password") {
      // event.target.name이 password일 경우 password의 값을 입력받은 것으로 바꿔줌
      setPassword(value);
    } else if (name === "passwordcheck") {
      // event.target.name이 passwordcheck일 경우 passwordcheck의 값을 입력받은 것으로 바꿔줌
      setPasswordCheck(value);
    }
  };

  const handleEmailChange = (event) => {
    const currentEmail = event.target.value;
    setEmail(currentEmail);
    // 이메일 정규표현식
    const emailRegExp =
      /^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$/;
    if (!emailRegExp.test(currentEmail)) {
      setEmailMessage("이메일 형식이 올바르지 않습니다. 다시 확인해주세요.");
    } else {
      setEmailMessage("사용 가능한 이메일입니다.");
    }
  };

  const emailAlert = () => {
    alert(emailMessage);
  };

  // 입력한 나이가 0 이하 또는 100 이상일 경우 alert
  const handleAgeChange = (event) => {
    const currentAge = event.target.value;
    setAge(currentAge);
    if (currentAge <= 0 || currentAge >= 100) {
      alert("나이를 다시 확인해주세요.");
    }
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
                  value={email}
                  onChange={handleEmailChange}
                  onFocus={() => handleInputFocus(1)}
                  onBlur={handleInputBlur}
                />
              </div>
              <Button
                className="SignUpCheckBtn"
                text={"확인"}
                onClick={emailAlert}
              />
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
                value={password}
                onChange={handlePasswordChange}
                onFocus={() => handleInputFocus(4)}
                onBlur={() => {
                  handleInputBlur();
                  handlePasswordCheck();
                }}
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
                value={passwordCheck}
                onChange={handlePasswordChange}
                onFocus={() => handleInputFocus(5)}
                onBlur={() => {
                  handleInputBlur();
                  handlePasswordCheck();
                }}
              />
            </div>
            <p
              className={`pwcheckmsg ${
                passwordMessage === "✔ 비밀번호가 일치합니다 ✔"
                  ? "SamePassword"
                  : ""
              }`}
            >
              {passwordMessage}
            </p>
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
                  onChange={handleAgeChange}
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

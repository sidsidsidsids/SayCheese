import { useEffect, useState } from "react";
// third party
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";
// local
import "./SignUp.css";
import Button from "../Button";

function SignUp() {
  const [activeIndex, setActiveIndex] = useState(null);

  const [email, setEmail] = useState(""); // 이메일
  const [emailMessage, setEmailMessage] = useState("이메일을 입력해주세요."); // 이메일 메시지
  const [emailNum, setEmailNum] = useState(""); // 인증번호

  const [password, setPassword] = useState(""); // 비밀번호
  const [passwordCheck, setPasswordCheck] = useState(""); // 비밀번호 확인
  const [passwordMessage, setPasswordMessage] =
    useState("! 비밀번호를 입력해주세요 !"); // 비밀번호 메시지

  const [nickname, setNickname] = useState(""); // 닉네임
  const [nicknameMessage, setNicknameMessage] =
    useState("닉네임을 입력해주세요."); // 닉네임 메시지

  const [name, setName] = useState(""); // 이름
  const [gender, setGender] = useState(""); // 성별
  const [age, setAge] = useState(""); // 나이

  const [emailRegExpCheck, setEmailRegExpCheck] = useState(false); // 이메일 형식 체크
  const [emailCheck, setEmailCheck] = useState(false); // 사용 가능한 이메일인지 체크
  const [emailNumCheck, setEmailNumCheck] = useState(false); // 인증번호 맞게 입력했는지 체크

  const [nicknameCheck, setNicknameCheck] = useState(false); // 사용 가능한 닉네임인지 체크
  const [passwordInputCheck, setPasswordInputCheck] = useState(false); // 비밀번호 잘못 입력했는지 체크
  const [agreeCheck, setAgreeCheck] = useState(false); // 개인정보 제공 동의했는지 체크
  const [nullInputCheck, setNullInputCheck] = useState(false);
  // 필수 입력해야하는 부분 중 한 곳이라도 잘못 입력한 곳 있는지 체크

  const movePage = useNavigate(); // 페이지 이동

  useEffect(() => {
    setNullInputCheck(emailCheck && emailNumCheck && nicknameCheck);
    // 이메일 형식, 인증번호, 닉네임 중복 모두 확인됐을 경우 true
  }, [email, emailCheck, emailNum, emailNumCheck, nickname, nicknameCheck]);

  useEffect(() => {
    getNicknameMessage();
  }, []);

  useEffect(() => {
    handlePasswordCheck();
  }, [passwordInputCheck, password, passwordCheck]);

  useEffect(() => {}, [agreeCheck]);

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  const handleEmailChange = (event) => {
    const currentEmail = event.target.value; // 입력한 이메일
    setEmail(currentEmail); // email에 현재 입력한 이메일 저장
    // 이메일 정규표현식 - ####@####.### 형식인지
    const emailRegExp =
      /^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$/;
    if (!emailRegExp.test(currentEmail)) {
      setEmailMessage("이메일 형식이 올바르지 않습니다. 다시 확인해주세요.");
      setEmailRegExpCheck(false);
      setEmailCheck(false);
    } else {
      setEmailRegExpCheck(true);
      getEmailMessage(currentEmail);
    }
  };

  // axios 통신을 통해 emailMessage에 alert할 메시지 담기
  async function getEmailMessage(currentEmail) {
    let data = {
      email: currentEmail,
    };
    try {
      // 이메일 중복 체크 - currentEmail은 입력한 이메일
      const response = await axios.post("/api/member/id-check", data, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      // 이메일이 사용가능할 경우 인증번호 발송
      try {
        const secondResponse = await axios.post(
          "/api/email/auth",
          { email: currentEmail },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        setEmailCheck(true);
        setEmailMessage(
          "사용 가능한 이메일입니다.\n해당 이메일로 인증번호를 전송했습니다.\n인증번호 입력을 부탁합니다."
        );
      } catch (secondError) {
        console.log(secondError);
        setEmailMessage(
          "오류로 인해 인증번호 전송이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      }
    } catch (error) {
      setEmailCheck(false);
      if (error.response.status === 400) {
        // 잘못된 값이 들어왔을 경우
        setEmailMessage(error.response.data.message);
      } else {
        setEmailMessage(
          "오류로 인해 이메일 확인이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      }
    }
  }

  // emailMessage에 담은 메시지 alert하는 함수
  function emailAlert() {
    Swal.fire(`${emailMessage}`);
  }

  // 인증번호 확인 메시지 출력하는 함수
  async function getEmailNumMessage() {
    let data = {
      email: email,
      token: emailNum,
    };
    // 이메일에 전송된 인증번호 인증
    try {
      const response = await axios.post("/api/email/auth/check", data, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      setEmailNumCheck(true);
      Swal.fire(response.data.message);
    } catch (error) {
      setEmailNumCheck(false);
      if (error.response.status === 400) {
        Swal.fire("인증번호 입력을 부탁드립니다.");
      } else if (error.response.status === 404) {
        Swal.fire("잘못 입력하셨습니다.\n인증번호를 다시 확인 부탁드립니다.");
      } else {
        Swal.fire(
          "오류로 인해 인증번호 확인이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      }
    }
  }

  // 닉네임 입력값이 바뀔때
  const handleNicknameChange = (event) => {
    const currentNickname = event.target.value; // 현재 입력한 닉네임
    setNickname(currentNickname); // nickname에 현재 입력한 닉네임 저장
    getNicknameMessage(currentNickname); // 닉네임 중복체크하여 닉네임 메시지 저장하는 함수 실행
  };

  // axios 통신을 통해 nicknameMessage alert할 메시지 담기
  async function getNicknameMessage(currentNickname) {
    let data = {
      nickname: currentNickname,
    };
    try {
      // 닉네임 중복체크
      const response = await axios.post("/api/member/nickname-check", data, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      setNicknameCheck(true);
      setNicknameMessage(response.data.message);
    } catch (error) {
      setNicknameCheck(false);
      // 이미 사용중인 닉네임인 경우
      if (error.response.status === 400) {
        setNicknameMessage(error.response.data.message);
      } else {
        setNicknameMessage(
          "오류로 인해 닉네임 확인이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      }
    }
  }

  // nicknameMessage 담은 메시지 alert하는 함수
  function nicknameAlert() {
    Swal.fire(`${nicknameMessage}`);
  }

  const handlePasswordCheck = () => {
    // 비밀번호 정규 표현식 - 영어+숫자+특수기호 8~25자
    const passwordRegExp =
      /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    if (password === "" || passwordCheck === "") {
      // 비밀번호 입력란 또는 비밀번호 확인 입력란이 공백일 경우
      setPasswordMessage("! 비밀번호를 입력해주세요 !");
      setPasswordInputCheck(false);
    } else if (
      // 비밀번호 또는 비밀번호 확인에 입력한 값이 형식에 맞지 않을 경우
      !passwordRegExp.test(password) ||
      !passwordRegExp.test(passwordCheck)
    ) {
      setPasswordMessage(
        "! 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하 입력해주세요 !"
      );
      setPasswordInputCheck(false);
    } else if (password === passwordCheck) {
      // 비밀번호와 비밀번호 확인에 입력한 값이 일치할 경우
      setPasswordMessage("✔ 비밀번호가 일치합니다 ✔");
      setPasswordInputCheck(true);
    } else {
      // 비밀번호와 비밀번호 확인에 입력한 값이 일치하지 않을 경우
      setPasswordMessage("🚫 비밀번호가 일치하지 않습니다 🚫");
      setPasswordInputCheck(false);
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

  // 입력한 나이가 0 이하 또는 100 이상일 경우 alert
  const handleAgeChange = (event) => {
    const currentAge = event.target.value;
    setAge(currentAge);
    if (currentAge <= 0 || currentAge >= 100) {
      Swal.fire("나이를 다시 확인해주세요.");
    }
  };

  const handleAgreeCheck = (event) => {
    setAgreeCheck(event.target.checked);
  };

  async function handleSignUp(event) {
    event.preventDefault();
    // 필수로 입력해야 하는 이메일, 비밀번호, 닉네임이 공백일 경우
    if (
      email === "" ||
      emailNum === "" ||
      password === "" ||
      passwordCheck === "" ||
      nickname === ""
    ) {
      return Swal.fire(
        "이메일, 비밀번호, 닉네임은 필수로 입력해야 합니다.\n빈 칸을 확인해주세요."
      );
      // 이메일이나 닉네임 잘못 입력한 경우
    } else if (!nullInputCheck) {
      return Swal.fire("이메일 또는 닉네임을 다시 확인해주세요.");
      // 비밀번호 잘못 입력한 경우
    } else if (!passwordInputCheck) {
      return Swal.fire(
        "비밀번호를 다시 확인해주세요.\n비밀번호는 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하 입력해야 합니다."
      );
      // 개인정보 제공 동의 체크하지 않은 경우
    } else if (!agreeCheck) {
      return Swal.fire("개인정보 제공 동의를 해주세요.");
    }

    let data = {
      email: email,
      password: password,
      nickname: nickname,
      genderFm: gender,
      age: age,
      name: name,
    };
    // 회원가입 시 입력한 정보들을 담아 axios post 통신
    axios
      .post("/api/member/join", data, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        Swal.fire("회원가입에 성공했습니다. 로그인 페이지로 이동합니다.");
        movePage("/user/login");
      })
      .catch((error) => {
        Swal.fire(
          "오류로 인해 회원가입이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      });
  }

  return (
    <div className="SignUpWrapper">
      <div className="SignUpBox">
        <div>
          <h2 className="UserBoxText">회원가입</h2>
          <form onSubmit={handleSignUp}>
            {/* onSubmit을 통해 form 태그 안에 있는 submit 타입의 버튼 클릭 시 handleSignUp 작동*/}
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
                type="button"
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
                  value={emailNum}
                  onChange={(event) => setEmailNum(event.target.value)}
                  /* onChange를 통해 해당 입력창에 입력한 값이 emailNum에 저장됨*/
                  onFocus={() => handleInputFocus(2)}
                  onBlur={handleInputBlur}
                />
              </div>
              <Button
                className="SignUpCheckBtn"
                text={"인증"}
                onClick={getEmailNumMessage}
                type="button"
              />
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
                  value={nickname}
                  onChange={handleNicknameChange}
                  onFocus={() => handleInputFocus(3)}
                  onBlur={handleInputBlur}
                />
              </div>
              <Button
                className="SignUpCheckBtn"
                text={"중복확인"}
                onClick={nicknameAlert}
                type="button"
              />
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
              className={`PwCheckMsg ${
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
                value={name}
                onChange={(event) => setName(event.target.value)}
                onFocus={() => handleInputFocus(6)}
                onBlur={handleInputBlur}
              />
            </div>
            <div className="SignGenderAgeSort">
              <div className="SelectGender">
                {/* 남자를 클릭하면 M값이 저장되고, 여자를 클릭하면 F값이 저장됨 */}
                <input
                  type="radio"
                  id="man"
                  name="Gender"
                  value="M"
                  onChange={(event) => setGender(event.target.value)}
                  checked={gender === "M"}
                />
                <label for="man">남자</label>
                <input
                  type="radio"
                  id="woman"
                  name="Gender"
                  value="F"
                  onChange={(event) => setGender(event.target.value)}
                  checked={gender === "F"}
                />
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
                  value={age}
                  className="AgeSize"
                  onChange={handleAgeChange}
                  onFocus={() => handleInputFocus(7)}
                  onBlur={handleInputBlur}
                />
              </div>
            </div>
            <div className="InfoInnerBox">
              <div className="InfoText">
                <h2 className="InfoTitleText">개인정보 제공 동의</h2>
                <p>
                  개인정보보호법에 따라 세이치즈에 회원가입 신청하시는 분께
                  수집하는 개인정보의 항목, 개인정보의 수집 및 이용목적,
                  개인정보의 보유 및 이용기간, 동의 거부권 및 동의 거부 시
                  불이익에 관한 사항을 안내 드리오니 자세히 읽은 후 동의하여
                  주시기 바랍니다.
                </p>
                <p>
                  이용자는 회원가입을 하지 않아도 사진 촬영, 이미지 게시판 접근
                  등 대부분의 세이치즈 서비스를 회원과 동일하게 이용할 수
                  있습니다. 이용자가 프레임 만들기, 내 네컷사진, 프레임 모아보기
                  등과 같이 개인화 혹은 회원제 서비스를 이용하기 위해 회원가입을
                  할 경우, 세이치즈는 서비스 이용을 위해 필요한 최소한의
                  개인정보를 수집합니다.
                </p>
              </div>
            </div>
            <div className="AgreeBox">
              <input
                type="checkbox"
                id="AgreeCheckBtn"
                onChange={handleAgreeCheck}
              />
              <label for="AgreeCheckBtn">동의합니다.</label>
            </div>
            <div className="SignUpBtnSort">
              <Button className="SignUpBtn" text={"회원가입"} type="submit" />
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default SignUp;

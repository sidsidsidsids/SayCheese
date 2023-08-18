import { useEffect, useState } from "react";
// third party
import axios from "axios";
import Swal from "sweetalert2";
// local
import "./PwFindModal.css";
import Button from "../Button";

function PwFindModal({ close }) {
  const [findStep, setFindStep] = useState(1); // 비밀번호 변경 모달 화면 순서
  const [activeIndex, setActiveIndex] = useState(null);
  const [email, setEmail] = useState(""); // 회원 이메일
  const [number, setNumber] = useState(""); // 입력한 이메일 인증번호
  const [password, setPassword] = useState(""); // 바꿀 비밀번호
  const [passwordCheck, setPasswordCheck] = useState(""); // 바꿀 비밀번호 확인
  const [passwordMessage, setPasswordMessage] =
    useState("! 비밀번호를 입력해주세요 !"); // 비밀번호 메시지
  const [passwordInputCheck, setPasswordInputCheck] = useState(false); // 비밀번호 잘못 입력했는지 체크

  useEffect(() => {
    handlePasswordCheck();
  }, [passwordInputCheck, password, passwordCheck]);

  useEffect(() => {
    // 모달 열리면 본문 스크롤 방지
    document.body.style.overflow = "hidden";
    // 현재 보고 있는 스크롤의 왼쪽 top을 가운데 정렬할 기준 top으로 해줌
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []);

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  // 이메일 인증번호 발송
  async function PwEmailSent() {
    let data = {
      email: email,
    };
    axios
      .post("/api/member/password", data, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        setFindStep(2);
      })
      .catch((error) => {
        console.log(error);
        if (error.response.status === 400) {
          Swal.fire("이메일을 다시 확인해주세요.");
        } else {
          Swal.fire(
            "오류로 인해 비밀번호 찾기를 진행할 수 없습니다.\n다시 시도해주시길 바랍니다."
          );
        }
      });
  }

  // 인증번호 확인
  async function checkEmailNum() {
    if (number === "") {
      return Swal.fire("인증번호 입력을 부탁드립니다.ㄴ");
    }
    let data = {
      email: email,
      token: number,
    };
    // 이메일에 전송된 인증번호 인증
    axios
      .post("/api/email/auth/check", data, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        Swal.fire(response.data.message);
        setFindStep(3);
      })
      .catch((error) => {
        Swal.fire("잘못 입력하셨습니다.\n인증번호를 다시 확인 부탁드립니다.");
      });
  }

  // 비밀번호 변경
  async function handleModifyPassword() {
    let data = {
      email: email,
      newPassword: password,
      passwordConfirm: passwordCheck,
    };
    if (password === "" || passwordCheck === "") {
      return Swal.fire("빈 칸을 확인해주세요.");
    } else if (!passwordInputCheck) {
      return Swal.fire(
        "비밀번호를 다시 확인해주세요.\n비밀번호는 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하 입력해야 합니다."
      );
    }

    axios
      .put("/api/member/password", data, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        Swal.fire("비밀번호가 변경됐습니다.\n로그인을 다시 시도해주세요.");
      })
      .catch((error) => {
        Swal.fire(
          "오류로 인해 비밀번호 변경이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      });
  }

  const handlePasswordCheck = () => {
    // 비밀번호 정규 표현식 - 영어+숫자+특수기호 8~25자
    const passwordRegExp =
      /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    if (password === "" || passwordCheck === "") {
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

  return (
    <div>
      <div className="modalBackdrop">
        <div className="PwFindModal">
          {findStep === 1 ? (
            <div>
              <div
                style={{
                  marginTop: "50px",
                  marginLeft: "50px",
                  marginBottom: "20px",
                  textAlign: "left",
                }}
              >
                <h1 style={{ margin: "0" }}>비밀번호 찾을</h1>
                <h1 style={{ margin: "0" }}>이메일을 입력해주세요</h1>
              </div>
              <div className="SignBtnInputSort">
                <div
                  className={`SignUpInputLine ${
                    activeIndex === 1 ? "focused" : ""
                  } `}
                >
                  <input
                    type="email"
                    name="email"
                    placeholder="이메일"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    className="PasswordSize"
                    onFocus={() => handleInputFocus(1)}
                    onBlur={handleInputBlur}
                  />
                </div>
              </div>
              <div className="RightSort">
                <Button
                  className="SignUpCheckBtn"
                  text={"확인"}
                  onClick={() => PwEmailSent()}
                />
              </div>
            </div>
          ) : findStep === 2 ? (
            <div>
              <h1>인증번호를 입력해주세요</h1>
              <div className="SignBtnInputSort">
                <div
                  className={`SignUpInputLine ${
                    activeIndex === 2 ? "focused" : ""
                  } `}
                >
                  <input
                    type="text"
                    name="number"
                    placeholder="인증번호"
                    value={number}
                    onChange={(event) => setNumber(event.target.value)}
                    className="PasswordSize"
                    onFocus={() => handleInputFocus(2)}
                    onBlur={handleInputBlur}
                  />
                </div>
              </div>
              <div className="RightSort">
                <Button
                  className="SignUpCheckBtn"
                  text={"확인"}
                  onClick={() => checkEmailNum()}
                />
              </div>
            </div>
          ) : findStep === 3 ? (
            <div>
              <h1>새 비밀번호를 입력해주세요</h1>
              <div className="SignBtnInputSort">
                <div
                  className={`SignUpInputLine ${
                    activeIndex === 3 ? "focused" : ""
                  } `}
                >
                  <input
                    type="password"
                    name="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={handlePasswordChange}
                    className="PasswordSize"
                    onFocus={() => handleInputFocus(3)}
                    onBlur={() => {
                      handleInputBlur();
                      handlePasswordCheck();
                    }}
                  />
                </div>
              </div>
              <div className="SignBtnInputSort">
                <div
                  className={`SignUpInputLine ${
                    activeIndex === 5 ? "focused" : ""
                  } `}
                >
                  <input
                    type="password"
                    name="passwordcheck"
                    placeholder="비밀번호 확인"
                    value={passwordCheck}
                    onChange={handlePasswordChange}
                    className="PasswordSize"
                    onFocus={() => handleInputFocus(5)}
                    onBlur={() => {
                      handleInputBlur();
                      handlePasswordCheck();
                    }}
                  />
                </div>
              </div>
              <p
                className={`PwFindCheckMsg ${
                  passwordMessage === "✔ 비밀번호가 일치합니다 ✔"
                    ? "SamePasswordFind"
                    : ""
                }`}
              >
                {passwordMessage}
              </p>
              <div className="RightSort">
                <Button
                  className="SignUpCheckBtn"
                  text={"확인"}
                  onClick={() => {
                    document.body.style.overflow = "auto";
                    close();
                    handleModifyPassword();
                  }}
                />
              </div>
            </div>
          ) : null}
          <button
            className="ModalClose"
            onClick={() => {
              document.body.style.overflow = "auto";
              close();
            }}
          >
            ×
          </button>
        </div>
      </div>
    </div>
  );
}

export default PwFindModal;

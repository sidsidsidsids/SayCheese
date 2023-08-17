import { useEffect, useState } from "react";
// third party
import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";
// local
import Button from "../Button";
import "./MyInfoModify.css";
import { getUserInfo } from "../redux/features/login/loginSlice";

function MyInfoModify() {
  const [activeIndex, setActiveIndex] = useState(null);

  const { userInfo } = useSelector((store) => store.login);
  const { email } = useParams(); // 경로의 email

  const [nickname, setNickname] = useState(userInfo.nickname); // 현재 닉네임
  const [nicknameMessage, setNicknameMessage] = useState("현재 닉네임입니다."); // 닉네임 사용 가능 여부를 알려주기 위한 메시지

  const [nowPassword, setNowPassword] = useState(""); // 현재 비밀번호
  const [nowPasswordMessage, setNowPasswordMessage] = useState(
    "! 현재 비밀번호를 입력해주세요 !"
  ); // 비밀번호 사용 가능 여부를 알려주기 위한 메시지
  const [newPassword, setNewPassword] = useState(""); // 새 비밀번호
  const [newPasswordCheck, setNewPasswordCheck] = useState(""); // 새 비밀번호 확인
  const [newPasswordMessage, setNewPasswordMessage] = useState(""); // 새 비밀번호 사용 가능 여부를 알려주기 위한 메시지

  const [name, setName] = useState(userInfo.name); // 현재 이름

  const initialGender = userInfo?.genderFm || null; // 초기 선택된 성별
  const [gender, setGender] = useState(initialGender); // 성별

  const [age, setAge] = useState(userInfo.age); // 나이

  const [passwordChangeCheck, setPasswordChangeCheck] = useState(false); // 비밀번호 바꿀 것인지

  const [nicknameCheck, setNicknameCheck] = useState(false); // 사용 가능한 닉네임인지 체크
  const [nowPasswordCheck, setNowPasswordCheck] = useState(false); // 현재 비밀번호 잘못 입력했는지 체크
  const [newPasswordInputCheck, setNewPasswordInputCheck] = useState(false); // 새 비밀번호 잘못 입력했는지 체크
  const [nullInputCheck, setNullInputCheck] = useState(false);
  // 필수 입력해야하는 부분 중 한 곳이라도 잘못 입력한 곳 있는지 체크

  const movePage = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    setNullInputCheck(nicknameCheck && nowPasswordCheck); // 닉네임 또는 현재 비밀번호가 잘 입력되어있는지 체크
  }, [nickname, nicknameCheck, nowPassword, nowPasswordCheck]);

  useEffect(() => {
    getNicknameMessage();
  }, []);

  useEffect(() => {
    handleNewPasswordCheck();
  }, [newPasswordInputCheck, newPassword, newPasswordCheck]);

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  const handleNicknameChange = (event) => {
    const currentNickname = event.target.value;
    setNickname(currentNickname);
    getNicknameMessage(currentNickname);
  };

  // 닉네임 중복 체크
  async function getNicknameMessage(currentNickname) {
    let data = {
      nickname: currentNickname,
    };
    try {
      const response = await axios.post("/api/member/nickname-check", data, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      setNicknameCheck(true);
      setNicknameMessage(response.data.message);
    } catch (error) {
      if (error.response.status === 400) {
        if (currentNickname === userInfo.nickname) {
          setNicknameCheck(true);
          setNicknameMessage("현재 닉네임입니다. (사용 가능합니다.)");
        } else {
          setNicknameCheck(false);
          setNicknameMessage(error.response.data.message);
        }
      } else {
        setNicknameCheck(false);
        setNicknameMessage(
          "오류로 인해 닉네임 확인이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      }
    }
  }

  function nicknameAlert() {
    Swal.fire(`${nicknameMessage}`);
  }

  // 현재 비밀번호 맞게 입력했는지 체크
  function handleNowPassword(event) {
    event.preventDefault();

    let data = {
      email: email,
      password: nowPassword,
    };

    axios
      .post("/api/login", JSON.stringify(data), {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        if (response.status === 200) {
          setNowPasswordCheck(true);
          setNowPasswordMessage("✔ 비밀번호를 올바르게 입력했습니다 ✔");
        }
      })
      .catch((error) => {
        console.log(error);
        if (error.response.status === 401) {
          setNowPasswordCheck(false);
          setNowPasswordMessage("🚫 비밀번호를 다시 확인해주세요 🚫");
        }
      });
  }

  function YesBtnCheck() {
    setPasswordChangeCheck(true);
  }

  function NoBtnCheck() {
    setPasswordChangeCheck(false);
    setNewPassword("");
    setNewPasswordCheck("");
  }

  const handleNewPasswordCheck = () => {
    // 비밀번호 정규 표현식 - 영어+숫자+특수기호 8~25자
    const passwordRegExp =
      /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    if (newPassword === "" || newPasswordCheck === "") {
      setNewPasswordMessage("! 새 비밀번호를 입력해주세요 !");
      setNewPasswordInputCheck(false);
    } else if (
      !passwordRegExp.test(newPassword) ||
      !passwordRegExp.test(newPasswordCheck)
    ) {
      setNewPasswordMessage(
        "! 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하 입력해주세요 !"
      );
      setNewPasswordInputCheck(false);
    } else if (newPassword === newPasswordCheck) {
      setNewPasswordMessage("✔ 비밀번호가 일치합니다 ✔");
      setNewPasswordInputCheck(true);
    } else {
      setNewPasswordMessage("🚫 비밀번호가 일치하지 않습니다 🚫");
      setNewPasswordInputCheck(false);
    }
  };

  // 각각의 비밀번호 입력 요소의 값을 상태에 저장
  const handleNewPasswordChange = (event) => {
    const { name, value } = event.target;
    if (name === "newPassword") {
      // event.target.name이 newPassword 경우 newPassword 값을 입력받은 것으로 바꿔줌
      setNewPassword(value);
    } else if (name === "newPasswordCheck") {
      // event.target.name이 newPasswordCheck 경우 newPasswordCheck 값을 입력받은 것으로 바꿔줌
      setNewPasswordCheck(value);
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

  // 회원 정보 수정
  async function handleUserModify(event) {
    event.preventDefault();
    let data = {
      nickname: nickname,
      genderFm: gender,
      age: age,
      name: name,
    };
    // 비밀번호 바꾸는 경우
    if (passwordChangeCheck) {
      data.password = newPassword;

      if (
        nickname === "" ||
        nowPassword === "" ||
        newPassword === "" ||
        newPasswordCheck === ""
      ) {
        return Swal.fire(
          "닉네임, 비밀번호는 필수로 입력해야 합니다.\n빈 칸을 확인해주세요."
        );
      } else if (!nullInputCheck) {
        return Swal.fire("닉네임 또는 현재 비밀번호를 다시 확인해주세요.");
      } else if (!newPasswordInputCheck) {
        return Swal.fire(
          "새 비밀번호를 다시 확인해주세요.\n비밀번호는 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하 입력해야 합니다."
        );
      }
    } else {
      // 비밀번호 바꾸지 않는 경우
      data.password = nowPassword;

      if (nickname === "" || nowPassword === "") {
        return Swal.fire(
          "닉네임, 비밀번호는 필수로 입력해야 합니다.\n빈 칸을 확인해주세요."
        );
      } else if (!nullInputCheck) {
        return Swal.fire("닉네임 또는 현재 비밀번호를 다시 확인해주세요.");
      }
    }
    const accessToken = localStorage.getItem("accessToken");

    axios
      .put("/api/member/modify", data, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        Swal.fire("회원 정보 수정이 완료되었습니다.");
        dispatch(getUserInfo());
        movePage(`/user/mypage/${email}`);
      })
      .catch((error) => {
        console.log(error);
        Swal.fire(
          "오류로 인해 회원가입이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      });
  }

  return (
    <div className="ModifyWrapper">
      <div className="ModifyBox">
        <div>
          <h2 className="UserBoxText">내 정보 수정</h2>
          <form onSubmit={handleUserModify}>
            <div className="SignBtnInputSort">
              <div
                className={`SignUpInputLine ${
                  activeIndex === 1 ? "focused" : ""
                }`}
              >
                <input
                  type="text"
                  name="nickname"
                  placeholder="닉네임"
                  value={nickname}
                  onChange={handleNicknameChange}
                  onFocus={() => handleInputFocus(1)}
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
            <div className="SignBtnInputSort">
              <div
                className={`SignUpInputLine ${
                  activeIndex === 2 ? "focused" : ""
                }`}
              >
                <input
                  type="password"
                  name="nowPassword"
                  placeholder="현재 비밀번호"
                  value={nowPassword}
                  onChange={(event) => setNowPassword(event.target.value)}
                  onFocus={() => handleInputFocus(2)}
                  onBlur={handleInputBlur}
                />
              </div>
              <Button
                className="SignUpCheckBtn"
                text={"확인"}
                onClick={handleNowPassword}
                type="button"
              />
            </div>
            <p
              className={`PwCheckMsg ${
                nowPasswordMessage === "✔ 비밀번호를 올바르게 입력했습니다 ✔"
                  ? "SamePassword"
                  : ""
              }`}
            >
              {nowPasswordMessage}
              {nowPasswordCheck && (
                <div className="AskPwChangeMessage">
                  <p>비밀번호를 변경하시겠습니까?</p>
                  <p className="PwChangeAnswer" onClick={YesBtnCheck}>
                    예
                  </p>
                  <p className="PwChangeAnswer" onClick={NoBtnCheck}>
                    아니요
                  </p>
                </div>
              )}
            </p>
            <div
              className={`SignUpInputLine ${
                activeIndex === 3 ? "focused" : ""
              }`}
            >
              <input
                type="password"
                name="newPassword"
                placeholder="새 비밀번호"
                className="SignUpInputSize"
                value={newPassword}
                onChange={handleNewPasswordChange}
                onFocus={() => handleInputFocus(3)}
                onBlur={() => {
                  handleInputBlur();
                  handleNewPasswordCheck();
                }}
                disabled={!passwordChangeCheck}
              />
            </div>
            <div
              className={`SignUpInputLine ${
                activeIndex === 4 ? "focused" : ""
              }`}
            >
              <input
                type="password"
                name="newPasswordCheck"
                placeholder="새 비밀번호 확인"
                className="SignUpInputSize"
                value={newPasswordCheck}
                onChange={handleNewPasswordChange}
                onFocus={() => handleInputFocus(4)}
                onBlur={() => {
                  handleInputBlur();
                  handleNewPasswordCheck();
                }}
                disabled={!passwordChangeCheck}
              />
            </div>
            {passwordChangeCheck && (
              <p
                className={`PwCheckMsg ${
                  newPasswordMessage === "✔ 비밀번호가 일치합니다 ✔"
                    ? "SamePassword"
                    : ""
                }`}
              >
                {newPasswordMessage}
              </p>
            )}

            <div
              className={`SignUpInputLine ${
                activeIndex === 5 ? "focused" : ""
              }`}
            >
              <input
                type="text"
                name="name"
                placeholder="이름"
                className="SignUpInputSize"
                value={name}
                onChange={(event) => setName(event.target.value)}
                onFocus={() => handleInputFocus(5)}
                onBlur={handleInputBlur}
              />
            </div>
            <div className="SignGenderAgeSort">
              <div className="SelectGender">
                <input
                  type="radio"
                  id="man"
                  name="gender"
                  value="M"
                  onChange={(event) => setGender(event.target.value)}
                  checked={gender === "M"}
                />
                <label for="man">남자</label>
                <input
                  type="radio"
                  id="woman"
                  name="gender"
                  value="F"
                  onChange={(event) => setGender(event.target.value)}
                  checked={gender === "F"}
                />
                <label for="woman">여자</label>
              </div>
              <div
                className={`SignUpInputLine ${
                  activeIndex === 6 ? "focused" : ""
                }`}
              >
                <input
                  type="number"
                  name="age"
                  placeholder="나이"
                  className="AgeSize"
                  value={age}
                  onChange={handleAgeChange}
                  onFocus={() => handleInputFocus(6)}
                  onBlur={handleInputBlur}
                />
              </div>
            </div>
            <div className="SignUpBtnSort">
              <Button
                className="SignUpBtn"
                text={"회원 정보 수정"}
                type="submit"
              />
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default MyInfoModify;

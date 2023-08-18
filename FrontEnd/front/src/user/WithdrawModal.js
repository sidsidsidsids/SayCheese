import { useEffect, useState } from "react";
// third party
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";
// local
import "./WithdrawModal.css";
import Button from "../Button";
import { logoutSuccess } from "../redux/features/login/loginSlice";

function WithdrawModal({ close }) {
  const [activeIndex, setActiveIndex] = useState(null);

  const { userInfo } = useSelector((store) => store.login);

  const [password, setPassword] = useState(""); // 비밀번호
  const [passwordRight, setPasswordRight] = useState(false); // 비밀번호 맞게 입력하면 true

  const dispatch = useDispatch();
  const movePage = useNavigate();

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

  function handlePassword(event) {
    event.preventDefault();

    let data = {
      email: userInfo.email,
      password: password,
    };

    axios
      .post("/api/login", JSON.stringify(data), {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        if (response.status === 200) {
          setPasswordRight(true);
        }
      })
      .catch((error) => {
        console.log(error);
        setPasswordRight(false);
        if (error.response.status === 401) {
          Swal.fire("비밀번호를 다시 확인해주세요.");
        }
      });
  }

  function handleWithdraw() {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .delete("/api/member/delete", {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        if (response.status === 200) {
          dispatch(logoutSuccess());
          localStorage.clear(); // 로컬스토리지 비우기
          Swal.fire("그동안 이용해주셔서 감사합니다.\n또 만나요!🙋‍♀️");
          movePage("/");
        }
      })
      .catch((error) => {
        if (error.response.status === 401) {
          Swal.fire(
            "오류로 인해 회원 탈퇴를 진행할 수 없습니다.\n다시 시도해주시길 바랍니다."
          );
        }
      });
  }

  return (
    <div>
      <div className="modalBackdrop">
        <div className="WithdrawModal">
          {passwordRight ? (
            <div>
              <h1>정말 탈퇴하시겠습니까?</h1>
              <p className="WithdrawGuide">
                탈퇴하시는 경우, 회원 정보 및 이미지, 프레임 등의 모든 정보들이
                안전하게 지워집니다.
              </p>
              <br className="stop-dragging" />
              <br className="stop-dragging" />
              <div className="WithdrawYnBtnSort">
                <Button
                  className="WithdrawYnBtn"
                  text={"예"}
                  onClick={() => {
                    document.body.style.overflow = "auto";
                    close();
                    handleWithdraw();
                  }}
                  type="button"
                />
                <Button
                  className="WithdrawYnBtn"
                  text={"아니요"}
                  onClick={() => {
                    document.body.style.overflow = "auto";
                    close();
                  }}
                  type="button"
                />
              </div>
            </div>
          ) : (
            <div>
              <h1>비밀번호를 입력해주세요</h1>
              <div className="SignBtnInputSort">
                <div
                  className={`SignUpInputLine ${
                    activeIndex === 1 ? "focused" : ""
                  } `}
                >
                  <input
                    type="password"
                    name="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
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
                  onClick={handlePassword}
                  type="button"
                />
              </div>
            </div>
          )}

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

export default WithdrawModal;

import { useEffect, useState } from "react";
import "./MyPageModal.css";
import { useDispatch, useSelector } from "react-redux";
import { closeModal } from "../redux/features/modal/modalSlice";
import MyInfoModify from "./MyInfoModify";
import { useNavigate } from "react-router-dom";

function MyPageModal() {
  const [loading] = useState(false);

  const { userInfo } = useSelector((store) => store.login);

  const { isOpen } = useSelector((store) => store.modal);
  const { modalContent } = useSelector((state) => state.modal);

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

  return (
    <div>
      <div className="modalBackdrop">
        <div className="MyPageModal">
          {loading ? (
            <div>loading..</div>
          ) : (
            <div className="MyPageSort">
              <h1 className="MyInfoTitle"> 내 정보</h1>
              <div className="MyProfileNull"></div>
              <div style={{ margin: "20px 0" }}>
                <table className="MyInfoTable">
                  <tbody>
                    <tr>
                      <th>닉네임</th>
                      <td>{userInfo.nickname}</td>
                    </tr>
                    <tr>
                      <th>이메일</th>
                      <td>{userInfo.email}</td>
                    </tr>
                    <tr>
                      <th>이름</th>
                      <td>{userInfo.name}</td>
                    </tr>
                    <tr>
                      <th>나이</th>
                      <td>{userInfo.age === 0 ? "" : userInfo.age}</td>
                    </tr>
                    <tr>
                      <th>성별</th>
                      <td>
                        {userInfo.genderFm === "F"
                          ? "여자"
                          : userInfo.genderFm === "M"
                          ? "남자"
                          : ""}
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div>
                <p
                  className="ModifyBtn"
                  onClick={() => {
                    document.body.style.overflow = "auto";
                    dispatch(closeModal());
                    movePage(`/user/modify/${userInfo.email}`);
                  }}
                >
                  📝정보 수정
                </p>
              </div>
              <button
                className="ModalClose"
                onClick={() => {
                  document.body.style.overflow = "auto";
                  dispatch(closeModal());
                }}
              >
                ×
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default MyPageModal;

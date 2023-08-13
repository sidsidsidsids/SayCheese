import { useEffect, useRef, useState } from "react";
import "./MyPageModal.css";
import { useDispatch, useSelector } from "react-redux";
import { closeModal } from "../redux/features/modal/modalSlice";
import MyInfoModify from "./MyInfoModify";
import { useNavigate } from "react-router-dom";
import Button from "../Button";
import axios from "axios";

function MyPageModal() {
  const [loading] = useState(false);

  const { userInfo } = useSelector((store) => store.login);

  const dispatch = useDispatch();
  const movePage = useNavigate();
  const imgRef = useRef();

  const [isProfileModifyModalOpen, setIsProfileModifyModalOpen] =
    useState(false);
  const [imgFile, setImgFile] = useState();

  const handleProfileModifyModalOpen = () => {
    setIsProfileModifyModalOpen(true);
  };

  // 이미지 업로드 input의 onChange
  const saveImgFile = () => {
    const file = imgRef.current.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      setImgFile(reader.result);
    };
    console.log(imgFile);
  };

  // 파일 인풋 값 초기화
  const resetInput = () => {
    imgRef.current.value = ""; // 파일 선택을 리셋
    setImgFile(false); // 이미지 파일 상태 초기화
  };

  useEffect(() => {
    // 모달 열리면 본문 스크롤 방지
    document.body.style.overflow = "hidden";
    // 현재 보고 있는 스크롤의 왼쪽 top을 가운데 정렬할 기준 top으로 해줌
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []);

  async function ProfileChange(imgFile) {
    const accessToken = localStorage.getItem("accessToken");
    let fileName = `profile_${crypto.getRandomValues(new Uint32Array(1))}.jpg`;

    axios
      .post(
        "/api/amazon/presigned",
        {
          fileName: fileName,
          fileType: "image",
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        }
      )
      .then(function (response) {
        const binaryImageData = atob(imgFile.split(",")[1]);
        const arrayBufferData = new Uint8Array(binaryImageData.length);
        for (let i = 1; i < binaryImageData.length; i++) {
          arrayBufferData[i] = binaryImageData.charCodeAt(i);
        }
        const blob = new Blob([arrayBufferData], { type: "image/jpg" });
        const imageFile = new File([blob], fileName, {
          type: "image/jpg",
        });
        // presigned URL에 파일 전송
        fetch(response.data.preSignUrl, {
          method: "PUT",
          headers: {
            "Content-Type": " image/jpg",
          },
          body: imageFile,
        }).then(function (response) {});
      });
  }

  return (
    <div>
      <div className="modalBackdrop">
        <div className="MyPageModal">
          {loading ? (
            <div>loading..</div>
          ) : (
            <div>
              {isProfileModifyModalOpen ? (
                // 프로필 사진 눌렀을 경우
                <div className="ModifyProfileModal">
                  <h1 style={{ margin: "15px 0" }}>프로필 사진 수정</h1>
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "center",
                      flexDirection: "column",
                    }}
                  >
                    <div>
                      <h2 style={{ fontWeight: "500" }}>이미지 미리보기</h2>
                      {imgFile ? (
                        <img
                          className="ProfileImgBefore"
                          src={imgFile}
                          alt="프로필 사진 미리보기"
                        />
                      ) : (
                        <div className="ProfileImgBasis"></div>
                      )}
                    </div>
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-around",
                        margin: "10px",
                      }}
                    >
                      <div>
                        <input
                          type="file"
                          id="profileImage"
                          accept="image/*"
                          ref={imgRef}
                          onChange={() => saveImgFile()}
                          className="profileInput"
                        />
                        <label for="profileImage" className="ProfileLabel">
                          <h3 style={{ margin: "0" }}>프로필 사진 추가</h3>
                        </label>
                      </div>
                      <p
                        onClick={() => {
                          resetInput();
                        }}
                        className="ProfileRemove"
                      >
                        제거
                      </p>
                    </div>
                  </div>
                  <div className="ProfileModifyBtn">
                    <Button
                      text={"닫기"}
                      onClick={() => {
                        resetInput();
                        setIsProfileModifyModalOpen(false);
                      }}
                    />
                    <Button
                      text={"수정"}
                      onClick={() => {
                        setIsProfileModifyModalOpen(false);
                      }}
                    />
                  </div>
                </div>
              ) : null}
              <div className="MyPageSort">
                <h1 className="MyInfoTitle"> 내 정보</h1>
                <div
                  style={{ cursor: "pointer" }}
                  onClick={handleProfileModifyModalOpen}
                >
                  {userInfo.profile ? (
                    <div>내 사진</div>
                  ) : (
                    <div className="MyProfileNull"></div>
                  )}
                </div>

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
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default MyPageModal;

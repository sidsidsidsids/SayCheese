import { useEffect, useRef, useState } from "react";
import "./MyPageModal.css";
import { useDispatch, useSelector } from "react-redux";
import { closeModal } from "../redux/features/modal/modalSlice";
import MyInfoModify from "./MyInfoModify";
import { useNavigate } from "react-router-dom";
import Button from "../Button";
import axios from "axios";
<<<<<<< HEAD
=======
import { getUserInfo } from "../redux/features/login/loginSlice";
import logo from "./assets/SayCheeseLogo.png";
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723

function MyPageModal({ profileChanged, setProfileChanged }) {
  const [loading] = useState(false);

  const { userInfo } = useSelector((store) => store.login);

  const dispatch = useDispatch();
  const movePage = useNavigate();
  const imgRef = useRef();

  const [isProfileModifyModalOpen, setIsProfileModifyModalOpen] =
    useState(false);
<<<<<<< HEAD
  const [imgFile, setImgFile] = useState();
=======
  const [imgFile, setImgFile] = useState(userInfo.profile);
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723

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

<<<<<<< HEAD
  async function ProfileChange(imgFile) {
    const accessToken = localStorage.getItem("accessToken");
    let fileName = `profile_${crypto.getRandomValues(new Uint32Array(1))}.jpg`;

=======
  useEffect(() => {
    dispatch(getUserInfo());
    console.log(userInfo.profile);
    setProfileChanged(false);
  }, [profileChanged]);

  async function ProfileChange(imgFile) {
    let fileInput = document.getElementById("profileImage");
    let file = fileInput.files[0];

    const accessToken = localStorage.getItem("accessToken");
    let fileName = `profile_${crypto.getRandomValues(new Uint32Array(1))}.jpg`;
    console.log("파일이름만듦 ", fileName);
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
    axios
      .post(
        "/api/amazon/presigned",
        {
          fileName: fileName,
<<<<<<< HEAD
          fileType: "image",
=======
          fileType: "profile",
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        }
      )
      .then(function (response) {
<<<<<<< HEAD
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
=======
        console.log("프리사인", response.data.preSignUrl);
        const getFileName = response.data.fileName;
        // fileName = response.data.fileName;
        // presigned URL에 파일 전송
        setTimeout(() => {
          fetch(response.data.preSignUrl, {
            method: "PUT",
            headers: {
              "Content-Type": "image/jpg",
            },
            body: file,
          }).then(function (response) {
            console.log("파일이름 받아옴 ", getFileName);
            axios
              .put(
                "/api/member/profile",
                {
                  profileName: getFileName,
                },
                {
                  headers: {
                    "Content-Type": "application/json",
                    Authorization: `${accessToken}`,
                  },
                }
              )
              .then((response) => {
                console.log("업로드할 사진", response.data.profile);
                setImgFile(response.data.profile);
                setProfileChanged(true);
                alert("프로필 사진이 수정되었습니다.");
              })
              .catch((error) => {
                console.log(error);
                alert(
                  "오류로 인해 프로필 사진 수정이 불가능합니다.\n다시 시도해주시길 바랍니다."
                );
              });
          });
        }, 1000);
      })
      .catch((error) => {
        console.log(error);
        alert(
          "오류로 인해 프로필 사진 수정이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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
<<<<<<< HEAD
                        <div className="ProfileImgBasis"></div>
=======
                        <img
                          className="ProfileImgBefore"
                          src={logo}
                          alt="프로필 사진 미리보기"
                        />
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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
<<<<<<< HEAD
                          onChange={() => saveImgFile()}
=======
                          onChange={(event) => {
                            saveImgFile();
                            console.log(event.target.value);
                          }}
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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
<<<<<<< HEAD
=======
                        ProfileChange(imgFile);
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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
<<<<<<< HEAD
                    <div>내 사진</div>
                  ) : (
                    <div className="MyProfileNull"></div>
=======
                    <div>
                      <img
                        className="MyProfileCard"
                        src={userInfo.profile}
                        alt="프로필 이미지"
                      />
                    </div>
                  ) : (
                    <div>
                      <img
                        className="MyProfileCard"
                        src={logo}
                        alt="프로필 이미지"
                      />
                    </div>
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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

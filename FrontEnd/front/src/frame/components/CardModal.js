import React, { useState, useEffect, useCallback } from "react";
// third party
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import Swal from "sweetalert2";
// local
import { closeModal } from "../../redux/features/modal/modalSlice";
import "../css/CardModal.css";
import logo from "../assets/SayCheeseLogo.png";

export default function CardModal() {
  const [loading, setLoading] = useState(false); // 모달에 표시할 내용이 없으면 에러가 나지않게 로딩 상태를 표시
  const { modalContent } = useSelector((state) => state.modal); // 모달에 띄울 컨텐츠를 가져옵니다
  const [like, setLike] = useState(modalContent.loverYn); // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const { userInfo } = useSelector((store) => store.login); // 삭제버튼 활성화 위해 userInfo 가져오기
  const [isFrameDeleteModalOpen, setIsFrameDeleteModalOpen] = useState(false); // 삭제 모달과 프레임 모달의 차이를 위해 프레임 모달이 열려있는지 변수를 선언합니다
  const [likeCount, setLikeCount] = useState(modalContent.loverCnt);
  const [isFrameAccessControlModalOpen, setIsFrameAccessControlModalOpen] =
    useState(false); // 삭제 모달과 프레임 모달의 차이를 위해 프레임 모달이 열려있는지 변수를 선언합니다

  const dispatch = useDispatch();
  const accessToken = localStorage.getItem("accessToken");

  // 좋아요 함수 로직 //
  // 모달에 프레임 상세 데이터를 axios get 방식으로 가져오는 함수
  async function getLikeData() {
    console.log("like 숫자 받아올거임");
    try {
      const response = await axios.get(
        `/api/article/frame/${modalContent.articleId}`,
        // articleId에 해당하는 이미지 데이터 상세 정보 가져오기
        {
          headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      console.log(response.data);
      setLike(response.data.loverYn); // like에 좋아요 여부 저장 card 에서는 내가 표시했는지만 알면 됩니다
      setLikeCount(response.data.loverCnt); // Like 개수를 저장합니다
    } catch (error) {
      console.log(error);
    }
  }

  // 좋아요
  async function handleLikePlus() {
    console.log(modalContent.articleId);
    axios
      .post(`/api/article/lover/frame/${modalContent.articleId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        setLike(true);
        getLikeData(); // 좋아요 데이터가 바뀌도록 서버에서 다시 받아온다
      })
      .catch((error) => {
        console.log(error);
      });
  }

  // 좋아요 취소
  async function handleLikeMinus() {
    axios
      .delete(`/api/article/lover/${modalContent.articleId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        setLike(false);
        getLikeData(); // 좋아요 데이터가 바뀌도록 서버에서 다시 받아온다
      })
      .catch((error) => {
        console.log(error);
      });
  }

  // 좋아요
  function clickLike(event) {
    event.stopPropagation();
    // 예외처리 : 로그인 안 한 사람은 안되게 해야함
    if (accessToken) {
      // 로그인 한 사람만
      if (like) {
        console.log("좋아요 취소 요청");
        handleLikeMinus();
      } else if (!like) {
        console.log("좋아요 요청");
        handleLikePlus();
      }
    } else {
      Swal.fire("로그인 해주세요");
    }
  }
  // 프레임 삭제 API
  async function handleFrameDelete() {
    if (accessToken) {
      axios
        .delete(`/api/article/frame/${modalContent.articleId}`, {
          data: { frameArticleId: modalContent.articleId },
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        })
        .then((response) => {
          Swal.fire("게시글이 정상적으로 삭제되었습니다.");
          window.location.reload();
        })
        .catch((error) => {
          Swal.fire(
            "오류로 인해 게시글을 삭제할 수 없습니다.\n다시 시도해주시길 바랍니다."
          );
        });
    }
  }
  // 공개 여부 수정 API
  function handleFrameAccessControl() {
    axios
      .put(`/api/article/frame/${modalContent.articleId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        Swal.fire(response.message);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  useEffect(() => {
    if (true) {
      document.body.style.overflow = "hidden";
      const modalbg = document.getElementsByClassName("modalBackdrop")[0];
      const currentTop = window.scrollY + "px";
      modalbg.style.top = currentTop;
    }
  });
  return (
    <>
      <div className="modalBackdrop">
        <div className="modal">
          {loading ? (
            <div>loading..</div>
          ) : (
            <>
              {isFrameAccessControlModalOpen ? ( // 전체공개 여부를 수정하고 싶을때
                // 공개 여부 수정 모달 컨텐츠
                <div className="DeleteMyPhotoModal">
                  <div
                    style={{
                      marginTop: "50px",
                      marginLeft: "50px",
                      marginBottom: "20px",
                      textAlign: "left",
                    }}
                  >
                    <h1 style={{ margin: "0", fontSize: "32px" }}>비공개로</h1>
                    <h1 style={{ margin: "0", fontSize: "32px" }}>
                      수정하시겠습니까?
                    </h1>
                  </div>
                  <div className="DeleteGuide">
                    <p style={{ fontSize: "18px" }}>
                      비공개된 프레임의 공개 여부 수정은
                    </p>
                    <p style={{ fontSize: "18px" }}>
                      마이페이지에서 변경 가능합니다.
                    </p>
                  </div>

                  <div className="DeleteBtnSort">
                    <button
                      className="whtbtn"
                      onClick={() => {
                        setIsFrameAccessControlModalOpen(false);
                      }}
                    >
                      이전으로
                    </button>
                    <button
                      className="btn"
                      onClick={() => {
                        document.body.style.overflow = "auto";
                        dispatch(closeModal());
                        handleFrameAccessControl();
                      }}
                    >
                      확인
                    </button>
                  </div>
                </div>
              ) : isFrameDeleteModalOpen ? ( // 삭제 버튼 눌렀을때
                // 경우 삭제 모달 컨텐츠
                <div className="DeleteMyPhotoModal">
                  <div className="DeleteQuestion">
                    <h1 style={{ fontSize: "32px" }}>정말 삭제하시겠습니까?</h1>
                  </div>
                  <p className="DeleteGuide" style={{ fontSize: "18px" }}>
                    삭제하시는 경우, 복구는 불가능합니다.
                  </p>
                  <br className="stop-dragging" />
                  <br className="stop-dragging" />
                  <div className="DeleteBtnSort">
                    <button
                      className="whtbtn"
                      onClick={() => {
                        setIsFrameDeleteModalOpen(false);
                      }}
                    >
                      이전으로
                    </button>
                    <button
                      className="btn"
                      onClick={() => {
                        document.body.style.overflow = "auto";
                        dispatch(closeModal());
                        handleFrameDelete();
                      }}
                    >
                      확인
                    </button>
                  </div>
                </div>
              ) : null}
              {/* 프레임 상세보기 모달 컨텐츠 */}
              <h2 style={{ fontWeight: "500", margin: "20px 0" }}>
                {modalContent.subject}
              </h2>
              <img
                src={modalContent.frameLink}
                alt="프레임 이미지"
                width="300px"
                height="300px"
                style={{ objectFit: "contain" }}
              />
              <hr className="ModalLine" />
              <div className="ModalContent">
                <div
                  style={{ display: "flex", justifyContent: "space-between" }}
                >
                  <div style={{ display: "flex", justifyContent: "center" }}>
                    <div>{modalContent.author}</div>
                  </div>

                  <div>
                    {new Date(modalContent.createdDate).toLocaleString(
                      "ko-KR",
                      {
                        year: "numeric",
                        month: "2-digit",
                        day: "2-digit",
                        hour: "2-digit",
                        minute: "2-digit",
                        second: "2-digit",
                      }
                    )}
                  </div>
                </div>
                <div
                  className="heart-btn"
                  onClick={(event) => clickLike(event)}
                >
                  <div className="heart-content">
                    <span
                      className={
                        like === 1
                          ? "heart full"
                          : like === true
                          ? "heart-active heart"
                          : "heart"
                      }
                    ></span>
                    <span className="numb">{likeCount}</span>
                  </div>
                </div>
                {modalContent.isMine ? ( // 자신이 작성한 프레임 글이라면 삭제버튼과 공개여부 수정 버튼이 보입니다
                  <div className="alignFrameTwoButtons">
                    <button
                      className="btn"
                      style={{ width: "100px" }}
                      onClick={() => {
                        setIsFrameDeleteModalOpen(true);
                      }}
                    >
                      삭제하기
                    </button>
                    <button
                      className="btn"
                      style={{ width: "150px" }}
                      onClick={() => {
                        setIsFrameAccessControlModalOpen(true);
                      }}
                    >
                      공개 수정하기
                    </button>
                  </div>
                ) : null}
              </div>

              <button
                className="modalClose"
                onClick={() => {
                  // 삭제, 수정 모달 닫으면 본문 스크롤 허용
                  document.body.style.overflow = "auto";
                  dispatch(closeModal());
                }}
              >
                X
              </button>
            </>
          )}
        </div>
      </div>
    </>
  );
}

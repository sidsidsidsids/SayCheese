import React, { useState, useEffect, useCallback } from "react";
// third party
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
// local
import { closeModal } from "../../redux/features/modal/modalSlice";
import "../css/CardModal.css";

export default function CardModal() {
  const [loading, setLoading] = useState(false); // 모달에 표시할 내용이 없으면 에러가 나지않게 로딩 상태를 표시
  const { isOpen } = useSelector((store) => store.modal); // state는 젠처 리덕스 스토어를 의미하며, modal 리듀서에서 관리되는 상태 객체 modalContent를 추출합니다.
  const { modalContent } = useSelector((state) => state.modal); // 모달에 띄울 컨텐츠를 가져옵니다
  const [like, setLike] = useState(modalContent.loverYn); // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const { userInfo } = useSelector((store) => store.login); // 삭제버튼 활성화 위해 userInfo 가져오기
  const [isFrameDeleteModalOpen, setIsFrameDeleteModalOpen] = useState(false); // 삭제 모달과 프레임 모달의 차이를 위해 프레임 모달이 열려있는지 변수를 선언합니다
  const [isFrameAccessControlModalOpen, setIsFrameAccessControlModalOpen] =
    useState(false); // 삭제 모달과 프레임 모달의 차이를 위해 프레임 모달이 열려있는지 변수를 선언합니다

  const dispatch = useDispatch();
  const accessToken = localStorage.getItem("accessToken");

  // 좋아요 함수 로직 //
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
      })
      .catch((error) => {
        console.log(error);
      });
  }
  // 좋아요
  function clickLike(event) {
    event.stopPropagation();
    if (like) {
      handleLikeMinus();
    } else if (!like) {
      handleLikePlus();
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
          alert("게시글이 정상적으로 삭제되었습니다.");
          window.location.reload();
        })
        .catch((error) => {
          alert(
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
        alert(response.message);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  useEffect(() => {
    document.body.style.overflow = "hidden"; // 모달 열리면 본문 스크롤 방지해야 합니다
    // 현재 보고 있는 스크롤의 왼쪽 top을 가운데 정렬할 기준 top으로 해줌
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []); // Empty dependency array to run the effect only once when the component mounts

  return (
    <>
      <div className="modalBackdrop">
        <div className="modal">
          {loading ? (
            <div>loading..</div>
          ) : (
            <>
              {isFrameAccessControlModalOpen ? ( // 전체공개 여부를 수정하고 싶을때
                <div className="DeletePhotoModal">
                  <div className="DeleteQuestion">
                    <h1>공개 여부를 수정 하시겠습니까?</h1>
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
              ) : isFrameDeleteModalOpen ? (
                // 삭제 버튼 눌렀을 경우
                <div className="DeletePhotoModal">
                  <div className="DeleteQuestion">
                    <h1>삭제를 하시겠습니까?</h1>
                  </div>

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
              <div className="modal-name">{modalContent.name}</div>
              <div className="modal-author">작성자 : {modalContent.author}</div>
              <img src={modalContent.frameLink} alt="프레임 이미지" />

              <div className="heart-btn" onClick={(event) => clickLike(event)}>
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
                  <span className="numb">{modalContent.loverCnt}</span>
                </div>
              </div>
              <div className="frameTags">
                {modalContent.isPublic === true ? "전체 공개" : "비공개"}
              </div>
              {modalContent.isMine ? (
                <div className="alignTwoButtons">
                  <button
                    className="btn"
                    onClick={() => {
                      setIsFrameDeleteModalOpen(true);
                    }}
                  >
                    삭제하기
                  </button>
                  <button
                    className="btn"
                    onClick={() => {
                      // dispatch(closeModal());
                      setIsFrameAccessControlModalOpen(true);
                    }}
                  >
                    공개 수정하기
                  </button>
                </div>
              ) : null}
              <button
                className="modalClose"
                onClick={() => {
                  // 모달 닫으면 본문 스크롤 허용
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

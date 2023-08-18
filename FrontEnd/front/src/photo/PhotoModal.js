import { useEffect, useState } from "react";
// third party
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
// local
import "./PhotoModal.css";
import { closeModal } from "../redux/features/modal/modalSlice";
import Button from "../Button";
import logo from "../frame/assets/SayCheeseLogo.png";

function PhotoModal() {
  // 모달에 표시할 내용이 없으면 에러가 나지않게 로딩 상태를 표시
  const [loading] = useState(false);

  // 모달에 띄울 컨텐츠를 가져옵니다
  // state는 전체 리덕스 스토어를 의미하며, modal 리듀서에서 관리되는 상태 객체 modalContent를 추출합니다.
  const { isOpen } = useSelector((store) => store.modal);
  const { modalContent } = useSelector((state) => state.modal);
  const { userInfo } = useSelector((store) => store.login);

  const [imageData, setImageData] = useState([]);
  // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const [like, setLike] = useState(imageData.loverYn);
  const [authorEmail, setAuthorEmail] = useState(""); // 작성자 이메일
  const [isPhotoDelModalOpen, setIsPhotoDelModalOpen] = useState(false);
  const [deleteType, setDeleteType] = useState(""); // 삭제 타입

  const dispatch = useDispatch();

  const handleDeleteModalOpen = () => {
    setIsPhotoDelModalOpen(true);
  };

  function clickLike(event) {
    event.stopPropagation();

    // 좋아요 눌러져 있는 상태에서 좋아요 누를 경우 - 좋아요 취소
    if (like) {
      handleLikeMinus();
    } // 좋아요 눌러져 있지 않은 상태에서 좋아요 누를 경우 - 좋아요 추가
    else if (like === 0) {
      handleLikePlus();
    }
  }

  useEffect(() => {
    // 모달 열리면 본문 스크롤 방지
    document.body.style.overflow = "hidden";
    // 현재 보고 있는 스크롤의 왼쪽 top을 가운데 정렬할 기준 top으로 해줌
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []);

  useEffect(() => {
    getImageData();
  }, [like]); // 좋아요 체크 여부 변하면 axios get 다시 한다

  // 모달에 해당 이미지 데이터를 axios get 방식으로 가져오는 함수
  async function getImageData() {
    try {
      const response = await axios.get(
        `/api/article/image/${modalContent.articleId}`,
        // articleId에 해당하는 이미지 데이터 상세 정보 가져오기
        {
          headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      setImageData(response.data); // imageData에 이미지 정보 저장
      setLike(response.data.loverYn); // like에 좋아요 여부 저장
      setAuthorEmail(response.data.email); // authorEmail에 이미지 작성자 이메일 저장
    } catch (error) {
      console.log(error);
    }
  }

  // 좋아요 추가
  async function handleLikePlus() {
    const accessToken = localStorage.getItem("accessToken");

    axios
      .post(`/api/article/lover/image/${modalContent.articleId}`, {
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
    const accessToken = localStorage.getItem("accessToken");

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

  // 이미지 삭제
  async function handlePhotoDelete() {
    const accessToken = localStorage.getItem("accessToken");
    // 게시글만 삭제하기
    if (deleteType === "onlyArticle") {
      axios
        .delete("/api/article/image", {
          data: { articleId: modalContent.articleId },
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        })
        .then((response) => {
          Swal.fire("게시글이 정상적으로 삭제되었습니다.");
          window.location.reload(); // 해당 페이지로 리다이렉트
        })
        .catch((error) => {
          Swal.fire(
            "오류로 인해 게시글을 삭제할 수 없습니다.\n다시 시도해주시길 바랍니다."
          );
        });
      // 이미지도 같이 삭제하기
    } else if (deleteType === "withPhoto") {
      axios
        .delete("/api/article/image/all", {
          data: { articleId: modalContent.articleId },
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        })
        .then((response) => {
          Swal.fire("게시글과 네컷사진이 정상적으로 삭제되었습니다.");
          window.location.reload(); // 해당 페이지로 리다이렉트
        })
        .catch((error) => {
          Swal.fire(
            "오류로 인해 삭제를 진행할 수 없습니다.\n다시 시도해주시길 바랍니다."
          );
        });
      // 삭제방식을 선택하지 않았을 경우
    } else {
      return Swal.fire("원하는 삭제 방식을 선택 후 진행해주시길 바랍니다.");
    }
  }

  return (
    <div>
      <div className="modalBackdrop">
        <div className="modal">
          {loading ? (
            <div>loading..</div>
          ) : (
            <div>
              {isPhotoDelModalOpen ? (
                // 삭제 버튼 눌렀을 경우
                <div className="DeletePhotoModal">
                  <div className="DeleteQuestion">
                    <h1>어떤 삭제를 하시겠습니까?</h1>
                  </div>
                  <div className="SelectDelete">
                    <input
                      type="radio"
                      id="onlyArticle"
                      name="deleteType"
                      value="onlyArticle"
                      onChange={(event) => setDeleteType(event.target.value)}
                    />
                    <label for="onlyArticle">게시글만</label>
                    <input
                      type="radio"
                      id="withPhoto"
                      name="deleteType"
                      value="withPhoto"
                      onChange={(event) => setDeleteType(event.target.value)}
                    />
                    <label for="withPhoto">네컷사진도</label>
                  </div>
                  <div className="DeleteBtnSort">
                    <Button
                      text={"닫기"}
                      onClick={() => {
                        setIsPhotoDelModalOpen(false);
                      }}
                    />
                    <Button
                      text={"확인"}
                      onClick={() => {
                        document.body.style.overflow = "auto";
                        dispatch(closeModal());
                        handlePhotoDelete();
                      }}
                    />
                  </div>
                </div>
              ) : null}
              <div>
                <img
                  src={imageData.imgLink}
                  alt="네컷 이미지"
                  className="ModalImg"
                />
                <hr className="ModalLine" />
                <div className="ModalContent">
                  <div className="ModalSort">
                    <div style={{ display: "flex", justifyContent: "center" }}>
                      <div>{imageData.author}</div>
                    </div>

                    <div>
                      {new Date(imageData.createdDate).toLocaleString("ko-KR", {
                        year: "numeric",
                        month: "2-digit",
                        day: "2-digit",
                        hour: "2-digit",
                        minute: "2-digit",
                        second: "2-digit",
                      })}
                      {/* 날짜 형식 수정 - YYYY. MM. DD. 오후 hh:mm:ss 형식으로 */}
                    </div>
                  </div>
                  <div onClick={clickLike} className="HeartBtn">
                    <div className="Heartcontent">
                      <span
                        className={
                          like === 1
                            ? "Heart Full"
                            : like === true
                            ? "HeartActive Heart"
                            : "Heart"
                        }
                      ></span>
                      <span className="PhotoLikeNum">{imageData.loverCnt}</span>
                    </div>
                  </div>
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                    }}
                  >
                    <div className="PhotoTagsSort">
                      {imageData &&
                        imageData.tags &&
                        imageData.tags.length !== 0 &&
                        imageData.tags.map((tag, index) => (
                          <div key={index} className="PhotoTags">
                            # {tag}
                          </div>
                        ))}
                      {/* 이미지 데이터에 tags가 존재할 경우 tags 보여주는 코드 */}
                    </div>
                    {imageData.isMine === 1 ? (
                      <Button
                        className="DeleteBtn"
                        text={"삭제"}
                        onClick={handleDeleteModalOpen}
                        type="button"
                      />
                    ) : (
                      <div></div>
                    )}
                  </div>
                </div>

                <button
                  className="ModalClose"
                  onClick={() => {
                    // 모달 닫으면 본문 스크롤 허용
                    document.body.style.overflow = "auto";
                    dispatch(closeModal());
                  }}
                >
                  X
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default PhotoModal;

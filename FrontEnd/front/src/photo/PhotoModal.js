import { useEffect, useState } from "react";
import "./PhotoModal.css";
import { useDispatch, useSelector } from "react-redux";
import { closeModal } from "../redux/features/modal/modalSlice";
import axios from "axios";

function PhotoModal() {
  // 모달에 표시할 내용이 없으면 에러가 나지않게 로딩 상태를 표시
  const [loading] = useState(false);

  // 모달에 띄울 컨텐츠를 가져옵니다
  // state는 전체 리덕스 스토어를 의미하며, modal 리듀서에서 관리되는 상태 객체 modalContent를 추출합니다.
  const { isOpen } = useSelector((store) => store.modal);
  const { modalContent } = useSelector((state) => state.modal);
  const [imageData, setImageData] = useState([]);

  const dispatch = useDispatch();
  // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const [like, setLike] = useState(modalContent.loverYn);

  function clickLike(event) {
    event.stopPropagation();
    setLike(!like);
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
  }, []);

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
      setImageData(response.data);
    } catch (error) {
      console.log(error);
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
              <img
                src={imageData.imgLink}
                alt="네컷 이미지"
                className="ModalImg"
              />
              <hr className="ModalLine" />
              <div className="ModalContent">
                <div className="ModalSort">
                  <div>{imageData.author}</div>
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
                <div onClick={clickLike} className="PhotoLike">
                  {imageData.loverCnt}
                </div>
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
          )}
        </div>
      </div>
    </div>
  );
}

export default PhotoModal;

import { useEffect, useState } from "react";
import "./PhotoModal.css";
import { useDispatch, useSelector } from "react-redux";
import { closeModal } from "../redux/features/modal/modalSlice";

function PhotoModal() {
  // 모달에 표시할 내용이 없으면 에러가 나지않게 로딩 상태를 표시
  const [loading] = useState(false);

  // 모달에 띄울 컨텐츠를 가져옵니다
  // state는 젠처 리덕스 스토어를 의미하며, modal 리듀서에서 관리되는 상태 객체 modalContent를 추출합니다.
  const { isOpen } = useSelector((store) => store.modal);
  const { modalContent } = useSelector((state) => state.modal);

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

  return (
    <div>
      <div className="modalBackdrop">
        <div className="modal">
          {loading ? (
            <div>loading..</div>
          ) : (
            <div>
              <img
                src={modalContent.imageLink.sampleImg}
                className="ModalImg"
              />
              <hr className="ModalLine" />
              <div className="ModalContent">
                <div className="ModalSort">
                  <div>{modalContent.author}</div>
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
                <div onClick={clickLike}>{modalContent.loverCnt}</div>
                <div className="PhotoTagsSort">
                  {modalContent.tags.map((tag, index) => (
                    <div key={index} className="PhotoTags">
                      # {tag}
                    </div>
                  ))}
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

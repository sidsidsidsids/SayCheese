import { useEffect } from "react";
import "./DeletePhotoModal.css";

function DeletePhotoModal({ close }) {
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
        <div className="DeletePhotoModal">
          <div className="DeleteQuestion">
            <h1>어떤 삭제를 하시겠습니까?</h1>
          </div>
          <div className="SelectDelete">
            <input
              type="radio"
              id="onlyArticle"
              name="deleteType"
              value="article"
            />
            <label for="onlyArticle">게시글만</label>
            <input
              type="radio"
              id="withPhoto"
              name="deleteType"
              value="plusPhoto"
            />
            <label for="withPhoto">네컷사진도</label>
          </div>
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

export default DeletePhotoModal;

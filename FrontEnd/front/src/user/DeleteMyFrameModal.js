import { useEffect } from "react";
// third party
import axios from "axios";
import Swal from "sweetalert2";
// local
import Button from "../Button";

function DeleteMyFrameModal({
  myFrameChange,
  setMyFrameChange,
  articleId,
  close,
}) {
  useEffect(() => {
    // 모달 열리면 본문 스크롤 방지
    document.body.style.overflow = "hidden";
    // 현재 보고 있는 스크롤의 왼쪽 top을 가운데 정렬할 기준 top으로 해줌
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []);

  // 프레임 삭제 axios
  function handleDeleteMyFrame() {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .delete(`/api/article/frame/${articleId}`, {
        data: { frameArticleId: articleId },
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        Swal.fire("프레임이 정상적으로 삭제되었습니다.");
      })
      .catch((error) => {
        Swal.fire(
          "오류로 인해 프레임을 삭제할 수 없습니다.\n다시 시도해주시길 바랍니다."
        );
      });
  }

  return (
    <div>
      <div className="modalBackdrop">
        <div className="DeleteMyPhotoModal">
          <div className="DeleteQuestion">
            <h1>정말 삭제하시겠습니까?</h1>
          </div>
          <p className="DeleteGuide">삭제하시는 경우, 복구는 불가능합니다.</p>
          <br className="stop-dragging" />
          <br className="stop-dragging" />
          <div className="WithdrawYnBtnSort">
            <Button
              className="WithdrawYnBtn"
              text={"예"}
              onClick={() => {
                document.body.style.overflow = "auto";
                close();
                handleDeleteMyFrame();
                setMyFrameChange(true);
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

export default DeleteMyFrameModal;

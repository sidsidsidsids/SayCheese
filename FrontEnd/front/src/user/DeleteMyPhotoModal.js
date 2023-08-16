import { useEffect } from "react";
<<<<<<< HEAD
import "./DeleteMyPhotoModal.css";
import Button from "../Button";
import axios from "axios";
=======
// third party
import axios from "axios";
// local
import "./DeleteMyPhotoModal.css";
import Button from "../Button";
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723

function DeleteMyPhotoModal({
  close,
  imageId,
  myPhotoChange,
  setMyPhotochange,
}) {
  useEffect(() => {
    // 모달 열리면 본문 스크롤 방지
    document.body.style.overflow = "hidden";
    // 현재 보고 있는 스크롤의 왼쪽 top을 가운데 정렬할 기준 top으로 해줌
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []);

<<<<<<< HEAD
=======
  // 이미지 삭제 axios
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
  function handleDeleteMyPhoto() {
    console.log(imageId);
    const accessToken = localStorage.getItem("accessToken");
    axios
      .delete("/api/image", {
        data: { imageId: imageId },
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        alert("이미지가 정상적으로 삭제되었습니다.");
      })
      .catch((error) => {
        alert(
          "오류로 인해 이미지를 삭제할 수 없습니다.\n다시 시도해주시길 바랍니다."
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
                handleDeleteMyPhoto();
                setMyPhotochange(true);
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

export default DeleteMyPhotoModal;

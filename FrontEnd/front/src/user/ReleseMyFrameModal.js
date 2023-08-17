import { useEffect } from "react";
// third party
import axios from "axios";
import Swal from "sweetalert2";
// local
import Button from "../Button";

function ReleseMyFrameModal({ articleId, isPublic, close }) {
  useEffect(() => {
    // 모달 열리면 본문 스크롤 방지
    document.body.style.overflow = "hidden";
    // 현재 보고 있는 스크롤의 왼쪽 top을 가운데 정렬할 기준 top으로 해줌
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []);

  // 프레임 게시판 공개 여부 수정을 axios put 방식으로 하는 함수
  function handleReleseMyFrame() {
    const accessToken = localStorage.getItem("accessToken");

    axios
      .put(`/api/article/frame/${articleId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        Swal.fire(response.message);
        window.location.reload();
      })
      .catch((error) => {
        console.log(error);
        Swal.fire(
          "오류로 인해 공개 여부를 수정할 수 없습니다.\n다시 시도해주시길 바랍니다."
        );
      });
  }

  return (
    <div>
      <div className="modalBackdrop">
        <div className="DeleteMyPhotoModal">
          <div
            style={{
              marginTop: "50px",
              marginLeft: "50px",
              marginBottom: "20px",
              textAlign: "left",
            }}
          >
            <h1 style={{ margin: "0" }}>공개 여부를</h1>
            <h1 style={{ margin: "0" }}>수정하시겠습니까?</h1>
          </div>
          {isPublic ? (
            <p className="DeleteGuide">현재 프레임이 공개된 상태입니다.</p>
          ) : (
            <p className="DeleteGuide">현재 프레임이 비공개된 상태입니다.</p>
          )}
          <br className="stop-dragging" />
          <br className="stop-dragging" />
          <div className="WithdrawYnBtnSort">
            <Button
              className="WithdrawYnBtn"
              text={"예"}
              onClick={() => {
                document.body.style.overflow = "auto";
                close();
                handleReleseMyFrame();
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

export default ReleseMyFrameModal;

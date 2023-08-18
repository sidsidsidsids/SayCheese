import { useEffect } from "react";
// local
import "./MyPhotoHover.css";

function MyPhotoHover({
  imageId,
  imageLink,
  createdDate,
  loverCnt,
  loverYn,
  articleYn,
  props,
}) {
  useEffect(() => {
    document.body.style.overflow = "hidden";
    const modalbg = document.getElementsByClassName("modalBackdrop")[0]; // Get the first element with the class name
    const currentTop = window.scrollY + "px";
    modalbg.style.top = currentTop; // Set the top CSS property of the element
  }, []);

  return (
    <div>
      <div className="modalBackdrop">
        <div className="MyPhotoHover" onMouseLeave={props}>
          <img
            src={imageLink}
            width="450px"
            alt="네컷 이미지"
            className="ModalImg"
          />
          <hr className="ModalLine" />
          <div className="ModalContent">
            <div className="ModalSort">
              <div className="PhotoDate">
                {new Date(createdDate).toLocaleString("ko-KR", {
                  year: "numeric",
                  month: "2-digit",
                  day: "2-digit",
                  hour: "2-digit",
                  minute: "2-digit",
                  second: "2-digit",
                })}
              </div>
              {articleYn === "N" ? (
                <div></div>
              ) : (
                <div className="Heartcontent">
                  <span
                    className={loverYn === 0 ? "MyHeart" : "MyHeart Full"}
                  ></span>
                  <span className="PhotoLikeNum">{loverCnt}</span>
                </div>
              )}
            </div>
          </div>
          <div className="MyPohtoUploadInfo">
            {articleYn === "N" ? (
              <p>네컷사진 게시판에 업로드 되지 않은 이미지입니다.</p>
            ) : null}
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyPhotoHover;

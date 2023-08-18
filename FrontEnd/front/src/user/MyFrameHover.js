import { useEffect, useState } from "react";
// local
import "./MyFrameHover.css";

function MyFrameHover({
  props,
  articleId,
  subject,
  frameLink,
  loverCnt,
  createdDate,
  author,
  isPublic,
  frameSpecification,
  loverYn,
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
          <h1>{subject}</h1>
          <img
            src={frameLink}
            width="380px"
            height="380px"
            style={{ objectFit: "contain" }}
            alt="프레임 이미지"
            className="ModalImg"
          />
          <div className="ModalContent">
            <div className="FrameHoverSort">
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
              <div className="Heartcontent">
                {loverYn === 1 ? (
                  <span className="Heart Full"></span>
                ) : (
                  <span className="Heart"></span>
                )}
                <span className="PhotoLikeNum">{loverCnt}</span>
              </div>
            </div>
            <div className="IsPublicTitle">
              {isPublic ? (
                <p>현재 프레임 게시판에 공개된 상태입니다.</p>
              ) : (
                <p>현재 프레임 게시판에 공개되지 않은 상태입니다.</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyFrameHover;

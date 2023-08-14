import { useState } from "react";
import MyFrameHover from "./MyFrameHover";

function MyFrameCard({
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
  const [isHovered, setIsHovered] = useState(false);

  const handleMouseHover = (event) => {
    event.stopPropagation();
    setIsHovered(true);
  };

  const makeFalse = () => {
    setIsHovered(false);
    document.body.style.overflow = "auto";
  };

  return (
    <div className="MyPhotoCard">
      <div>
        <div style={{ alignItems: "center" }}>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <p className="MyPhotoDeleteBtn" style={{ margin: "20px" }}>
              삭제
            </p>
            <p className="MyPhotoDeleteBtn" style={{ margin: "20px" }}>
              게시
            </p>
            <div className="HeartBtn">
              <div className="Heartcontent">
                <span
                  className={loverYn === 0 ? "MyHeart" : "MyHeart Full"}
                ></span>
              </div>
            </div>
          </div>
          <img
            width="150px"
            height="130px"
            src={frameLink}
            alt="프레임 이미지"
            onMouseEnter={handleMouseHover}
          />
          <h3>{subject}</h3>
          {isHovered && (
            <div>
              <MyFrameHover
                props={makeFalse}
                articleId={articleId}
                subject={subject}
                frameLink={frameLink}
                loverCnt={loverCnt}
                createdDate={createdDate}
                author={author}
                isPublic={isPublic}
                frameSpecification={frameSpecification}
                loverYn={loverYn}
              />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default MyFrameCard;

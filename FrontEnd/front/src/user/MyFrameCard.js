import { useState } from "react";
import MyFrameHover from "./MyFrameHover";
import DeleteMyFrameModal from "./DeleteMyFrameModal";
import ReleseMyFrameModal from "./ReleseMyFrameModal";

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
  myFrameChange,
  setMyFrameChange,
}) {
  const [isHovered, setIsHovered] = useState(false);

  const [isFrameDelModalOpen, setIsFrameDelModalOpen] = useState(false);
  const [isFrameRelModalOpen, setIsFrameRelModalOpen] = useState(false);

  const handleMouseHover = (event) => {
    event.stopPropagation();
    setIsHovered(true);
  };

  const makeFalse = () => {
    setIsHovered(false);
    document.body.style.overflow = "auto";
  };

  const handleDeleteModalOpen = () => {
    setIsFrameDelModalOpen(true);
  };

  const handleReleseModalOpen = () => {
    setIsFrameRelModalOpen(true);
  };

  return (
    <div className="MyPhotoCard">
      <div>
        <div style={{ alignItems: "center" }}>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <p
              className="MyPhotoDeleteBtn"
              style={{ margin: "20px" }}
              onClick={handleDeleteModalOpen}
            >
              삭제
            </p>
            <p
              className="MyPhotoDeleteBtn"
              style={{ margin: "20px" }}
              onClick={handleReleseModalOpen}
            >
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
            height="150px"
            style={{ objectFit: "contain" }}
            src={frameLink}
            alt="프레임 이미지"
            onMouseEnter={handleMouseHover}
          />
          <h3>{subject}</h3>
          {isFrameDelModalOpen && (
            <DeleteMyFrameModal
              myFrameChange={myFrameChange}
              setMyFrameChange={setMyFrameChange}
              articleId={articleId}
              close={() => setIsFrameDelModalOpen(false)}
            />
          )}
          {isFrameRelModalOpen && (
            <ReleseMyFrameModal
              articleId={articleId}
              isPublic={isPublic}
              close={() => setIsFrameRelModalOpen(false)}
            />
          )}
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

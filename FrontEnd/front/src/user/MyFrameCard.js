import { useState } from "react";
// local
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
  const [isHovered, setIsHovered] = useState(false); // 프레임 카드에 호버한 상태인지 보기 위한 변수입니다

  const [isFrameDelModalOpen, setIsFrameDelModalOpen] = useState(false); // 프레임 삭제 모달이 열려있는지 변수를 선언합니다
  const [isFrameRelModalOpen, setIsFrameRelModalOpen] = useState(false); // 프레임 공개 수정 모달이 열려있는지 변수를 선언합니다

  // 마우스가 프레임 카드에 올라와 있을 경우 - 올라와 있는 상태임을 나타내는 함수
  const handleMouseHover = (event) => {
    event.stopPropagation();
    setIsHovered(true);
  };

  // 마우스가 프레임 카드에 올라와 있지 않은 경우 - 마우스가 프레임 카드에서 벗어난 상태임을 나타내는 함수
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

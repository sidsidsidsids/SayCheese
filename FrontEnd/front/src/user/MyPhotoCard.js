import { useState } from "react";
// third party
import { useDispatch } from "react-redux";
// local
import "./MyPhotoCard.css";
import MyPhotoHover from "./MyPhotoHover";
import DeleteMyPhotoModal from "./DeleteMyPhotoModal";

function MyPhotoCard({
  imageId,
  author,
  createdDate,
  imageLink,
  loverCnt,
  loverYn,
  tags,
  articleYn,
  myPhotoChange,
  setMyPhotochange,
  payload,
}) {
  const [isHovered, setIsHovered] = useState(false); // 이미지 카드에 호버한 상태인지 보기 위한 변수입니다
  const [isPhotoDelModalOpen, setIsPhotoDelModalOpen] = useState(false); // 이미지 삭제 모달이 열려있는지 변수를 선언합니다

  // 마우스가 이미지 카드에 올라와 있을 경우 - 올라와 있는 상태임을 나타내는 함수
  const handleMouseHover = (event) => {
    event.stopPropagation();
    setIsHovered(true);
  };

  // 마우스가 이미지 카드에 올라와 있지 않은 경우 - 마우스가 이미지 카드에서 벗어난 상태임을 나타내는 함수
  const makeFalse = () => {
    setIsHovered(false);
    document.body.style.overflow = "auto";
  };

  const handleDeleteModalOpen = () => {
    setIsPhotoDelModalOpen(true);
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
            <div className="HeartBtn">
              <div className="Heartcontent">
                {articleYn === "N" ? (
                  <div style={{ width: "60px", height: "60px" }}></div>
                ) : (
                  <span
                    className={loverYn === 0 ? "MyHeart" : "MyHeart Full"}
                  ></span>
                )}
              </div>
            </div>
          </div>
          <img
            className="MyPhotoCardImg"
            src={imageLink}
            alt="네컷 이미지"
            onMouseEnter={handleMouseHover}
          />

          {isPhotoDelModalOpen && (
            <DeleteMyPhotoModal
              setMyPhotochange={setMyPhotochange}
              myPhotoChange={myPhotoChange}
              imageId={imageId}
              close={() => setIsPhotoDelModalOpen(false)}
            />
          )}
          {isHovered && (
            <div>
              <MyPhotoHover
                props={makeFalse}
                imageLink={imageLink}
                imageId={imageId}
                createdDate={createdDate}
                loverCnt={loverCnt}
                loverYn={loverYn}
                articleYn={articleYn}
              />
              {/* <img src={imageLink} width="500px" alt="네컷 이미지" /> */}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default MyPhotoCard;

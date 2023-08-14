import { useDispatch } from "react-redux";
import "./MyPhotoCard.css";
import { useState } from "react";
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
  const dispatch = useDispatch();
  const [isHovered, setIsHovered] = useState(false);
  const [isPhotoDelModalOpen, setIsPhotoDelModalOpen] = useState(false);

  const handleMouseHover = (event) => {
    event.stopPropagation();
    setIsHovered(true);
  };

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

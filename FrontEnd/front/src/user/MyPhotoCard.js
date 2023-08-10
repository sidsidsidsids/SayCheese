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
          <img
            width="160px"
            src={imageLink}
            alt="네컷 이미지"
            onMouseEnter={handleMouseHover}
          />
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <div className="HeartBtn">
              <div className="Heartcontent">
                <span
                  className={loverCnt === -1 ? "MyHeart" : "MyHeart Full"}
                ></span>
              </div>
            </div>

            <p
              className="MyPhotoDeleteBtn"
              style={{ margin: "20px" }}
              onClick={handleDeleteModalOpen}
            >
              삭제
            </p>
          </div>
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

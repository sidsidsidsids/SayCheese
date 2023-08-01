import { useState } from "react";
import "./PhotoCard.css";
import { useDispatch } from "react-redux";
import { openModal } from "../redux/features/modal/modalSlice";

function PhotoCard({
  author,
  createdDate,
  imageLink,
  loverCnt,
  loverYn,
  tags,
  payload,
}) {
  const dispatch = useDispatch();
  // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const [like, setLike] = useState(loverYn);

  return (
    <div
      className="PhotoCard"
      onClick={(event) => {
        dispatch(openModal(payload));
      }}
    >
      <img width="200px" src={imageLink} />
      <div>likes:{loverCnt}</div>
    </div>
  );
}

export default PhotoCard;

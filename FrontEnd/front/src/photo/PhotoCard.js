import { useState } from "react";
// third party
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import Swal from "sweetalert2";
// local
import "./PhotoCard.css";
import { openModal } from "../redux/features/modal/modalSlice";

function PhotoCard({
  articleId,
  author,
  createdDate,
  imageLink,
  loverCnt,
  loverYn,
  tags,
  payload,
}) {
  const { isLogin, userInfo } = useSelector((store) => store.login);

  const dispatch = useDispatch();
  // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const [like, setLike] = useState(loverYn);

  const handleCardClick = () => {
    if (!isLogin || !userInfo) {
      Swal.fire(
        "네컷사진을 자세히 보기 위해서는 로그인이 필요합니다.\n로그인 후 진행해주시길 바랍니다."
      );
    } else {
      dispatch(openModal(payload));
    }
  };

  return (
    <div className="PhotoCard" onClick={handleCardClick}>
      <img
        width="200px"
        height="200px"
        style={{ objectFit: "contain" }}
        // 가로세로비율 유지하면서 요소 내부에 들어감, 크기 맞지 않을 경우 가로세로비율 유지되는 정도까지 크기가 유지된다.
        src={imageLink}
        alt="네컷 이미지"
      />
      <div className="HeartBtn">
        <div className="Heartcontent">
          <span className={like === 1 ? "Heart Full" : "Heart"}></span>
        </div>
      </div>
    </div>
  );
}

export default PhotoCard;

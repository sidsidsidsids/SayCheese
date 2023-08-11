import React, { useState } from "react";

import "../css/FrameCard.css";

// react-redux
import { useDispatch } from "react-redux";
import { openModal } from "../../redux/features/modal/modalSlice";

// FrameCard 컴포넌트: 프레임을 한개씩 담을 카드 컴포넌트입니다
export default function FrameCard({
  name,
  imageLink,
  loverCnt,
  author,
  createDate,
  loverYn,
  payload,
}) {
  const dispatch = useDispatch();
  // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const [like, setLike] = useState(loverYn);

  function clickLike(event) {
    event.stopPropagation();
    setLike(!like);
    // api 추가해야함
  }

  return (
    // 카드를 클릭하면 isOpen을 true하기 합니다
    <div
      className="frameCard"
      onClick={(event) => {
        dispatch(openModal(payload));
      }}
    >
      {name}
      <img
        className="cardImg"
        width="100px"
        src={imageLink}
        alt="프레임 이미지"
      />
      <div className="heart-btn" onClick={clickLike}>
        <div className="content">
          <span
            className={
              like === 1
                ? "heart full"
                : like === true
                ? "heart-active heart"
                : "heart"
            }
          ></span>
        </div>
      </div>
    </div>
  );
}

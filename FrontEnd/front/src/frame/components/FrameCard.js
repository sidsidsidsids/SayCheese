import React, { useState } from "react";

import "../css/FrameCard.css";

// react-redux
import { useDispatch } from "react-redux";
import { openModal } from "../../redux/features/modal/modalSlice";
import axios from "axios";

// FrameCard 컴포넌트: 프레임을 한개씩 담을 카드 컴포넌트입니다
export default function FrameCard({
  name,
  imageLink,
  loverCnt,
  author,
  createDate,
  loverYn,
  payload,
  isPublic,
}) {
  const dispatch = useDispatch();
  const [like, setLike] = useState(loverYn); // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const accessToken = localStorage.getItem("accessToken");
  console.log(payload.articleId);
  function clickLike(event) {
    event.stopPropagation();
    setLike(!like);
    // api 추가해야함
  }
  // 좋아요 함수 로직 //
  // 좋아요
  async function handleLikePlus() {
    console.log(payload.articleId);
    axios
      .post(`/api/article/lover/frame/${payload.articleId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        setLike(true);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  // 좋아요 취소
  async function handleLikeMinus() {
    axios
      .delete(`/api/article/lover/${payload.articleId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        setLike(false);
      })
      .catch((error) => {
        console.log(error);
      });
  }
  // 좋아요
  function clickLike(event) {
    event.stopPropagation();
    console.log("좋아요", like);
    if (like) {
      axios
        .delete(`/api/article/lover/${payload.articleId}`, {
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        })
        .then((response) => {
          setLike(false);
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      axios
        .post(`/api/article/lover/frame/${payload.articleId}`, {
          headers: {
            "Content-Type": "application/json",
            Authorization: `${accessToken}`,
          },
        })
        .then((response) => {
          setLike(true);
        })
        .catch((error) => {
          console.log(error);
        });
    }
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
      <div className="heart-btn" onClick={(event) => clickLike(event)}>
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

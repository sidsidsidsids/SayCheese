import React, { useState } from "react";

import "../css/FrameCard.css";

// react-redux
import { useDispatch } from "react-redux";
import { openModal } from "../../redux/features/modal/modalSlice";
import axios from "axios";
import Swal from "sweetalert2";
// FrameCard 컴포넌트: 프레임을 한개씩 담을 카드 컴포넌트입니다
export default function FrameCard({
  subject,
  imageLink,
  loverCnt,
  author,
  createdDate,
  loverYn,
  payload,
  isPublic,
}) {
  const dispatch = useDispatch();
  const [like, setLike] = useState(loverYn); // 좋아요 체크 되어있으면 like:1 안 했으면 :0
  const accessToken = localStorage.getItem("accessToken");

  // 좋아요 함수 로직 //
  // 좋아요
  async function handleLikePlus() {
    axios
      .post(`/api/article/lover/frame/${payload.articleId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${accessToken}`,
        },
      })
      .then((response) => {
        setLike(true);
        getLikeData();
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
        getLikeData();
      })
      .catch((error) => {
        console.log(error);
      });
  }
  // 모달에 프레임 상세 데이터를 axios get 방식으로 가져오는 함수
  async function getLikeData() {
    try {
      const response = await axios.get(
        `/api/article/frame/${payload.articleId}`,
        // articleId에 해당하는 이미지 데이터 상세 정보 가져오기
        {
          headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      setLike(response.data.loverYn); // like에 좋아요 여부 저장합니다
    } catch (error) {
      console.log(error);
    }
  }

  // 좋아요
  function clickLike(event) {
    event.stopPropagation();
    // 예외처리 : 로그인 안 한 사람은 안되게 해야함
    if (accessToken) {
      // 로그인 한 사람만
      if (like) {
        handleLikeMinus();
      } else if (!like) {
        handleLikePlus();
      }
    } else {
      Swal.fire("로그인 해주세요");
    }
  }
  // 좋아요 함수 로직 //
  // 좋아요
  async function handleLikePlus() {
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
    console.log("안좋아요", like);
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
        getLikeData();
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
        getLikeData();
      })
      .catch((error) => {
        console.log(error);
      });
  }
  // 모달에 프레임 상세 데이터를 axios get 방식으로 가져오는 함수
  async function getLikeData() {
    console.log("like 숫자 받아올거임");
    try {
      const response = await axios.get(
        `/api/article/frame/${payload.articleId}`,
        // articleId에 해당하는 이미지 데이터 상세 정보 가져오기
        {
          headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      console.log(response.data.loverCnt);
      setLike(response.data.loverYn); // like에 좋아요 여부 저장합니다
    } catch (error) {
      console.log(error);
    }
  }

  // 좋아요
  function clickLike(event) {
    event.stopPropagation();
    // 예외처리 : 로그인 안 한 사람은 안되게 해야함
    if (accessToken) {
      // 로그인 한 사람만
      if (like) {
        handleLikeMinus();
      } else if (!like) {
        handleLikePlus();
      }
    } else {
      alert("로그인 해주세요");
    }
  }

  return (
    // 카드를 클릭하면 isOpen을 true하기 합니다
    <div
      className="frameCard"
      onClick={(event) => {
        event.stopPropagation();
        dispatch(openModal(payload));
      }}
    >
      <img
        className="cardImg"
        width="150px"
        height="150px"
        style={{ objectFit: "contain" }}
        src={imageLink}
        alt="프레임 이미지"
      />
      {subject}
      <div
        className="heart-btn"
        onClick={(event) => {
          event.stopPropagation();
          clickLike(event);
        }}
      >
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

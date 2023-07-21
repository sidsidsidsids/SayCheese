// 프레임 구경하기 전시 아이템으로 사용될 카드 컴포넌트 파일입니다.
import React, { useState } from "react";

import "./FrameCard.css";

import CardModal from "./CardModal";

export default function FrameCard({ name, imgSrc, likes }) {
  //모달에 쓸 데이터
  const [showModal, setShowModal] = useState(false); // 모달 열림/닫힘 상태를 관리합니다.
  // 모달 열기 함수를 정의합니다.
  const openModal = () => {
    setShowModal(true);
    console.log(openModal);
  };

  // 모달 닫기 함수를 정의합니다.
  const closeModal = () => {
    setShowModal(false);
  };

  return (
    <>
      <div className="frameCard" onClick={openModal}>
        {name}
        <img
          className="cardImg"
          width="100px"
          src={imgSrc}
          alt="프레임 이미지"
        />
        likes:{likes}
      </div>
      {showModal && (
        <CardModal open={showModal} close={closeModal} header="Modal heading" />
      )}
    </>
  );
}

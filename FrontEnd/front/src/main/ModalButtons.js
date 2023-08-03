import React from "react";
import "./ModalButtons.css";

const ModalButtons = ({ onConfirm, onClose }) => {
  return (
    <>
      <button className="modal-button" onClick={onConfirm}>
        확인
      </button>
      <button className="modal-button" onClick={onClose}>
        나가기
      </button>
    </>
  );
};

export default ModalButtons;

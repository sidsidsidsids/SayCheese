import React from "react";
import "./ModalButtons.css";

const ModalButtons = ({ onConfirm, onClose }) => {
  return (
    <div className="twoButtonAlign">
      <button className="modal-button btn" onClick={onConfirm}>
        확인
      </button>
      <button
        className="modal-button btn
      "
        onClick={onClose}
      >
        나가기
      </button>
    </div>
  );
};

export default ModalButtons;

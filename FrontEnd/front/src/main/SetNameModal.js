import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { notUserNickname } from "../redux/features/login/loginSlice";

import "./SetNameModal.css";
import ModalButtons from "./ModalButtons";

function SetNameModal({ open, close, onConfirm }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [inputNickname, setInputNickname] = useState("");

  if (!open) {
    return null;
  }
  const handleConfirm = () => {
    nicknameCheck(inputNickname);
  };
  const nicknameCheck = (nickname) => {
    axios
      .get(`/api/auth/guest?nickname=${nickname}`, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        const accessToken = response.headers["authorization"];
        localStorage.setItem("accessToken", accessToken);
        dispatch(notUserNickname(`Guest_${nickname}`));
        close();
      })
      .catch((error) => {
        console.log(error);
      });
  };
  return (
    <div className="set-name-modal">
      <div className="set-name-modal-content">
        <h2>현재 비로그인 상태입니다</h2>
        <button
          onClick={() => {
            navigate(`/user/login`);
            close();
          }}
          id="goLogin"
          className="btn"
        >
          로그인 하러가기
        </button>

        <div>닉네임 설정하고 계속하기</div>
        <input
          type="text"
          id="nickname"
          placeholder="닉네임을 입력해주세요"
          value={inputNickname}
          onChange={(event) => {
            setInputNickname(event.target.value);
          }}
          maxLength={10}
        />
        <ModalButtons onConfirm={handleConfirm} onClose={close} />
      </div>
    </div>
  );
}

export default SetNameModal;

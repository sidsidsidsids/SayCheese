import React, { useState } from "react";

import "../css/CardModal.css";
// react-redux
import { useDispatch, useSelector } from "react-redux";
import { closeModal } from "../../redux/features/modal/modalSlice";

export default function CardModal() {
  // 모달에 표시할 내용이 없으면 에러가 나지않게 로딩 상태를 표시
  const [loading] = useState(false);

  // 모달에 띄울 컨텐츠를 가져옵니다
  // state는 젠처 리덕스 스토어를 의미하며, modal 리듀서에서 관리되는 상태 객체 modalContent를 추출합니다.
  const { isOpen } = useSelector((store) => store.modal);
  const { modalContent } = useSelector((state) => state.modal);

  const dispatch = useDispatch();

  return (
    <>
      <div className="modalBackdrop"></div>
      <div className="modal">
        {loading ? (
          <div>loading..</div>
        ) : (
          <>
            {modalContent.name}
            <br />
            {modalContent.writer}
            <br />
            {/* .imgSrc에 추후 주의가 필요합니다. 이후 재설정이 필요합니다 */}

            <img src={modalContent.imgSrc.sampleImg} alt="프레임 이미지" />
            <br />

            {modalContent.likes}
          </>
        )}
        <button
          onClick={() => {
            dispatch(closeModal());
          }}
        >
          닫기
        </button>
      </div>
    </>
  );
}

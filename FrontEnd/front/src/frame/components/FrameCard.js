import "../css/FrameCard.css";

// react-redux
import { useDispatch } from "react-redux";
import { openModal } from "../../redux/features/modal/modalSlice";

// FrameCard 컴포넌트: 프레임을 한개씩 담을 카드 컴포넌트입니다
export default function FrameCard({ name, imgSrc, likes, writer, payload }) {
  const dispatch = useDispatch();

  return (
    // 카드를 클릭하면 isOpen을 true하기 합니다
    <div className="frameCard" onClick={() => dispatch(openModal(payload))}>
      {/* <div className="frameCard" onClick={openModal}> */}
      {name}
      <img className="cardImg" width="100px" src={imgSrc} alt="프레임 이미지" />
      likes:{likes}
    </div>
  );
}

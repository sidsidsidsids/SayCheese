// 프레임 구경하기 전시 아이템으로 사용될 카드 컴포넌트 파일입니다.

import "./FrameCard.css";

export default function FrameCard({ name, imgSrc, likes }) {
  return (
    <div className="frameCard">
      {name}
      <br />
      <img width="100px" src={imgSrc} alt="프레임 이미지" />
      <br />
      likes:{likes}
    </div>
  );
}

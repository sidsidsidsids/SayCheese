// 프레임 리스트 아이템으로 사용될 카드 컴포넌트 파일입니다.

import "./FrameCard.css";

export default function FrameCard({ name, imgSrc, likes }) {
  return (
    <div className="frameCard">
      {name}
      <br />
      <img width="100px" src={imgSrc} />
      <br />
      likes:{likes}
    </div>
  );
}

// 프레임 메인 페이지입니다.
import "./Frame.css";

import FrameCreateButton from "./FrameCreateButton";
import FrameSearchBar from "./FrameSearchBar";
import FrameList from "./FrameList";

export default function Frame() {
  return (
    <div className="frame">
      <p>Frame 페이지입니다.</p>
      <div className="frameHeader">
        <FrameCreateButton />
        <FrameSearchBar />
      </div>
      <FrameList />
    </div>
  );
}

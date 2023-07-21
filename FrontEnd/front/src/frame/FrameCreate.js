// Frame Create를 위한 메인입니다.
import "./FrameCreate.css";

import CreateToolBar from "./FrameCreateToolBar";
import CreateCanvas from "./FrameCreateCanvas";

export default function FrameCreate() {
  return (
    <div className="createWrapper">
      <div className="createSpace">
        <CreateToolBar />
        <CreateCanvas />
      </div>
    </div>
  );
}

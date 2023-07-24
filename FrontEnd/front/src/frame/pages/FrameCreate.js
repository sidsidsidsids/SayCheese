// Frame Create를 위한 메인입니다.
import "../css/FrameCreate.css";

import CreateToolBar from "../components/FrameCreateToolBar";
import CreateCanvas from "../components/FrameCreateCanvas";

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

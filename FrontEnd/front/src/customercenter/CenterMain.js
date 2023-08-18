import { Outlet } from "react-router-dom";
// local
import "./CenterMain.css";

// 공지사항과 자주 묻는 질문 나타내는 부분
function CenterMain() {
  return (
    <div className="CenterMain">
      <Outlet />
      {/* 공지사항 또는 자주 묻는 질문 라우터 */}
    </div>
  );
}

export default CenterMain;

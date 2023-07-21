import "./CenterMain.css";
import NoticeList from "./NoticeList";
import Notice from "./Notice";
import Faq from "./Faq";

// 공지사항과 자주 묻는 질문 나타내는 부분
function CenterMain() {
  return (
    <div className="CenterMain">
      <Notice />
    </div>
  );
}

export default CenterMain;

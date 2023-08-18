import { useState } from "react";
// local
import "./NoticeWrite.css";

function NoticeWrite() {
  const [activeIndex, setActiveIndex] = useState(null);

  const handleInputFocus = (index) => {
    setActiveIndex(index);
  };

  const handleInputBlur = () => {
    setActiveIndex(null);
  };

  return (
    <div>
      <h1>공지사항 작성</h1>
      <br className="stop-dragging" />
      <form>
        <div
          className={`NoticeInputLine ${activeIndex === 1 ? "focused" : ""}`}
        >
          <input
            type="text"
            name="subject"
            placeholder="제목"
            className="NoticeInput"
            onFocus={() => handleInputFocus(1)}
            onBlur={handleInputBlur}
          />
        </div>
        <div
          className={`NoticeInputLine ${activeIndex === 2 ? "focused" : ""}`}
        >
          <textarea
            style={{ width: "85%" }}
            rows="20"
            name="content"
            placeholder="내용"
            className="NoticeTextArea"
            onFocus={() => handleInputFocus(2)}
            onBlur={handleInputBlur}
          />
        </div>
      </form>
    </div>
  );
}

export default NoticeWrite;

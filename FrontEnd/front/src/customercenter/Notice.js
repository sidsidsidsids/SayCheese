import "./Notice.css";

function Notice() {
  return (
    <div>
      <h1>공지사항</h1>
      <br className="stop-dragging" />
      <hr className="NoticeLine" />
      <h2>제목1</h2>
      <div className="NoticeInfo">
        <h4>관리자</h4>
        <div className="NoticeInfo">
          <h4>2023.07.17</h4>
          <h4>&nbsp;&nbsp;조회 255</h4>
        </div>
      </div>
      <hr className="NoticeLine" />
      <div className="NoticeMain">
        <br className="stop-dragging" />
        내용내용
        <br />
        어쩌구저쩌구~~~
      </div>
      <hr className="NoticeLine" />
    </div>
  );
}
export default Notice;

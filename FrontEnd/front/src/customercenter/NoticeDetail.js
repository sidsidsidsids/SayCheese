function NoticeDetail({ id, subject, createdDate, hit, content }) {
  return (
    <div>
      <h1>공지사항</h1>
      <br className="stop-dragging" />
      <h2 className="NoticeTextSort">{subject}</h2>
      <div className="NoticeInfo">
        <h4>관리자</h4>
        <div className="NoticeInfo">
          <h4>
            {new Date(createdDate).toLocaleString("ko-KR", {
              year: "numeric",
              month: "2-digit",
              day: "2-digit",
              hour: "2-digit",
              minute: "2-digit",
              second: "2-digit",
            })}
          </h4>
          <h4>&nbsp;&nbsp;조회 {hit}</h4>
        </div>
      </div>
      <hr className="NoticeLine" />
      <div className="NoticeMain NoticeTextSort">
        <br className="stop-dragging" />
        {content}
      </div>
      <hr className="NoticeLine" />
    </div>
  );
}

export default NoticeDetail;

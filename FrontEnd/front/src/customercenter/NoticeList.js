import { useState, useEffect } from "react";
// third party
import { Link } from "react-router-dom";
import axios from "axios";
// local
import "./NoticeList.css";
import Paging from "./Paging";
import { useSelector } from "react-redux";

function NoticeList() {
  const { userInfo } = useSelector((store) => store.login);

  const [notices, setNotices] = useState([]); // 나타낼 공지사항
  const [count, setCount] = useState(0); // 공지사항 총 개수
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const [postPerPage] = useState(10); // 페이지 당 공지사항 개수

  useEffect(() => {
    getNoticeList();
  }, [currentPage]); // 현재 페이지가 바뀔 때마다 공지사항 목록 다시 불러오기

  async function getNoticeList() {
    try {
      const response = await axios.get(
        `/api/article/notice?page=${currentPage}`,
        {
          headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      setNotices(response.data.noticeArticleVoList);
      setCount(response.data.pageNavigator.totalDataCount);
    } catch (error) {
      console.log(error);
    }
  }

  const indexOfLastPost = currentPage * postPerPage;
  const indexOfFirstPost = indexOfLastPost - postPerPage;
  const currentPosts = notices.slice(indexOfFirstPost, indexOfLastPost);

  const setPage = (e) => {
    setCurrentPage(e);
  };

  return (
    <div>
      <h1>공지사항</h1>
      <br className="stop-dragging" />
      <table>
        <thead>
          <tr>
            <th className="TableCell1">번호</th>
            <th>제목</th>
            <th className="TableCell2">작성일</th>
            <th className="TableCell3">조회수</th>
          </tr>
        </thead>
        <tbody>
          {notices.map((notice, index) => (
            <tr key={index}>
              <td>{notice.id}</td>
              <td>
                <Link
                  className="NoticeTitle"
                  to={`/customercenter/notice/${notice.id}`}
                >
                  {notice.subject}
                </Link>
              </td>
              <td>{new Date(notice.createdDate).toLocaleDateString()}</td>
              <td>{notice.hit}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <Paging page={currentPage} count={count} setPage={setPage} />
      <div className="NoticeWriteBtnSort">
        {!userInfo ? null : userInfo.email === "mailto:admin@admin.com" ? (
          <Link className="NoticeWriteBtn" to="/customercenter/notice/write">
            글 작성
          </Link>
        ) : null}
      </div>
    </div>
  );
}

export default NoticeList;

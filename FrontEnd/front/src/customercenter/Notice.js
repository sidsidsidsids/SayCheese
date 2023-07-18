import { useState, useEffect } from "react";
import "./Notice.css";
import Paging from "./Paging";

function Notice() {
  {
    /* API통신 후 다시 페이지네이션 해봐야 함 */
  }
  const [notices, setNotices] = useState([]); // 나타낼 공지사항
  const [count, setCount] = useState(0); // 공지사항 총 개수
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const [postPerPage] = useState(5); // 페이지 당 공지사항 개수
  const [indexOfLastPost, setIndexOfLastPost] = useState(0);
  const [indexOfFirstPost, setIndexOfFirstPost] = useState(0);
  const [currentPosts, setCurrentPosts] = useState(0);

  useEffect(() => {
    setCount(notices.length);
    setIndexOfLastPost(currentPage * postPerPage);
    setIndexOfFirstPost(indexOfLastPost - postPerPage);
    setCurrentPosts(notices.slice(indexOfFirstPost, indexOfLastPost));
  }, [currentPage, indexOfFirstPost, indexOfLastPost, notices, postPerPage]);

  const setPage = (e) => {
    setCurrentPage(e);
  };

  return (
    <div className="Notice">
      <div className="Container">
        <div className="NoticeMenu">
          <h1>고객센터</h1>
          <hr />
          <h3>공지사항</h3>
          <hr />
          <h3>자주 묻는 질문</h3>
        </div>
        <div className="NoticeTable">
          <h1>공지사항</h1>
          <br />
          <table>
            <thead>
              <tr>
                <th className="TableCell1">번호</th>
                <th>제목</th>
                <th className="TableCell2">작성일</th>
                <th className="TableCell2">조회수</th>
              </tr>
            </thead>
            {/* 레이아웃 짜기 위해 임의로 데이터 작성함 */}
            <tbody>
              <tr>
                <td>6</td>
                <td>제목6</td>
                <td>2023.07.15</td>
                <td>324</td>
              </tr>
              <tr>
                <td>5</td>
                <td>제목5</td>
                <td>2023.07.12</td>
                <td>426</td>
              </tr>
              <tr>
                <td>4</td>
                <td>제목4</td>
                <td>2023.07.11</td>
                <td>493</td>
              </tr>
              <tr>
                <td>3</td>
                <td>제목3</td>
                <td>2023.07.08</td>
                <td>522</td>
              </tr>
              <tr>
                <td>2</td>
                <td>제목2</td>
                <td>2023.07.04</td>
                <td>972</td>
              </tr>
              <tr>
                <td>1</td>
                <td>제목1</td>
                <td>2023.07.01</td>
                <td>1223</td>
              </tr>
            </tbody>
          </table>
          <Paging page={currentPage} count={count} setPage={setPage} />
        </div>
      </div>
    </div>
  );
}

export default Notice;

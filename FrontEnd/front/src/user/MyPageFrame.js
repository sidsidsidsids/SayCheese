import { useEffect, useState } from "react";
// third party
import axios from "axios";
// local
import MyFrameCard from "./MyFrameCard";
import MyPhotoNull from "./MyPhotoNull";
import Paging from "../customercenter/Paging";

function MyPageFrame() {
  const [frames, setFrames] = useState([]); // 나타낼 프레임
  const [count, setCount] = useState(0); // 프레임 총 개수
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const [postPerPage] = useState(9); // 페이지 당 사진 개수

  const [myFrameChange, setMyFrameChange] = useState(false); // 내 프레임 목록이 변경(삭제)되었는지 여부 - true일 경우 변경됨

  useEffect(() => {
    getMyFrames();
    setMyFrameChange(false);
  }, [currentPage, myFrameChange]);

  const itemsPerRow = 3; // 각 줄의 아이템 개수
  const rows = [];
  let currentRow = [];

  for (let i = 0; i < frames.length; i++) {
    currentRow.push(frames[i]);

    if (currentRow.length === itemsPerRow) {
      rows.push(
        <div key={i} className="Rows">
          {currentRow.map((item) => (
            <MyFrameCard
              key={item.articleId}
              articleId={item.articleId}
              subject={item.subject}
              frameLink={item.frameLink}
              loverCnt={item.loverCnt}
              createdDate={item.createdDate}
              author={item.author}
              isPublic={item.isPublic}
              frameSpecification={item.frameSpecification}
              loverYn={item.loverYn}
              myFrameChange={myFrameChange}
              setMyFrameChange={setMyFrameChange}
            />
          ))}
        </div>
      );

      currentRow = [];
    }
  }

  if (currentRow.length > 0) {
    if (currentRow.length === 1) {
      rows.push(
        <div key={frames.length} className="Rows">
          <MyFrameCard
            key={currentRow[0].articleId}
            articleId={currentRow[0].articleId}
            subject={currentRow[0].subject}
            frameLink={currentRow[0].frameLink}
            loverCnt={currentRow[0].loverCnt}
            createdDate={currentRow[0].createdDate}
            author={currentRow[0].author}
            isPublic={currentRow[0].isPublic}
            frameSpecification={currentRow[0].frameSpecification}
            loverYn={currentRow[0].loverYn}
            myFrameChange={myFrameChange}
            setMyFrameChange={setMyFrameChange}
          />
          <MyPhotoNull />
          <MyPhotoNull />
        </div>
      );
    } else if (currentRow.length === 2) {
      rows.push(
        <div key={frames.length} className="Rows">
          {currentRow.map((item) => (
            <MyFrameCard
              key={item.articleId}
              articleId={item.articleId}
              subject={item.subject}
              frameLink={item.frameLink}
              loverCnt={item.loverCnt}
              createdDate={item.createdDate}
              author={item.author}
              isPublic={item.isPublic}
              frameSpecification={item.frameSpecification}
              loverYn={item.loverYn}
              myFrameChange={myFrameChange}
              setMyFrameChange={setMyFrameChange}
            />
          ))}
          <MyPhotoNull />
        </div>
      );
    }
  }

  // 내 프레임 목록을 axios get 방식으로 가져오는 함수
  async function getMyFrames() {
    const accessToken = localStorage.getItem("accessToken");

    try {
      const response = await axios.get("/api/article/frame/my/list", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
          Authorization: `${accessToken}`,
        },
      });
      setFrames(response.data.frameArticleVoList);
      setCount(response.data.pageNavigator.totalDataCount);
    } catch (error) {
      console.log(error);
    }
  }

  const indexOfLastPost = currentPage * postPerPage;
  const indexOfFirstPost = indexOfLastPost - postPerPage;
  const currentPosts = frames.slice(indexOfFirstPost, indexOfLastPost);

  const setPage = (e) => {
    setCurrentPage(e);
  };

  return (
    <div>
      <div className="MyPageBox">
        <h1>내가 만든 프레임</h1>
        {rows}
        <Paging page={currentPage} count={count} setPage={setPage} />
      </div>
    </div>
  );
}

export default MyPageFrame;

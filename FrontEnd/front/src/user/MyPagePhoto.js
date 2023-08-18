import { useEffect, useState } from "react";
// third party
import axios from "axios";
// local
import "./MyPagePhoto.css";
import MyPhotoNull from "./MyPhotoNull";
import MyPhotoCard from "./MyPhotoCard";
import Paging from "../customercenter/Paging";

function MyPagePhoto() {
  const [photos, setPhotos] = useState([]); // 나타낼 사진
  const [count, setCount] = useState(0); // 사진 총 개수
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const [postPerPage] = useState(9); // 페이지 당 사진 개수

  const [myPhotoChange, setMyPhotochange] = useState(false);

  useEffect(() => {
    // 페이지가 바뀌거나 내 이미지 목록에 변경이 생기면 내 사진 목록 다시 가져오기
    getMyPhotos();
    setMyPhotochange(false);
  }, [currentPage, myPhotoChange]);

  const itemsPerRow = 3; // 각 줄의 아이템 개수
  const rows = [];
  let currentRow = [];

  for (let i = 0; i < photos.length; i++) {
    currentRow.push(photos[i]);

    if (currentRow.length === itemsPerRow) {
      rows.push(
        <div key={i} className="Rows">
          {currentRow.map((item) => (
            <MyPhotoCard
              key={item.imageId}
              imageId={item.imageId}
              imageLink={item.imageLink}
              payload={item}
              createdDate={item.createdDate}
              loverCnt={item.loverCnt}
              loverYn={item.loverYn}
              articleYn={item.articleYn}
              myPhotoChange={myPhotoChange}
              setMyPhotochange={setMyPhotochange}
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
        <div key={photos.length} className="Rows">
          <MyPhotoCard
            key={currentRow[0].imageId}
            imageId={currentRow[0].imageId}
            imageLink={currentRow[0].imageLink}
            payload={currentRow[0]}
            createdDate={currentRow[0].createdDate}
            loverCnt={currentRow[0].loverCnt}
            loverYn={currentRow[0].loverYn}
            articleYn={currentRow[0].articleYn}
            myPhotoChange={myPhotoChange}
            setMyPhotochange={setMyPhotochange}
          />
          <MyPhotoNull />
          <MyPhotoNull />
        </div>
      );
    } else if (currentRow.length === 2) {
      rows.push(
        <div key={photos.length} className="Rows">
          {currentRow.map((item) => (
            <MyPhotoCard
              key={item.imageId}
              imageId={item.imageId}
              imageLink={item.imageLink}
              payload={item}
              createdDate={item.createdDate}
              loverCnt={item.loverCnt}
              loverYn={item.loverYn}
              articleYn={item.articleYn}
              myPhotoChange={myPhotoChange}
              setMyPhotochange={setMyPhotochange}
            />
          ))}
          <MyPhotoNull />
        </div>
      );
    }
  }

  // 내 이미지 목록 axios get 방식으로 가져오기
  async function getMyPhotos() {
    const accessToken = localStorage.getItem("accessToken");
    try {
      const response = await axios.get(`/api/image?page=${currentPage}`, {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
          Authorization: `${accessToken}`,
        },
      });
      setPhotos(response.data.imageVoList);
      setCount(response.data.pageNavigator.totalDataCount);
    } catch (error) {
      console.log(error);
    }
  }

  const indexOfLastPost = currentPage * postPerPage;
  const indexOfFirstPost = indexOfLastPost - postPerPage;
  const currentPosts = photos.slice(indexOfFirstPost, indexOfLastPost);

  const setPage = (e) => {
    setCurrentPage(e);
  };

  return (
    <div>
      <div className="MyPageBox">
        <h1>내가 찍은 사진</h1>
        {rows}
        <Paging page={currentPage} count={count} setPage={setPage} />
      </div>
    </div>
  );
}

export default MyPagePhoto;

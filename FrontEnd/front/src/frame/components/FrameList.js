// FrameList 컴포넌트 : 프레임 리스트를 서버에서 받아와서 렌더링할 컴포넌트입니다.

import "../css/FrameList.css";
import "../css/Pagination.css";

import React, { useState, useEffect } from "react";
// third party
import { useSelector } from "react-redux";
import Pagination from "react-js-pagination";
import axios from "axios";
// components
import FrameCard from "./FrameCard";
// import sampleImg from "../assets/snoopy.png";
import sampleImg from "../assets/luka.jpg";
import CardModal from "./CardModal";

// FrameList() 함수 : API를 호출해 프레임 정보를 가져옵니다.
export default function FrameList() {
  // TODO: 이후 서버 API가 나오면 프레임 리스트를 fetch해야 합니다
  const [frameList, setFrameList] = useState([
    {
      id: 0,
      name: " ",
      imageLink: {},
      author: " ",
      loverCnt: 0,
      createDate: " ",
      loverYn: 0,
    },
  ]);

  // 스토어에서 모달이 열려있는지 확인하는 isOpen을 가져옴
  const { isOpen } = useSelector((store) => store.modal);

  // 페이지네이션의 기능을 담당합니다.
  const [activePage, setActivePage] = useState(1);
  const [page, setPage] = useState(0);

  function handlePageChange(pageNumber) {
    console.log(`active page is ${pageNumber}`);
    setActivePage(pageNumber);
    console.log(`/api/article/frame/list/recent?curPage=${activePage}`);
  }
  useEffect(() => {
    axios
      .get(`/api/article/frame/list/recent?curPage=${activePage}`, {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
        },
      })
      .then((res) => {
        console.log("######", res.data);
        setFrameList(res.data.frameArticleVoList);
        setPage(res.data.pageNavigator);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [activePage]);

  return (
    <div className="frameGallery">
      <div className="frameList">
        {frameList.map((item) => (
          <FrameCard
            key={item.articleId}
            name={item.subject}
            imageLink={item.frameLink}
            loverCnt={item.loverCnt}
            author={item.author}
            loverYn={item.loverYn}
            isPublic={item.isPublic}
            createDate={item.createdDate}
            // 모달 띄울때 사용하려고 전체 데이터 전달
            payload={item}
          />
        ))}
        {/* isOpen이 true일때 모달이 열립니다 */}
        {isOpen && <CardModal />}
        {/* // 페이지네이션 */}
      </div>
      <Pagination
        activePage={activePage}
        itemsCountPerPage={6}
        totalItemsCount={page.totalDataCount}
        pageRangeDisplayed={5}
        onChange={(e) => {
          handlePageChange(e);
        }}
      />
    </div>
  );
}

// FrameList 컴포넌트 : 프레임 리스트를 서버에서 받아와서 렌더링할 컴포넌트입니다.

import React, { useState, useEffect } from "react";
// third party
import { useSelector } from "react-redux";
import Pagination from "react-js-pagination";
import axios from "axios";
// components
import FrameCard from "./FrameCard";
import CardModal from "./CardModal";
// css
import "../css/FrameList.css";
import "../css/Pagination.css";

// FrameList() 함수 : API를 호출해 프레임 정보를 가져옵니다.
export default function FrameList({ searchWord }) {
  // 기존 검색어와 새로 검색어의 비교를 위해 정보를 저장합니다.
  //
  const [beforeSearch, setBeforeSearch] = useState("");
  // TODO: 이후 서버 API가 나오면 프레임 리스트를 fetch해야 합니다
  const [frameList, setFrameList] = useState([]);

  // 스토어에서 모달이 열려있는지 확인하는 isOpen을 가져옴
  const { isOpen } = useSelector((store) => store.modal);

  // 페이지네이션의 기능을 담당합니다.
  const [activePage, setActivePage] = useState(1); // 페이지네이션을 위한 값으로 초기값은 1로 생성합니다
  const [page, setPage] = useState(0); // 페이지네이션에 사용할 데이터입니다
  // TODO: 검색어가 있다면 검색어 api로 넘거여햔다.
  // 검색어가 없다면 아래 api 로직을 수행한다

  if (searchWord) {
    if (beforeSearch !== searchWord) {
      setBeforeSearch(searchWord);
      setActivePage(1);
    }
  }
  function handlePageChange(pageNumber) {
    setActivePage(pageNumber);
  }

  function fetchRecetAll() {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .get(`/api/article/frame/list/recent?page=${activePage}`, {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
          Authorization: `${accessToken}`,
        },
      })
      .then((res) => {
        setFrameList(res.data.frameArticleVoList);
        setPage(res.data.pageNavigator);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  function fetchRecentSearch() {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .get(
        `/api/article/frame/list/recent?searchWord=${searchWord}&page=${activePage}`,
        {
          headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "69420",
            Authorization: `${accessToken}`,
          },
        }
      )
      .then((res) => {
        setFrameList(res.data.frameArticleVoList);
        setPage(res.data.pageNavigator);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  useEffect(() => {
    if (searchWord) {
      fetchRecentSearch();
    } else {
      fetchRecetAll();
    }
  }, [activePage, searchWord]);

  return (
    <div className="frameGallery">
      <div className="frameList">
        {frameList.map((item) => (
          <FrameCard
            key={item.articleId}
            articleId={item.articleId}
            subject={item.subject}
            imageLink={item.frameLink}
            loverCnt={item.loverCnt}
            author={item.author}
            loverYn={item.loverYn}
            isPublic={item.isPublic}
            createdDate={item.createdDate}
            // 모달 띄울때 사용하려고 전체 데이터 전달
            payload={item}
          />
        ))}
        {/* isOpen이 true일때 모달이 열립니다 */}
        {isOpen && <CardModal />}
        {/* // 페이지네이션 */}
      </div>
      {page ? (
        <Pagination
          activePage={activePage}
          itemsCountPerPage={8}
          totalItemsCount={page.totalDataCount}
          pageRangeDisplayed={5}
          onChange={(e) => {
            handlePageChange(e);
          }}
        />
      ) : null}
    </div>
  );
}

// FrameList 컴포넌트 : 프레임 리스트를 서버에서 받아와서 렌더링할 컴포넌트입니다.

import "../css/FrameList.css";
import "../css/Pagination.css";

import React, { useState } from "react";
// components
import FrameCard from "./FrameCard";
import sampleImg from "../assets/snoopy.png";
import CardModal from "./CardModal";
// react-redux
import { useSelector } from "react-redux";
// pagination
import Pagination from "react-js-pagination";

// FrameList() 함수 : API를 호출해 프레임 정보를 가져옵니다.
export default function FrameList() {
  // TODO: 이후 서버 API가 나오면 프레임 리스트를 fetch해야 합니다
  const [frameList, setFrameList] = useState([
    {
      id: 0,
      name: "snoopy",
      imgSrc: { sampleImg },
      writer: "sk",
      likes: 10,
    },
    {
      id: 1,
      name: "snoopy2",
      imgSrc: { sampleImg },
      writer: "sk",
      likes: 12,
    },
    {
      id: 2,
      name: "snoopy3",
      imgSrc: { sampleImg },
      writer: "sk",
      likes: 20,
    },
    {
      id: 3,
      name: "snoopy4",
      imgSrc: { sampleImg },
      writer: "sk",
      likes: 100,
    },
  ]);

  // 스토어에서 모달이 열려있는지 확인하는 isOpen을 가져옴
  const { isOpen } = useSelector((store) => store.modal);

  // 페이지네이션의 기능을 담당합니다.
  const [activePage, setActivePage] = useState(1);

  function handlePageChange(pageNumber) {
    console.log(`active page is ${pageNumber}`);
    setActivePage(pageNumber);
  }

  return (
    <div className="frameGallery">
      <div className="frameList">
        {frameList.map((item) => (
          <FrameCard
            key={item.id}
            name={item.name}
            imgSrc={item.imgSrc.sampleImg}
            likes={item.likes}
            writer={item.writer}
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
        itemsCountPerPage={10}
        totalItemsCount={450}
        pageRangeDisplayed={5}
        onChange={(e) => {
          handlePageChange(e);
        }}
      />
    </div>
  );
}

// FrameList 컴포넌트 : 프레임 리스트를 서버에서 받아와서 렌더링할 컴포넌트입니다.

import "../css/FrameList.css";

import React, { useState } from "react";
import FrameCard from "./FrameCard";
import sampleImg from "../assets/snoopy.png";
import CardModal from "./CardModal";
// react-redux
import { useSelector } from "react-redux";

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

  return (
    <div className="FrameList">
      {frameList.map((item) => (
        <FrameCard
          key={item.id}
          name={item.name}
          imgSrc={item.imgSrc.sampleImg}
          likes={item.likes}
          writer={item.writer}
          // 모달 띄울때 payload 편하게 주려고 추가하였음
          payload={item}
        />
      ))}
      {/* isOpen이 true일때 모달이 열립니다 */}
      {isOpen && <CardModal />}
    </div>
    // 페이지네이션
  );
}

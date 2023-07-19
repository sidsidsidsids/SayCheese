// 프레임 리스트 컴포넌트 입니다.
// todo: get frame data

import "./FrameList.css";

import React, { useState } from "react";
import FrameCard from "./FrameCard";
import sampleImg from "./assets/snoopy.png";

export default function FrameList() {
  const [frameList, setFrameList] = useState([
    {
      id: 0,
      name: "snoopy",
      imgSrc: { sampleImg },
      likes: 10,
    },
    {
      id: 1,
      name: "snoopy2",
      imgSrc: { sampleImg },
      likes: 12,
    },
    {
      id: 2,
      name: "snoopy3",
      imgSrc: { sampleImg },
      likes: 20,
    },
    {
      id: 3,
      name: "snoopy4",
      imgSrc: { sampleImg },
      likes: 100,
    },
  ]);
  frameList.map((item) => console.log(item));

  return (
    <div className="FrameList">
      {frameList.map((item) => (
        <FrameCard
          key={item.id}
          name={item.name}
          imgSrc={item.imgSrc.sampleImg}
          likes={item.likes}
        />
      ))}
    </div>
  );
}

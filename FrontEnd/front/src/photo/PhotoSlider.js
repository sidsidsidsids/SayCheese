import React, { useState } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import "./PhotoSlider.css";
import PhotoCard from "./PhotoCard";
import sampleImg from "./assets/snoopy.png";

function PhotoSlider() {
  const settings = {
    dots: true, // 페이지 번호 표시
    arrows: true, // 좌,우 버튼
    infinite: true, // 무한 반복
    draggable: false, // 드래그
    speed: 500, // 슬라이드 전환 속도 (ms)
    slidesToShow: 3, // 보여줄 슬라이드 수
    slidesToScroll: 3, // 스크롤시 이동할 슬라이드 수
    initialSlide: 0,
    responsive: [
      {
        breakpoint: 1300,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2,
          infinite: true,
          dots: true,
        },
      },
      {
        breakpoint: 900,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          dots: true,
        },
      },
    ],
  };

  const [photoList, setPhotoList] = useState([
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

  return (
    <div>
      <Slider {...settings}>
        {photoList.map((item) => (
          <div>
            <PhotoCard
              key={item.id}
              name={item.name}
              imgSrc={item.imgSrc.sampleImg}
              likes={item.likes}
            />
          </div>
        ))}
      </Slider>
    </div>
  );
}

export default PhotoSlider;

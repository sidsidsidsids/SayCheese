import React from "react";
// 슬라이더
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
// local
import "./PhotoSlider.css";
import PhotoCard from "./PhotoCard";

function PhotoSlider({ photoList }) {
  const settings = {
    dots: true, // 페이지 번호 표시
    arrows: true, // 좌,우 버튼
    infinite: true, // 무한 반복
    draggable: false, // 드래그
    speed: 500, // 슬라이드 전환 속도 (ms)
    slidesToShow: 3, // 보여줄 슬라이드 수
    slidesToScroll: 3, // 스크롤시 이동할 슬라이드 수
    initialSlide: 0, // 시작 페이지
    responsive: [
      {
        breakpoint: 1300,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2,
          infinite: true,
          dots: true,
          initialSlide: 0,
        },
      },
      {
        breakpoint: 900,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          dots: true,
          initialSlide: 0,
        },
      },
    ],
  };

  return (
    <div>
      <div>
        <Slider {...settings}>
          {photoList.map((item) => (
            <div>
              <PhotoCard
                key={item.articleId}
                articleId={item.articleId}
                name={item.name}
                imageLink={item.imageLink}
                loverCnt={item.loverCnt}
                loverYn={item.loverYn}
                // 모달 띄울때 사용하려고 전체 데이터 전달
                payload={item}
              />
            </div>
          ))}
        </Slider>
      </div>
    </div>
  );
}

export default PhotoSlider;

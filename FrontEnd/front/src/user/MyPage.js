import { useState } from "react";
import "./MyPage.css";
import MyProfile from "./MyProfile";
import MyPhotoCard from "./MyPhotoCard";
import sampleImg from "./assets/snoopy.png";

function MyPage() {
  const [myPhotoList, setMyPhotoList] = useState([
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
  ]);

  const [MyFrameList, setMyFrameList] = useState([
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
  ]);

  return (
    <div className="MyPageBox">
      <div className="MyProfileSort">
        <MyProfile name={"김싸피"} />
      </div>
      <div className="MyPageTitle">
        <h2>내가 찍은 사진</h2>
        <h3>더보기</h3>
      </div>
      <div className="MyPagePhoto">
        {myPhotoList.map((item) => (
          <MyPhotoCard
            key={item.id}
            name={item.name}
            imgSrc={item.imgSrc.sampleImg}
            likes={item.likes}
          />
        ))}
      </div>
      <div className="MyPageTitle">
        <h2>내가 만든 프레임</h2>
        <h3>더보기</h3>
      </div>
      <div className="MyPagePhoto">
        {MyFrameList.map((item) => (
          <MyPhotoCard
            key={item.id}
            name={item.name}
            imgSrc={item.imgSrc.sampleImg}
            likes={item.likes}
          />
        ))}
      </div>
    </div>
  );
}

export default MyPage;

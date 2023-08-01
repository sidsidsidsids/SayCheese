import "./Photo.css";
import PhotoCategory from "./PhotoCategory";
import PhotoSlider from "./PhotoSlider";
import sampleImg from "./assets/snoopy.png";
import { useSelector } from "react-redux";
import PhotoModal from "./PhotoModal";
import { useEffect, useState } from "react";
import axios from "axios";
function Photo() {
  const [photoListLike, setPhotoListLike] = useState([]); // 좋아요 순 이미지
  const [photoListRecent, setPhotoListRecent] = useState([]); // 최신 순 이미지
  const [photoListRandom, setPhotoListRandom] = useState([]); // 이미지 랜덤으로

  useEffect(() => {
    getPhotoListLike();
    getPhotoListRecent();
    getPhotoListRandom();
  }, []);

  async function getPhotoListLike() {
    try {
      const response = await axios.get("/api/article/image/list/hot", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
        },
      });
      setPhotoListLike(response.data);
    } catch (error) {
      console.log(error);
    }
  }

  async function getPhotoListRecent() {
    try {
      const response = await axios.get("/api/article/image/list/recent", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
        },
      });
      setPhotoListRecent(response.data);
    } catch (error) {
      console.log(error);
    }
  }

  async function getPhotoListRandom() {
    try {
      const response = await axios.get("/api/article/image/list/random", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
        },
      });
      setPhotoListRandom(response.data);
    } catch (error) {
      console.log(error);
    }
  }

  // 스토어에서 모달이 열려있는지 확인하는 isOpen을 가져옴
  const { isOpen } = useSelector((store) => store.modal);

  return (
    <div className="PhotoBox">
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"가장 인기있는 사진"} />
        </div>
        <PhotoSlider photoList={photoListLike} />
      </div>
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"최신 사진"} />
        </div>
        <PhotoSlider photoList={photoListRecent} />
      </div>
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"랜덤으로"} />
        </div>
        <PhotoSlider photoList={photoListRandom} />
      </div>
      {isOpen && <PhotoModal />}
    </div>
  );
}

export default Photo;

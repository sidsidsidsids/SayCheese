import "./Photo.css";
import PhotoCategory from "./PhotoCategory";
import PhotoSlider from "./PhotoSlider";
import sampleImg from "./assets/snoopy.png";
import { useSelector } from "react-redux";
import PhotoModal from "./PhotoModal";
function Photo() {
  const photoList1 = [
    {
      id: 0,
      author: "snoopy",
      imageLink: { sampleImg },
      loverCnt: 10,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 1,
      tags: ["귀엽게", "멋있게", "시크하게", "성숙하게"],
    },
    {
      id: 1,
      author: "snoopy2",
      imageLink: { sampleImg },
      loverCnt: 12,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 1,
      tags: ["신나게", "재밌게", "발랄하게", "차분하게"],
    },
    {
      id: 2,
      author: "snoopy3",
      imageLink: { sampleImg },
      loverCnt: 20,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 0,
      tags: ["멋있게", "성숙하게", "신나게", "발랄하게"],
    },
    {
      id: 3,
      author: "snoopy4",
      imageLink: { sampleImg },
      loverCnt: 100,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 1,
      tags: ["발랄하게", "멋있게", "재밌게", "귀엽게"],
    },
  ];
  const photoList2 = [
    {
      id: 4,
      author: "snoopy5",
      imageLink: { sampleImg },
      loverCnt: 10,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 1,
      tags: ["성숙하게", "발랄하게", "차분하게", "시크하게"],
    },
    {
      id: 5,
      author: "snoopy6",
      imageLink: { sampleImg },
      loverCnt: 12,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 1,
      tags: ["재밌게", "성숙하게", "신나게", "차분하게"],
    },
    {
      id: 6,
      author: "snoopy7",
      imageLink: { sampleImg },
      loverCnt: 20,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 0,
      tags: ["차분하게", "귀엽게", "시크하게", "신나게"],
    },
    {
      id: 7,
      author: "snoopy8",
      imageLink: { sampleImg },
      loverCnt: 100,
      createdDate: "2023-07-25T16:00:48",
      loverYn: 1,
      tags: ["멋있게", "재밌게", "차분하게", "신나게"],
    },
  ];

  // 스토어에서 모달이 열려있는지 확인하는 isOpen을 가져옴
  const { isOpen } = useSelector((store) => store.modal);

  return (
    <div className="PhotoBox">
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"가장 인기있는 사진"} />
        </div>
        <PhotoSlider photoList={photoList1} />
      </div>
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"최신 사진"} />
        </div>
        <PhotoSlider photoList={photoList2} />
      </div>
      {/* <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"컨셉 1 사z진"} />
        </div>
        <PhotoSlider />
      </div> */}
      {/* isOpen이 true일때 모달이 열립니다 */}
      {isOpen && <PhotoModal />}
    </div>
  );
}

export default Photo;

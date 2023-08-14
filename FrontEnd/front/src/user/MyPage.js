import { useEffect, useState } from "react";
import "./MyPage.css";
import MyProfile from "./MyProfile";
import MyPhotoCard from "./MyPhotoCard";
import MyPhotoNull from "./MyPhotoNull";
import MyPageModal from "./MyPageModal";
import sampleImg from "./assets/snoopy.png";
import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import WithdrawModal from "./WithdrawModal";
import axios from "axios";
import { Link } from "react-router-dom";
import MyFrameCard from "./MyFrameCard";

function MyPage() {
  const { isLogin, userInfo } = useSelector((store) => store.login);
  const { isOpen, modalContent } = useSelector((store) => store.modal);

  const { email } = useParams(); // 경로의 email
  const movePage = useNavigate();

  const [isWithdrawModalOpen, setIsWithdrawModalOpen] = useState(false); // 회원탈퇴 모달 열렸는지 여부 - true일 경우 열려있음
  const [myPhotoChange, setMyPhotochange] = useState(false);
  const [myPhotoList, setMyPhotoList] = useState([]);

  const [myFrameChange, setMyFrameChange] = useState(false);
  const [myFrameList, setMyFrameList] = useState([]);

  // useEffect(() => {
  //   // 로그인 안되어있거나 다른 회원의 주소로 접근할 경우
  //   if (!isLogin || !userInfo || userInfo.email !== email) {
  //     movePage("/"); // "/" 경로로 리다이렉션
  //   }
  // }, [isLogin, userInfo, email, movePage]);

  useEffect(() => {
    getMyPhotoList();
    setMyPhotochange(false);
    getMyFrameList();
    setMyFrameChange(false);
  }, [myPhotoChange, myFrameChange]);

  const handleWithdrawModalOpen = () => {
    setIsWithdrawModalOpen(true);
  };

  async function getMyPhotoList() {
    const accessToken = localStorage.getItem("accessToken");
    try {
      const response = await axios.get("/api/image", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
          Authorization: `${accessToken}`,
        },
      });
      setMyPhotoList(response.data.imageVoList);
    } catch (error) {
      console.log(error);
    }
  }

  async function getMyFrameList() {
    const accessToken = localStorage.getItem("accessToken");
    try {
      const response = await axios.get("/api/article/frame/my/list", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
          Authorization: `${accessToken}`,
        },
      });
      setMyFrameList(response.data.frameArticleVoList);
      console.log(response.data.frameArticleVoList);
    } catch (error) {
      console.log(error);
    }
  }

  // {!userInfo ? ( movePage("/") ) : ( 여기에 아래 리턴 내용 들어가야 함)
  return (
    <div>
      <div className="MyPageBox">
        <div className="MyProfileSort">
          <MyProfile
            email={userInfo.email}
            nickname={userInfo.nickname}
            genderFm={userInfo.genderFm}
            age={userInfo.age}
            name={userInfo.name}
            profile={userInfo.profile}
            payload={userInfo}
          />
          {/* isOpen이 true일때 MyPageModal 모달이 열림 */}
          {isOpen && <MyPageModal />}
          {/* isWithdrawModalOpen이 true일때 WithdrawModal 모달이 열림 */}
          {isWithdrawModalOpen && (
            <WithdrawModal close={() => setIsWithdrawModalOpen(false)} />
          )}
        </div>

        <div className="MyPageTitle">
          <h2>내가 찍은 사진</h2>
          <Link to={`/user/mypage/myphoto/${email}`}>
            <h3>더보기</h3>
          </Link>
          {/* <div>
            {myPhotoList.length > 3 && (
              <Link to={`/user/mypage/myphoto/${email}`}>
                <h3>더보기</h3>
              </Link>
            )}
          </div> */}
        </div>
        <div className="MyPagePhoto">
          {myPhotoList.slice(0, 3).map((item) => (
            <MyPhotoCard
              key={item.imageId}
              imageId={item.imageId}
              imageLink={item.imageLink}
              createdDate={item.createdDate}
              loverCnt={item.loverCnt}
              loverYn={item.loverYn}
              articleYn={item.articleYn}
              myPhotoChange={myPhotoChange}
              setMyPhotochange={setMyPhotochange}
              payload={item}
            />
          ))}
          {/* 마이페이지에서 내가 찍은 사진 3개 넘을 경우 3개만 보여주기 위함 */}
          {myPhotoList.length > 0 &&
            myPhotoList.length < 3 &&
            Array(3 - myPhotoList.length)
              .fill(null)
              .map((num, index) => <MyPhotoNull key={index} />)}
          {/* 내가 찍은 사진이 1개일 경우 <MyPhotoNull /> 2개 출력하고, 2개일 경우 <MyPhotoNull /> 1개 출력
            마이페이지에서 내가 찍은 사진 왼쪽 정렬 깔끔히 하기 위해 내용 없는 <MyPhotoNull /> 임의로 생성 */}
        </div>
        <div className="MyPageTitle">
          <h2>내가 만든 프레임</h2>
          {myFrameList.length > 3 && (
            <Link to={`/user/mypage/myframe/${email}`}>
              <h3>더보기</h3>
            </Link>
          )}
        </div>
        <div className="MyPagePhoto">
          {myFrameList.slice(0, 3).map((item) => (
            <MyFrameCard
              key={item.articleId}
              articleId={item.articleId}
              subject={item.subject}
              frameLink={item.frameLink}
              loverCnt={item.loverCnt}
              createdDate={item.createdDate}
              author={item.author}
              isPublic={item.isPublic}
              frameSpecification={item.frameSpecification}
              loverYn={item.loverYn}
              myFrameChange={myFrameChange}
              setMyFrameChange={setMyFrameChange}
            />
          ))}
          {myFrameList.length > 0 &&
            myFrameList.length < 3 &&
            Array(3 - myFrameList.length)
              .fill(null)
              .map((num, index) => <MyPhotoNull key={index} />)}
        </div>
        <div className="UserWithdrawSort">
          <p className="UserWithdrawBtn" onClick={handleWithdrawModalOpen}>
            회원 탈퇴
          </p>
        </div>
      </div>
    </div>
  );
}

export default MyPage;

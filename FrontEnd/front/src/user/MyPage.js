import { useEffect, useState } from "react";
// third party
import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate, Link } from "react-router-dom";
import axios from "axios";
// local
import "./MyPage.css";
import MyProfile from "./MyProfile";
import MyPhotoCard from "./MyPhotoCard";
import MyPhotoNull from "./MyPhotoNull";
import MyPageModal from "./MyPageModal";
import WithdrawModal from "./WithdrawModal";
import MyFrameCard from "./MyFrameCard";
import { getUserInfo } from "../redux/features/login/loginSlice";

function MyPage() {
  const { isLogin, userInfo } = useSelector((store) => store.login);
  const { isOpen, modalContent } = useSelector((store) => store.modal);

  const { email } = useParams(); // 경로의 email

  const [isWithdrawModalOpen, setIsWithdrawModalOpen] = useState(false); // 회원탈퇴 모달 열렸는지 여부 - true일 경우 열려있음
  const [myPhotoChange, setMyPhotochange] = useState(false); // 내 사진 목록이 변경(삭제)되었는지 여부 - true일 경우 변경됨
  const [myPhotoList, setMyPhotoList] = useState([]); // 내 사진 목록

  const [myFrameChange, setMyFrameChange] = useState(false); // 내 프레임 목록이 변경(삭제)되었는지 여부 - true일 경우 변경됨
  const [myFrameList, setMyFrameList] = useState([]); // 내 프레임 목록

  const [profileChanged, setProfileChanged] = useState(false); // 내 프로필 사진이 변경되었는지 여부 - true일 경우 변경됨

  const movePage = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    // 로그인 안되어있거나 다른 회원의 주소로 접근할 경우
    if (!isLogin || !userInfo || userInfo.email !== email) {
      movePage("/main"); // "/main" 경로로 리다이렉션
    }
  }, [isLogin, userInfo, email, movePage]);

  useEffect(() => {
    // 내 사진, 내 프레임 또는 내 프로필이 변경되었을 경우
    getMyPhotoList();
    setMyPhotochange(false);
    getMyFrameList();
    setMyFrameChange(false);
    dispatch(getUserInfo());
    setProfileChanged(false);
  }, [myPhotoChange, myFrameChange, profileChanged]);

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
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <div>
      {!userInfo ? (
        // 로그인 하지 않은 사용자가 접근하는 경우 main 페이지로 리다이렉트
        movePage("/main")
      ) : (
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
            {isOpen && (
              <MyPageModal
                profileChanged={profileChanged}
                setProfileChanged={setProfileChanged}
              />
            )}
            {/* isWithdrawModalOpen이 true일때 WithdrawModal 모달이 열림 */}
            {isWithdrawModalOpen && (
              <WithdrawModal close={() => setIsWithdrawModalOpen(false)} />
            )}
          </div>

          <div className="MyPageTitle">
            <h2>내가 찍은 사진</h2>

            <div>
              {myPhotoList.length > 3 && (
                <Link to={`/user/mypage/myphoto/${email}`}>
                  <h3>더보기</h3>
                </Link>
              )}
            </div>
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
      )}
    </div>
  );
}

export default MyPage;

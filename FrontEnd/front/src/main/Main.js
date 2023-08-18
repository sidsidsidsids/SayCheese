import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import RoomCreateModal from "./RoomCreateModal";
import RoomJoinModal from "./RoomJoinModal";
import SetNameModal from "./SetNameModal";
import Loading from "../Loading";
import leftBooth from "./assets/booth(left).png";
import rightBooth from "./assets/booth(right).png";
import sign from "./assets/sign.png";
import shelf from "./assets/shelf.png";
import cs from "./assets/curtain stick.png";
import cy1 from "./assets/curtain yellow1.png";
import cy2 from "./assets/curtain yellow2.png";
import cg1 from "./assets/curtain green1.png";
import cg2 from "./assets/curtain green2.png";
import login from "./assets/login.png";
import photo from "./assets/photo.png";
import frame from "./assets/frame.png";
import notice from "./assets/notice.png";
import rabbit from "./assets/rabbit.png";
import fox1 from "./assets/cute fox(1).png";
import hat from "./assets/hat.png";
import witch from "./assets/witch hat.png";
import l_Frame from "./assets/ladder_shape.svg";
import w_Frame from "./assets/window_shape.png";

import "./Main.css";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { getUserInfo } from "../redux/features/login/loginSlice";

let able;
function Main() {
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [joinModalOpen, setJoinModalOpen] = useState(false);
  const [nameModalOpen, setNameModalOpen] = useState(false);
  const [toCreate, setToCreate] = useState(false);
  const [toJoin, setToJoin] = useState(false);
  const { userInfo, isLogin } = useSelector((store) => store.login);
  const [loginLink, setLoginLink] = useState("/user/login");

  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getUserInfo());
    // able = checkAvailable();

    if (userInfo) {
      // 로그인 되어있으면 마이페이지로 링크 연결합니다
      setLoginLink(`/user/mypage/${userInfo.email}`);
    }
  }, []);

  const defaultOptions = {
    reverse: false, // reverse the tilt direction
    max: 35, // max tilt rotation (degrees)
    perspective: 1000, // Transform perspective, the lower the more extreme the tilt gets.
    speed: 1000, // Speed of the enter/exit transition
    transition: true, // Set a transition on enter/exit.
    axis: null, // What axis should be disabled. Can be X or Y.
    reset: true, // If the tilt effect has to be reset on exit.
    easing: "cubic-bezier(.03,.98,.52,.99)", // Easing on enter/exit.
  };

  return (
    <div id="mainWrapper">
      <img id="sign" src={sign} alt="sign" />
      <div id="mainBooth">
        <div id="left">
          <div className="leftPoster">
            게임촬영
            <br />
            일반촬영
            <br />
            0원
          </div>
          <img className="sticker2" src={l_Frame} alt="l_Frame" />
          <img className="sticker1" src={l_Frame} alt="l_Frame" />
          <img id="leftBooth" src={leftBooth} alt="left-booth" />
          <img src={cs} className="curtainPipe" alt="curtain pipe" />
          <div className="curtains">
            <div className="glow leftGlow">ON AIR</div>
            <div
              className="createRoom"
              onClick={() => {
                if (userInfo) {
                  setCreateModalOpen(true);
                } else {
                  setNameModalOpen(true);
                  setToCreate(true);
                }
              }}
            >
              방 생성
            </div>
            <img src={cy1} className="curtain left5" alt="curtain1 left5"></img>
            <img src={cy2} className="curtain left4" alt="curtain2 left4"></img>
            <img src={cy1} className="curtain left3" alt="curtain1 left3"></img>
            <img src={cy2} className="curtain left2" alt="curtain2 left2"></img>
            <img src={cy1} className="curtain left1" alt="curtain1 left1"></img>
          </div>
        </div>
        <div id="mainMenu">
          <img id="shelf" src={shelf} alt="shelf"></img>
          <Link to={loginLink}>
            {/* 로그인 되어있다면 마이페이지 안 되어 있다면 로그인 페이지로 이동 */}
            <img id="login" src={login} alt="login"></img>
          </Link>
          <Link to="/photo">
            <img id="photo" src={photo} alt="photo"></img>
          </Link>
          <Link to="/frame/">
            <img id="frame" src={frame} alt="frame"></img>
          </Link>
          <Link to="/customercenter/notice">
            <img id="notice" src={notice} alt="notice"></img>
          </Link>
          <img id="hat" src={hat} alt="hat"></img>
          <img id="witch" src={witch} alt="witch"></img>
          <img id="rabbit" src={rabbit} alt="rabbit"></img>
          <img id="fox1" src={fox1} alt="fox1"></img>
        </div>
        <div id="right">
          <img id="rightBooth" src={rightBooth} alt="right-booth"></img>
          <div className="rightPoster">
            1 + 1
            <br />
            영구소장
            <br />
            0원
          </div>
          <img className="sticker3" src={l_Frame} alt="l_Frame"></img>
          <img className="sticker4" src={l_Frame} alt="l_Frame"></img>
          <img className="sticker5" src={w_Frame} alt="w_Frame"></img>
          <img
            src={cs}
            className="curtainPipe right"
            alt="curtain pipe right"
          ></img>
          <div className="curtains right">
            <div className="glow rightGlow">ON AIR</div>
            <div
              className="createRoom"
              onClick={() => {
                if (userInfo) {
                  setJoinModalOpen(true);
                } else {
                  setNameModalOpen(true);
                  setToJoin(true);
                }
              }}
            >
              방 입장
            </div>
            <img
              src={cg1}
              className="curtain right1"
              alt="curtain1 left5"
            ></img>
            <img
              src={cg2}
              className="curtain right2"
              alt="curtain2 left4"
            ></img>
            <img
              src={cg1}
              className="curtain right3"
              alt="curtain1 left3"
            ></img>
            <img
              src={cg2}
              className="curtain right4"
              alt="curtain2 left2"
            ></img>
            <img
              src={cg1}
              className="curtain right5"
              alt="curtain1 right1"
            ></img>
          </div>
        </div>
      </div>
      <SetNameModal
        open={nameModalOpen}
        close={() => {
          setNameModalOpen(false);
          if (toCreate) {
            setCreateModalOpen(true);
            setToCreate(false);
          } else {
            if (toJoin) {
              setJoinModalOpen(true);
              setToJoin(false);
            }
          }
        }}
        // onConfirm={(inputNickname) => {
        //   dispatch(notUserNickname(inputNickname));
        // }}
      />
      <RoomCreateModal
        open={createModalOpen}
        close={() => setCreateModalOpen(false)}
      />
      <RoomJoinModal
        open={joinModalOpen}
        close={() => setJoinModalOpen(false)}
      />
    </div>
  );
}
export default Main;

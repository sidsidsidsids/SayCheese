import { useState, useEffect, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { OpenVidu } from "openvidu-browser";
import html2canvas from "html2canvas";
import axios from "axios";
import "./Room.css";
import RoomButtons from "./RoomButtons";
import RoomFooter from "./RoomFooter";
import RoomHeader from "./RoomHeader";
import RoomPhoto from "./RoomPhoto";
import UserVideoComponent from "./UserVideoComponent";
import TargetVideoComponent from "./TargetVideoComponent";
import ChatComponent from "./ChatComponent";
import Timer from "./Timer";

import sampleImage from "./assets/sample.jpg";

const APPLICATION_SERVER_URL = "https://i9a401.p.ssafy.io/";
const APPLICATION_SERVER_SECRET = "my_secret";
var chatData;
var sessionConnectId;
var joinUsers;
var locationX;
var locationY;
var iamHost = false;
var photoData;
const Room = () => {
  const params = useParams();
  const navigate = useNavigate();

  const [mySessionId, setMySessionId] = useState(params.id);
  const [myUserName, setMyUserName] = useState(
    "test" + Math.floor(Math.random() * 100)
  );
  const [isHost, setHost] = useState(false);
  const [session, setSession] = useState(undefined);
  const [mainStreamManager, setMainStreamManager] = useState(undefined);
  const [publisher, setPublisher] = useState(undefined);
  const [subscribers, setSubscribers] = useState([]);

  const [roomStatus, setRoomStatus] = useState(0);
  const [Minutes, setMinutes] = useState(0);
  const [Seconds, setSeconds] = useState(0);
  const [isTimeReset, setIsTimeReset] = useState(false);

  useEffect(() => {
    window.addEventListener("beforeunload", onbeforeunload);
    joinSession();
    return () => {
      window.removeEventListener("beforeunload", onbeforeunload);
    };
  }, [params.id]);

  useEffect(() => {
    joinUsers = [publisher, ...subscribers];
  }, [publisher, subscribers]);

  const onbeforeunload = (event) => {
    leaveSession();
  };

  const deleteSubscriber = (streamManager) => {
    setSubscribers((prevSubscribers) =>
      prevSubscribers.filter((sub) => sub !== streamManager)
    );
  };

  const joinSession = () => {
    const OV = new OpenVidu();
    setSession(OV.initSession());
    const mySession = OV.initSession();

    console.log(mySession);

    mySession.on("streamCreated", (event) => {
      const subscriber = mySession.subscribe(event.stream, undefined);
      setSubscribers((prevSubscribers) => [...prevSubscribers, subscriber]);
    });

    mySession.on("streamDestroyed", (event) => {
      deleteSubscriber(event.stream.streamManager);
      if (!isHost) {
        updateHost(mySessionId);
      }
    });

    mySession.on("exception", (exception) => {
      console.warn(exception);
    });

    mySession.on("startGameMode", (event) => {
      console.log(event);
      setRoomStatus(1);
      gameMode();
    });

    mySession.on("updatePhoto", (event) => {
      console.log(event);
    });

    getToken().then((token) => {
      mySession
        .connect(token)
        .then(async () => {
          // 채팅창에 보낼 데이터셋
          chatData = mySession;

          console.log(mySession, mySessionId);
          const publisher = await OV.initPublisherAsync(undefined, {
            audioSource: undefined,
            videoSource: undefined,
            publishAudio: false,
            publishVideo: true,
            resolution: "320x240",
            frameRate: 30,
            insertMode: "APPEND",
            mirror: false,
          });
          console.log(publisher);
          mySession.publish(publisher);
          // 방에 유저 정보 보낼 곳

          // 카메라 선택 옵션 넣을 때 사용됨
          const devices = await OV.getDevices();
          // const videoDevices = devices.filter((device) => device.kind === "videoinput");
          // const currentVideoDeviceId = publisher.stream.getMediaStream().getVideoTracks()[0].getSettings().deviceId;
          // const currentVideoDevice = videoDevices.find((device) => device.deviceId === currentVideoDeviceId);

          setPublisher(publisher);
          // Connection ID 저장용
          sessionConnectId = publisher.session.connection.connectionId;
        })
        .catch((error) => {
          console.log(
            "There was an error connecting to the session:",
            error.code,
            error.message
          );
        });
    });
  };

  const leaveSession = () => {
    if (session) {
      session.disconnect();
    }

    setSession(undefined);
    setSubscribers([]);
    setMainStreamManager(undefined);
    setPublisher(undefined);
  };

  const getToken = async () => {
    return sessionCheck(mySessionId).then((sessionExists) => {
      if (sessionExists) {
        console.log("참가자로");
        return createToken(mySessionId);
      } else {
        console.log("방장으로");
        setHost(true);
        iamHost = true;
        return createSession(mySessionId).then((session) =>
          createToken(session.sessionId)
        );
      }
    });
  };
  const sessionCheck = async (sessionId) => {
    try {
      const response = await axios.get(
        APPLICATION_SERVER_URL + "openvidu/api/sessions/" + sessionId,
        {
          headers: {
            Authorization: `Basic ${btoa(
              `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
            )}`,
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,POST",
          },
        }
      );

      // Check the response status and return true or false accordingly
      if (response.status === 200) {
        return true;
      } else if (response.status === 404) {
        return false;
      } else {
        return false;
      }
    } catch (error) {
      // If there was an error, return false
      console.error("Error checking session:", error);
      return false;
    }
  };
  const createSession = async (sessionId) => {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "openvidu/api/sessions",
      { customSessionId: sessionId },
      {
        headers: {
          Authorization: `Basic ${btoa(
            `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
          )}`,
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET,POST",
        },
      }
    );
    return response.data;
  };

  const createToken = async (sessionId) => {
    const response = await axios.post(
      APPLICATION_SERVER_URL +
        "openvidu/api/sessions/" +
        sessionId +
        "/connection",
      {},
      {
        headers: {
          Authorization: `Basic ${btoa(
            `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
          )}`,
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET,POST",
        },
      }
    );
    return response.data.token;
  };

  const updateHost = async (sessionId) => {
    try {
      const response = await axios.get(
        APPLICATION_SERVER_URL +
          "openvidu/api/sessions/" +
          sessionId +
          "/connection",
        {
          headers: {
            Authorization: `Basic ${btoa(
              `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
            )}`,
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,POST",
          },
        }
      );

      let content = response.data.content;
      let sortByTime = await content.sort((a, b) => a.createdAt - b.createdAt);
      let hostConnectionId = sortByTime[0].connectionId;
      console.log(hostConnectionId);
      if (hostConnectionId === sessionConnectId) {
        console.log("방장되기");
        setHost(true);
        iamHost(true);
      }
    } catch (error) {
      console.error(error);
    }
  };
  // 화면 캡쳐 func
  const handleCapture = (locationX, locationY) => {
    // 비디오 요소 가져오기
    let video = document.querySelector("video");

    // 비디오 일시 중지 (의도하지 않은 결과물 가운데 출력 막기)
    if (video) {
      video.pause();
      video.style.display = "none";
    }

    let range = document.querySelector(".room-main");
    // html2canvas를 사용하여 .room-main 요소를 캡처
    html2canvas(range, { scale: 6, backgroundColor: null }).then((canvas) => {
      let ctx = canvas.getContext("2d");
      // 캔버스에 비디오를 그립니다.
      ctx.drawImage(
        video,
        0, // 비디오를 캔버스의 어떤 X 위치에 그릴지
        0, // 비디오를 캔버스의 어떤 y 위치에 그릴지
        320, // 비디오의 원래 width
        240, // 비디오의 원래 height
        500 + locationX, // 캔버스에 그릴 때 시작점 x 위치
        220 + locationY, // 캔버스에 그릴 때 시작점 y 위치
        240, // 캔버스에 그릴 비디오의 width
        180 // 캔버스에 그릴 비디오의 height
      );

      // 캔버스의 데이터 URL을 얻어 이미지로 사용할 수 있습니다.
      const dataURL = canvas.toDataURL("image/png");
      // console.log(dataURL);
      // range.style.backgroundImage = `url(${dataURL})`;

      // 이미지를 다른 요소에 적용하려면 아래와 같이 설정합니다.
      // const image = new Image();
      // image.src = dataURL;
      // document.body.appendChild(image);

      // 비디오를 다시 재생합니다.
      if (video) {
        video.style.display = "flex";
        video.play();
      }
      photoData = dataURL;
    });
  };

  const gameMode = () => {
    console.log("GAMEMODE");
    // 방 상태 설정
    setRoomStatus(1);
    // 유저 순서 설정(without RoomServer ver)
    // 백엔드와 연동 이후 userStreamIds ~ targetUsers 부분 삭제할 것
    const userStreamIds = joinUsers.map((user) => user.stream.streamId);
    const targetUsers = [...userStreamIds];
    if (targetUsers.length === 3) {
      targetUsers.push(targetUsers[0]);
    } else if (targetUsers.length === 2) {
      targetUsers.push(...targetUsers);
    } else if (targetUsers.length === 1) {
      targetUsers.push(...targetUsers, ...targetUsers, ...targetUsers);
    }
    // 유저 정보
    // const Users = [publisher, ...subscribers];
    const Users = joinUsers;

    // gameMode 로직
    const gamePhase = (count) => {
      // 4번 다 돌았을 때
      if (count === 0) {
        console.log("finish");
        setRoomStatus(2);
        return;
      } // 4번 다 돌기 전에는 이를 반복
      else {
        console.log(`${count} times left`);
        // 페이즈에 들어서며 일어날 일 (캠 메인에 띄우기/주제 출력)
        Users.forEach((user) => {
          console.log(user.stream.streamId, targetUsers[count]);
          if (
            user &&
            user.stream &&
            user.stream.streamId === targetUsers[count - 1]
          ) {
            // 해당 유저를 MainPublisher로 설정합니다.
            setMainStreamManager(user);
          }
        });
        // 캠 위치 설정
        if (count % 2 === 0) {
          locationX = 150;
        } else {
          locationX = -150;
        }
        if (count > 2) {
          locationY = -120;
        } else {
          locationY = 120;
        }
        // 초기 시간 설정 (setTimeout 맨 마지막 값과 동일시)
        setMinutes(0);
        setSeconds(2);
        setTimeout(() => {
          // x000ms 후에 일어날 일 (사진 찍기/주제 변경/유저 변경)
          handleCapture(locationX, locationY);
          console.log(photoData);
          if (iamHost) {
            handlePhoto(mySessionId, photoData);
          }
          setMainStreamManager(undefined);
          setMinutes(0);
          setSeconds(0);
          setTimeout(() => {
            // 1000ms 후에 일어날 일(중간 텀), 이 이후 다시 else문
            setMinutes(0);
            setSeconds(2);
            gamePhase(count - 1);
          }, 3000);
        }, 2000);
      }
    };
    gamePhase(4);
  };

  const handleGameMode = async (sessionId) => {
    try {
      const request = await axios.post(
        APPLICATION_SERVER_URL + "openvidu/api/signal",
        {
          session: sessionId,
          to: [],
          type: "startGameMode",
          data: "start game",
        },
        {
          headers: {
            Authorization: `Basic ${btoa(
              `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
            )}`,
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,POST",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.error("Error checking session:", error);
    }
  };

  const handlePhoto = async (sessionId, photoURL) => {
    try {
      console.log(photoURL);
      const request = await axios.post(
        APPLICATION_SERVER_URL + "openvidu/api/signal",
        {
          session: sessionId,
          to: [],
          type: "updatePhoto",
          data: photoURL,
        },
        {
          headers: {
            Authorization: `Basic ${btoa(
              `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
            )}`,
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,POST",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.error("Error checking session:", error);
    }
  };

  return (
    <div className="room">
      {roomStatus === 2 ? (
        <div className="room-active">
          <div className="room-top">
            <RoomHeader status={roomStatus} />
            <RoomButtons
              onConfirm={() => {
                navigate("/");
              }}
              onClose={() => {
                setRoomStatus(1);
              }}
              buttonName1="나가기"
              buttonName2="사진 공유"
              option1={false}
              option2={!isHost}
            />
          </div>
          <div className="room-mid">
            {/* <RoomPhoto /> */}
            <div
              className="room-main"
              style={{
                backgroundImage: `url('${sampleImage}')`,
                backgroundSize: "contain",
                backgroundRepeat: "no-repeat",
                backgroundPosition: "center",
              }}
            ></div>
            {chatData && <ChatComponent user={chatData} />}
          </div>
          <div className="room-bot">
            <RoomFooter status={roomStatus} />
            <Timer minutes="5" seconds="0" />
          </div>
        </div>
      ) : roomStatus === 1 ? (
        <div className="room-active">
          <div className="room-top">
            <RoomHeader status={roomStatus} />
            <RoomButtons
              onConfirm={() => {
                setRoomStatus(2);
              }}
              onClose={() => {
                setRoomStatus(0);
              }}
              buttonName1="촬영하기"
              buttonName2="다시 찍기"
              option1={!isHost}
              option2={!isHost}
            />
          </div>
          <div className="room-mid">
            {/* <RoomPhoto /> */}
            <div
              className="room-main"
              style={{
                backgroundImage: `url('${sampleImage}')`,
                backgroundSize: "contain",
                backgroundRepeat: "no-repeat",
                backgroundPosition: "center",
              }}
            >
              {mainStreamManager && (
                <TargetVideoComponent
                  streamManager={mainStreamManager}
                  locationX={locationX}
                  locationY={locationY}
                />
              )}
            </div>
            {chatData && <ChatComponent user={chatData} myName={myUserName} />}
          </div>
          <div className="room-bot">
            {/* <RoomFooter status={roomStatus} /> */}
            <div className="video-container">
              <UserVideoComponent
                streamManager={publisher}
                myName={myUserName}
              />
              {subscribers.map((sub, i) => (
                <div
                  key={sub.id}
                  className="stream-container col-md-6 col-xs-6"
                >
                  <span>{sub.id}</span>
                  <UserVideoComponent streamManager={sub} />
                </div>
              ))}
            </div>
            <Timer minutes={Minutes} seconds={Seconds} />
          </div>
        </div>
      ) : (
        <div className="room-active">
          <div className="room-top">
            <RoomHeader status={roomStatus} />
            <RoomButtons
              onConfirm={() => {
                console.log(session);
                handleGameMode(mySessionId);

                console.log("COMPLE");
              }}
              onClose={() => {
                console.log("pub: ", publisher);
                console.log("sub: ", subscribers);
                console.log("session: ", session);
                console.log("connecionId: ", sessionConnectId);
              }}
              buttonName1="시작하기"
              buttonName2="초대 링크"
              option1={!isHost}
              option2={!isHost}
            />
          </div>
          <div className="room-mid">
            <div className="room-main">
              <UserVideoComponent
                streamManager={publisher}
                myName={myUserName}
              />
              {subscribers.map((sub, i) => (
                <div key={i} className="stream-container col-md-6 col-xs-6">
                  <UserVideoComponent streamManager={sub} />
                </div>
              ))}
            </div>
            {chatData && <ChatComponent user={chatData} myName={myUserName} />}
          </div>
          <div className="room-bot">
            <RoomFooter status={roomStatus} />
          </div>
        </div>
      )}
    </div>
  );
};

export default Room;

import { useState, useEffect, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { OpenVidu } from "openvidu-browser";
import html2canvas from "html2canvas";
import axios from "axios";
import "./Room.css";
import RoomButtons from "./RoomButtons";
import RoomHeader from "./RoomHeader";
import UserVideoComponent from "./UserVideoComponent";
import TargetVideoComponent from "./TargetVideoComponent";
import ChatComponent from "./ChatComponent";
import Timer from "./Timer";

import sampleImage from "./assets/sample.jpg";

const APPLICATION_SERVER_SECRET = "my_secret";
let chatData;
let sessionConnectId;
let joinUsers;
let locationX;
let locationY;
let resultSrc;
let resultData;
// const accessToken = localStorage.getItem("accessToken");
const Room = () => {
  const params = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  // openvidu 관련
  const [mySessionId, setMySessionId] = useState(params.id);
  const [myUserName, setMyUserName] = useState(
    "test" + Math.floor(Math.random() * 100)
  );
  const [session, setSession] = useState(undefined);
  const [mainStreamManager, setMainStreamManager] = useState(undefined);
  const [publisher, setPublisher] = useState(undefined);
  const [subscribers, setSubscribers] = useState([]);
  // room 관련
  const [isHost, setHost] = useState(false);
  const [roomStatus, setRoomStatus] = useState(0);
  const [Minutes, setMinutes] = useState(0);
  const [Seconds, setSeconds] = useState(0);

  useEffect(() => {});
  useEffect(() => {
    window.addEventListener("beforeunload", onbeforeunload);
    joinSession();
    userJoin(mySessionId);
    return () => {
      window.removeEventListener("beforeunload", onbeforeunload);
    };
  }, [params.id]);

  useEffect(() => {
    joinUsers = [publisher, ...subscribers];
  }, [publisher, subscribers]);

  useEffect(() => {
    if (roomStatus === 2) {
      if (isHost) {
        // sendImageData(resultSrc, mySessionId);
      }
      setTimeout(async () => {
        try {
          userDelete();
          if (isHost) {
            roomDelete(mySessionId);
          }
          navigate("/");
        } catch (error) {
          console.log(error);
        }
      }, 300000);
    }
  }, [roomStatus]);

  useEffect(() => {
    if (isHost) {
      hostPost(mySessionId);
    }
  }, [isHost]);

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
      gameMode();
    });

    mySession.on("startNormalMode", (event) => {
      console.log(event);
      setRoomStatus(1);
      // normalMode();
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
          userStream(mySessionId, publisher.stream.streamId);
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
    userDisconnection(mySessionId, myUserName);
    if (session) {
      session.disconnect();
    }

    setMySessionId(undefined);
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
        return createSession(mySessionId).then((session) =>
          createToken(session.sessionId)
        );
      }
    });
  };
  const sessionCheck = async (sessionId) => {
    try {
      const response = await axios.get("/openvidu/api/sessions/" + sessionId, {
        headers: {
          Authorization: `Basic ${btoa(
            `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
          )}`,
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET,POST",
        },
      });

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
      "/openvidu/api/sessions",
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
      "/openvidu/api/sessions/" + sessionId + "/connection",
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

  // 유저 정보 방 입장 시 전달
  const userJoin = async (sessionId) => {
    console.log("USERJOIN");
    try {
      const request = await axios.post(
        "/api/room/participant",
        {
          ownerYn: "N",
          roomCode: sessionId,
        },
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 유저 삭제 (재접속할 일 없음)
  const userDelete = async () => {
    try {
      const request = await axios.delete(
        "/api/room/participant",
        {},
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 유저와 연결 끊김 (재접속할 수 있음)
  const userDisconnection = async (sessionId, myName) => {
    console.log("USERSTREAM");
    try {
      const request = await axios.put(
        "/api/room/participant/fail/connection/" + sessionId,
        {
          nickname: myName,
        },
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 유저의 화상 ID(streamId) 전달
  const userStream = async (sessionId, streamId) => {
    console.log("USERSTREAM");
    try {
      const request = await axios.put(
        "/api/room/participant/" + sessionId + "/streamId",
        {
          streamId: streamId,
        },
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 방 종료
  const roomDelete = async (sessionId) => {
    try {
      const request = await axios.delete(
        "/api/room",
        {
          roomCode: sessionId,
        },
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 새 방장 갱신
  const updateHost = async (sessionId) => {
    console.log("UPDATEHOST");
    try {
      const response = await axios.get(
        "/openvidu/api/sessions/" + sessionId + "/connection",
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
      }
    } catch (error) {
      console.error(error);
    }
  };
  // 방장 정보 갱신
  const hostPost = async (sessionId) => {
    console.log("HOSTPOST");
    try {
      const request = await axios.post(
        "/api/room/participant/" + sessionId + "/owner",
        {
          roomCode: sessionId,
        },
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 사진 캡처(사진 찍기)
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
    html2canvas(range, { scale: 4, backgroundColor: null }).then((canvas) => {
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
      range.style.backgroundImage = `url(${dataURL})`;
      // 이미지를 다른 요소에 적용하려면 아래와 같이 설정합니다.
      // const image = new Image();
      // image.src = dataURL;
      // document.body.appendChild(image);
      // 비디오를 다시 재생합니다.
      if (video) {
        video.style.display = "flex";
        video.play();
      }
    });
  };
  // 게임모드
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
        const range = document.querySelector(".room-main");
        const imageURL = range.style.backgroundImage;
        // resultSrc = imageURL;
        const imageURLCleaned = imageURL.replace(
          /url\(['"]?(.*?)['"]?\)/,
          "$1"
        );
        resultSrc = imageURLCleaned;
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
        setSeconds(1);
        setTimeout(() => {
          // 500ms 후에 일어날 일 (사진 찍기/주제 변경/유저 변경)
          handleCapture(locationX, locationY);
          setMainStreamManager(undefined);
          setMinutes(0);
          setSeconds(0);
          setTimeout(() => {
            // 1000ms 후에 일어날 일(중간 텀), 이 이후 다시 else문
            setMinutes(0);
            setSeconds(1);
            gamePhase(count - 1);
          }, 500);
        }, 1000);
      }
    };
    gamePhase(4);
  };
  // 방 유저 전체 게임모드 돌입
  const handleStartGameMode = async (sessionId) => {
    try {
      const request = await axios.post(
        "/openvidu/api/signal",
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
  // 방 유저 전체 일반모드 돌입
  const handleStartNormalMode = async (sessionId) => {
    try {
      const request = await axios.post(
        "/openvidu/api/signal",
        {
          session: sessionId,
          to: [],
          type: "startNormalMode",
          data: "start normal",
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
  // 결과창에서 DB에 이미지 저장
  const sendImageData = async (resultURL, sessionId) => {
    console.log("SEND");
    try {
      await axios
        .post(
          "/api/amazon/presigned",
          {
            fileName: `${sessionId}.jpg`,
            fileType: "image",
          },
          {
            headers: {
              Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
              "Content-Type": "application/json;charset=UTF-8",
            },
          }
        )
        .then(async (response) => {
          console.log("IMAGE");
          console.log(response.data.fileName);
          console.log(response.data.preSignUrl);
          const imageBlob = await fetch(resultURL).then((response) =>
            response.blob()
          );
          console.log(imageBlob);
          const imageFile = new File([imageBlob], response.data.fileName, {
            type: "image/jpg",
          });
          console.log(imageFile);
          const formData = new FormData();
          formData.append(response.data.fileName, imageFile);
          // console.log(formData)

          for (let value of formData.values()) {
            console.log(value);
          }
          await axios
            .put(
              response.data.preSignUrl,
              {
                formData,
              },
              {
                headers: {
                  Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
                  "Content-Type": "image/jpg",
                },
              }
            )
            .then((response) => {
              console.log("DATA");
              console.log(response);
              resultData = response.data;
            });
        });
    } catch (error) {
      alert(error);
      console.error("Error:", error);
      return;
    }
  };
  // 결과창 사진 공유 버튼 눌렀을 때
  const handleImageUpload = async (imageId) => {
    try {
      const request = await axios.post(
        "/api/article/image",
        {
          imageId: imageId,
        },
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjIifQ.OUP4gSxVM-CV0JeNYLxTtbwY9B0YGuS1PYjss9X0y5a9Q61g7Gjb43RsTVTK7L-EVhPvHS-DuUBN9Chy2SLgVg`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      alert("업로드 실패");
      console.error("Error:", error);
    }
  };
  // 결과창 사진 다운
  const handleDownload = () => {
    let range = document.querySelector(".room-main");
    console.log(range);
    const imageURL = range.style.backgroundImage;
    console.log(imageURL);
    const imageURLCleaned = imageURL.replace(/url\(['"]?(.*?)['"]?\)/, "$1");
    // const imageElement = document.createElement("img");
    // imageElement.src = imageURLCleaned;
    const downloadLink = document.createElement("a");
    downloadLink.href = imageURLCleaned;
    downloadLink.download = `${sessionConnectId}.png`; // 원하는 파일명 설정
    // 다운로드 링크 클릭 이벤트 트리거
    downloadLink.click();
  };

  return (
    <div className="room">
      {roomStatus === 2 ? (
        <div className="room-active">
          <div className="room-top">
            <RoomHeader status={roomStatus} />
            <RoomButtons
              onButton1={() => {
                navigate("/");
              }}
              onButton2={() => {
                handleDownload();
              }}
              onButton3={() => {
                handleImageUpload("4");
              }}
              buttonName1="나가기"
              buttonName2="사진 다운"
              buttonName3="사진 공유"
              option1={false}
              option2={false}
              option3={!isHost}
            />
          </div>
          <div className="room-mid">
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
            <Timer minutes="5" seconds="0" />
          </div>
        </div>
      ) : roomStatus === 1 ? (
        <div className="room-active">
          <div className="room-top">
            <RoomHeader status={roomStatus} />
            <RoomButtons
              onButton1={() => {}}
              onButton2={() => {
                setRoomStatus(2);
              }}
              onButton3={() => {
                setRoomStatus(0);
              }}
              buttonName1="나가기"
              buttonName2="촬영하기"
              buttonName3="다시 찍기"
              option1={true}
              option2={!isHost}
              option3={!isHost}
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
              onButton1={() => {
                navigate("/");
              }}
              onButton2={() => {
                handleStartGameMode(mySessionId);
              }}
              onButton3={() => {
                console.log("pub: ", publisher);
                console.log("sub: ", subscribers);
                console.log("session: ", session);
                console.log("connecionId: ", sessionConnectId);
              }}
              buttonName1="나가기"
              buttonName2="시작하기"
              buttonName3="초대 링크"
              option2={!isHost}
              option3={!isHost}
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
            <div className="frame-container"></div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Room;

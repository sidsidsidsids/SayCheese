import { useState, useEffect, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { setRoom } from "../redux/features/room/roomSlice";
import { getUserInfo } from "../redux/features/login/loginSlice";
import { OpenVidu } from "openvidu-browser";
import html2canvas from "html2canvas";
import axios from "axios";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import "./Room.css";
import RoomButtons from "./RoomButtons";
import UserVideoComponent from "./UserVideoComponent";
import TargetVideoComponent from "./TargetVideoComponent";
import ChatComponent from "./ChatComponent";
import Timer from "./Timer";
import sampleImage from "./assets/sample.jpg";
import logo from "./assets/cheese.png";
import horTest from "./assets/horTest.png";
import verTest from "./assets/verTest.png";

const APPLICATION_SERVER_SECRET = "my_secret";

let chatData; // 채팅창에 보낼 데이터
let sessionStreamId; // 세션 스트림 ID 저장할 변수
let sessionConnectId; // 새션 연결 ID 저장할 변수
let joinUsers; // 참가자 저장 변수
let locationX; // 사진 위치 할당 변수
let locationY; // 사진 위치 할당 변수
let resultSrc; // 결과 이미지 URL 저장 변수
let tempSrc; // 중간 이미지 URL 저장 변수
let normalCnt = 0; // 일반 모드 촬영 횟수 저장 변수
let gameCnt = 0; // 게임 모드 촬영 횟수 저장 변수
// let randomTags; // 무작위 태그 리스트들 저장 변수
let randomTags = ["sa", "ds", "cx", "vcx"];
let randomTag; // 무작위 태그 추출하여 저장할 변수
let tagResult = []; // 태그 결과물 저장 변수
let frameSearchInput = "";
let roomImageID; // 최종 결과물 ID 저장 변수
let chosenUrl; // 프레임 url
let orders;
let horizontalLeftLoc = [32, 261, 32, 261];
let horizontalTopLoc = [29, 29, 205, 205];
let verticalLeftLoc = [19, 19, 19, 19];
let verticalTopLoc = [19, 139, 259, 379];
let videoFrameSizes = {
  VERTICAL: {
    width: "207.8px",
    height: "589.6px",
  },
  HORIZONTAL: {
    width: "589.6px",
    height: "400.6px",
  },
};
let videoFrameWidth = "207.8px";
let videoFrameHeight = "589.6px";
const accessToken = localStorage.getItem("accessToken");
const Room = () => {
  const params = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { userInfo } = useSelector((store) => store.login);
  const { roomInfo } = useSelector((store) => store.room);
  // let selectedMode = roomInfo ? roomInfo.mode : undefined;
  let selectedMode = roomInfo ? roomInfo.mode : "game";
  // let selectedMode = "game";
  // let selectedSpec = roomInfo ? roomInfo.specification : "horizontal";
  let selectedSpec = "horizontal";
  if (selectedSpec) {
    selectedSpec = selectedSpec.toUpperCase();
    console.log(selectedSpec);
    videoFrameWidth = videoFrameSizes[selectedSpec].width;
    videoFrameHeight = videoFrameSizes[selectedSpec].height;
  }
  console.log(selectedSpec);
  // openvidu 관련
  const [mySessionId, setMySessionId] = useState(params.id);
  const [myUserName, setMyUserName] = useState(userInfo.nickname);
  // const [myUserName, setMyUserName] = useState("s");
  const [session, setSession] = useState(undefined);
  const [mainStreamManager, setMainStreamManager] = useState(undefined);
  const [publisher, setPublisher] = useState(undefined);
  const [subscribers, setSubscribers] = useState([]);
  // room 관련
  const [isHost, setHost] = useState(false);
  const [frameSortType, setFrameSortType] = useState("hot");
  const [frameList, setFrameList] = useState([]);
  const [frameSearch, setFrameSearch] = useState("");
  const [Spec, setSpec] = useState("")
  const [selectFrame, setSelectFrame] = useState(`url('${horTest}')`);
  const [roomStatus, setRoomStatus] = useState(0);
  const [Minutes, setMinutes] = useState(0);
  const [Seconds, setSeconds] = useState(0);

  useEffect(() => {
    window.addEventListener("beforeunload", onbeforeunload);
    if (roomInfo) {
      sendRoomInfo();
      setHost(true);
    } 
    joinSession();
    if (params.id[0])
    return () => {
  window.removeEventListener("beforeunload", onbeforeunload);
};
}, [params.id]);

  useEffect(() => {
    joinUsers = [publisher, ...subscribers];
    if (!roomInfo) {
      // getRoomInfo();
    }
  }, [publisher, subscribers]);
  
  // useEffect(() => {
  //   if (publisher !== undefined) {
  //     const userStreamIds = joinUsers.map((user) => user.stream.streamId);
  //     const userStreamId = userStreamIds[0];
  //     userStream(mySessionId, userStreamId);
  //   }
  // }, [publisher]);
  
  useEffect(() => {
    if (roomStatus === 2) {
      if (isHost) {
        sendImageData(resultSrc, mySessionId);
      }
      setTimeout(async () => {
        try {
          // userDelete();
          // if (isHost) {
          //   roomDelete(mySessionId);
          // }
          navigate("/");
        } catch (error) {
          console.log(error);
        }
      }, 300000);
    }
  }, [roomStatus]);

  useEffect(() => {
    if (isHost && roomStatus === 0 && roomInfo === {}) {
      leaveSession();
    }
  }, [isHost]);

  useEffect(() => {
    if (roomInfo) {
      getFrames(frameSortType, frameSearch, selectedSpec);
    }
  }, [frameSortType]);

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
      console.log(event);
      deleteSubscriber(event.stream.streamManager);
      setTimeout(() => {
        const outStreamId = event.stream.streamId;
        userDisconnection(mySessionId, outStreamId);
      }, 1000);
      if (!isHost) {
        updateHost(mySessionId);
      }
    });

    mySession.on("exception", (exception) => {
      console.warn(exception);
    });

    mySession.on("startGameMode", (event) => {
      console.log(event);
      // if (chosenUrl) {
      //   axios
      //     .get(
      //       chosenUrl,
      //       {
      //         responseType: "arraybuffer",
      //       },
      //       {
      //         headers: {
      //           "Access-Control-Allow-Origin": "*",
      //           "Access-Control-Allow-Methods": "GET,POST",
      //         },
      //       }
      //     )
      //     .then((response) => {
      //       console.log(response);
      //       // setSelectFrame(response.data);
      //       const b64 = arrayBufferToBase64(response.data);
      //       console.log(b64);
      //       const image = new Image();
      //       image.src = "data:image/png;base64," + b64;
      //       // document.body.appendChild(image);
      //       // setSelectFrame(`url(${response.data})`);
      //       setSelectFrame(`url(${image.src})`);
      //     })
      //     .catch((error) => {
      //       console.log(error);
      //     });

      // }
      gameMode();
    });

    mySession.on("startNormalMode", (event) => {
      console.log(event);
      normalMode();
      handleNormalPhase(normalCnt);
    });

    mySession.on("normalProgress", (event) => {
      nextNormalPhase(normalCnt);
      setTimeout(() => {
        handleNormalPhase(normalCnt);
      }, 100);
    });

    mySession.on("resetNormalProgress", (event) => {
      normalCnt = 0;
      setOriginImage();
      setMainStreamManager(undefined);
      setTimeout(() => {
        handleNormalPhase(normalCnt);
      }, 100);
    });

    mySession.on("randomTags", (event) => {
      randomTags = event.data;
    });

    mySession.on("randomOrder", (event) => {
      orders = event.data
    })

    mySession.on("selectFrame", (event) => {
      console.log(event);
      // let src = /^data:image/.test(event.data)
      //   ? event.data
      //   : event.data + "?" + new Date().getTime();

      console.log(event.data);
      setSelectFrame(`url(${event.data})`);
      chosenUrl = event.data;
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
          // 카메라 선택 옵션 넣을 때 사용됨
          const devices = await OV.getDevices();
          setPublisher(publisher);
          console.log(publisher);

          sessionConnectId = publisher.session.connection.connectionId;
          userJoin(mySessionId);
          setTimeout(() => {
            // 방에 유저 정보 보낼 곳
            sessionStreamId = publisher.stream.streamId;
            // if (isHost) {
              //   console.log("방장");
              //   getRoomInfo(mySessionId);
              //   hostPost(mySessionId);
              // }
              userStream(mySessionId, sessionStreamId);
            }, 500);
            
          setTimeout(() => {
            getRoomInfo();
          }, 800);
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
    // userDelete();
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
  const sendRoomInfo = () => {
    try {
      axios
        .post(
          "/api/room",
          {
            password: roomInfo.password,
            maxCount: roomInfo.maxCount,
            mode: roomInfo.mode,
            roomCode: roomInfo.roomCode,
            specification: roomInfo.specification,
          },
          {
            headers: {
              // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
              Authorization: `${accessToken}`,
              "Content-Type": "application/json;charset=UTF-8",
            },
          }
        )
        .then((response) => {
          console.log(response);
        });
    } catch (error) {
      console.log(error);
    }
  };
  // Frame Carousel
  const carouselSettings = {
    dots: false,
    arrows: true,
    infinite: true,
    draggable: true,
    speed: 500,
    slidesToShow: 4,
    slidesToScroll: 4,
    initialSlide: 0,
    variableWidth: true,
  };

  // selectFrame Signal
  const handleSelectFrame = async (sessionId, frameURL) => {
    try {
      const request = await axios.post(
        "/openvidu/api/signal",
        {
          session: sessionId,
          to: [],
          type: "selectFrame",
          data: `${frameURL}`,
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
    } catch (error) {}
  };

  // 방 정보 조회
  const getRoomInfo = async () => {
    try {
      await axios
        .get("/api/room" + `?roomCode=${mySessionId}`, {
          headers: {
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            Authorization: `${accessToken}`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        })
        .then((response) => {
          console.log("ROOMINFO");
          console.log(response);
          selectedMode = response.data.mode
          selectedSpec = response.data.specification.toUpperCase()
          setSpec(response.data.specification.toUpperCase())
        });
    } catch (error) {
      console.log(error);
    }
  };
  // 유저 정보 방 입장 시 전달
  const userJoin = async (sessionId) => {
    console.log("USERJOIN");
    try {
      const request = await axios.post(
        "/api/participant",
        {
          roomCode: sessionId,
        },
        {
          headers: {
            Authorization: `${accessToken}`,
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
        "/api/participant",
        {},
        {
          headers: {
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            Authorization: `${accessToken}`,
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
  const userDisconnection = async (sessionId, streamId) => {
    console.log("USERSTREAM");
    try {
      const request = await axios.put(
        "/api/participant/fail/connection/" + sessionId,
        {
          streamId: streamId,
        },
        {
          headers: {
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            Authorization: `${accessToken}`,
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
      console.log(streamId);
      const request = await axios.put(
        "/api/participant/" + sessionId + "/streamId",
        {
          streamId: streamId,
        },
        {
          headers: {
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            Authorization: `${accessToken}`,
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
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            Authorization: `${accessToken}`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 방 정보 전파
  const spreadRoomInfo = (sessionId) => {
    try {
      axios.post("/openvidu/api/signal", {
        session: sessionId,
        to: [],
        type: "roomInfo",
        data: {
          mode: selectedMode,
          specification: selectedSpec,
        },
      });
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
        hostPost(mySessionId);
      }
    } catch (error) {
      console.error(error);
    }
  };
  // 방장 정보 갱신
  const hostPost = async (sessionId) => {
    console.log("HOSTPOST");
    try {
      const request = await axios.put(
        "/api/participant/" + sessionId + "/owner",
        {
          roomCode: sessionId,
        },
        {
          headers: {
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            Authorization: `${accessToken}`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        }
      );
      console.log(request);
    } catch (error) {
      console.log(error);
    }
  };
  // 프레임 목록 가져오기
  const getFrames = async (sortType, searchInput, specific) => {
    try {
      await axios
        .get(
          "/api/article/frame/list/" +
            sortType +
            `?searchWord=${searchInput}` +
            `&frameSpec=${specific}`,
          {
            headers: {
              "Content-Type": `application/json;charset=UTF-8`,
              // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjIifQ.OUP4gSxVM-CV0JeNYLxTtbwY9B0YGuS1PYjss9X0y5a9Q61g7Gjb43RsTVTK7L-EVhPvHS-DuUBN9Chy2SLgVg`,
              Authorization: `${accessToken}`,
              "ngrok-skip-browser-warning": "69420",
            },
          }
        )
        .then((response) => {
          console.log(response);
          setFrameList(response.data.frameArticleVoList);
        });
    } catch (error) {}
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
    let range = document.querySelector("#video-frame");
    // html2canvas를 사용하여 .video-frame 요소를 캡처
    html2canvas(range, {
      scale: 4,
      backgroundColor: null,
      allowTaint: true,
      useCORS: true,
    }).then((canvas) => {
      let ctx = canvas.getContext("2d");
      // 캔버스에 비디오를 그립니다.
      if (selectedSpec === "VERTICAL") {
        ctx.drawImage(
          video,
          // 0, // 비디오를 캔버스의 어떤 X 위치에 그릴지
          // 0, // 비디오를 캔버스의 어떤 y 위치에 그릴지
          // 217.5, // 비디오의 원래 width
          // 165.5, // 비디오의 원래 height
          320 + locationX, // 캔버스에 그릴 때 시작점 x 위치
          134.5 + locationY, // 캔버스에 그릴 때 시작점 y 위치
          170, // 캔버스에 그릴 비디오의 width
          114 // 캔버스에 그릴 비디오의 height
        );
      } else {
        ctx.drawImage(
          video,
          // 0, // 비디오를 캔버스의 어떤 X 위치에 그릴지
          // 0, // 비디오를 캔버스의 어떤 y 위치에 그릴지
          // 217.5, // 비디오의 원래 width
          // 165.5, // 비디오의 원래 height
          320 + locationX, // 캔버스에 그릴 때 시작점 x 위치
          134.5 + locationY, // 캔버스에 그릴 때 시작점 y 위치
          217.5, // 캔버스에 그릴 비디오의 width
          165.5 // 캔버스에 그릴 비디오의 height
        );
      }
      // 캔버스의 데이터 URL을 얻어 이미지로 사용할 수 있습니다.
      const dataURL = canvas.toDataURL("image/png");
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
  // 방 시작
  const startRoom = (sessionId) => {
    try {
      axios
        .put(
          `/api/room/${sessionId}/start`,
          { roomCode: sessionId },
          {
            headers: {
              // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
              Authorization: `${accessToken}`,
              "Content-Type": "application/json;charset=UTF-8",
            },
          }
        )
        .then((response) => {
          console.log(response);
        });
    } catch (error) {
      console.error(error);
    }
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
    const Users = joinUsers;
    // gameMode 로직
    const gamePhase = (count) => {
      // 4번 다 돌았을 때
      if (count === 0) {
        const range = document.querySelector("#video-frame");
        const imageURL = range.style.backgroundImage;
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
        if (randomTags) {
          randomTag = randomTags[4 - count];
          tagResult.push(randomTag);
        }
        console.log(orders)
        Users.forEach((user) => {
          if (
            user &&
            user.stream &&
            user.stream.streamId === orders[count - 1].streamId
          ) {
            // 해당 유저를 MainPublisher로 설정합니다.
            setMainStreamManager(user);
          }
        });
        // 초기 시간 설정 (setTimeout 맨 마지막 값과 동일시)
        // randomtag = randomtags[count-1].tag
        setMinutes(0);
        setSeconds(1);
        setTimeout(() => {
          // 3000ms 후에 일어날 일 (사진 찍기/주제 변경/유저 변경)
          setMinutes(0);
          setSeconds(3);
          setTimeout(() => {
            // 2000ms 후에 일어날 일(중간 텀), 이 이후 다시 else문
            if (selectedSpec === "HORIZONTAL") {
              handleCapture(
                horizontalLeftLoc[gameCnt],
                horizontalTopLoc[gameCnt]
              );
            } else {
              handleCapture(
                verticalLeftLoc[gameCnt],
                verticalTopLoc[gameCnt]
              );
            }
            setMainStreamManager(undefined);
            setMinutes(0);
            setSeconds(2);
            gameCnt = gameCnt + 1;
            gamePhase(count - 1);
          }, 3000);
        }, 2000);
      }
    };
    gamePhase(4);
  };
  // 일반모드 (settings)
  const normalMode = () => {
    console.log("NORMALMODE");
    setRoomStatus(1);
    // 유저 순서 설정(without RoomServer ver)
    // 순서 설정 기능 구현 후 userStreamIds ~ targetUsers 부분 삭제할 것
    // const userStreamIds = joinUsers.map((user) => user.stream.streamId);
    // const targetUsers = [...userStreamIds];
    // if (targetUsers.length === 3) {
    //   targetUsers.push(targetUsers[0]);
    // } else if (targetUsers.length === 2) {
    //   targetUsers.push(...targetUsers);
    // } else if (targetUsers.length === 1) {
    //   targetUsers.push(...targetUsers, ...targetUsers, ...targetUsers);
    // }
    // setOrders(targetUsers);
    // console.log(orders, targetUsers);
  };
  // 일반모드 (display)
  const handleNormalPhase = (count) => {
    console.log("COUNT : ", count);
    if (count === 4) {
      const range = document.querySelector("#video-frame");
      const imageURL = range.style.backgroundImage;
      console.log("imageURL: ", imageURL);
      const imageURLCleaned = imageURL.replace(/url\(['"]?(.*?)['"]?\)/, "$1");
      resultSrc = imageURLCleaned;
      console.log("finish");
      setRoomStatus(2);
      return;
    } else {
      if (selectedSpec === "HORIZONTAL") {
        locationX = horizontalLeftLoc[count];
        locationY = horizontalTopLoc[count];
      } else {
        locationX = verticalLeftLoc[count];
        locationY = verticalTopLoc[count];
      }
      joinUsers.forEach((user) => {
        console.log(user.stream.streamId, orders[count].streamId);
        if (user && user.stream && user.stream.streamId === orders[count].streamId) {
          // 해당 유저를 MainPublisher로 설정합니다.
          setMainStreamManager(user);
        } else {
          if (count === 0) {
            setMainStreamManager(joinUsers[0]);
          }
        }
      });
    }
  };
  // 일반모드 (capture)
  const nextNormalPhase = (count) => {
    if (selectedSpec === "HORIZONTAL") {
      handleCapture(horizontalLeftLoc[count], horizontalTopLoc[count]);     
    } else {
      handleCapture(verticalLeftLoc[count], verticalTopLoc[count]);
    }
    normalCnt = count + 1;
    setMainStreamManager(undefined);
  };
  // 다시하기에서 백그라운드 이미지 복구
  const setOriginImage = () => {
    const canvas = document.querySelector("#video-frame");
    canvas.style.backgroundImage = selectFrame;
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
  // 방 유저 전체 게임모드 돌입
  // const handleSendConcept = async (sessionId) => {
  //   try {
  //     const request = await axios.post(
  //       "/openvidu/api/signal",
  //       {
  //         session: sessionId,
  //         to: [],
  //         type: "sendConcept",
  //         data: randomTag,
  //       },
  //       {
  //         headers: {
  //           Authorization: `Basic ${btoa(
  //             `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
  //           )}`,
  //           "Content-Type": "application/json",
  //           "Access-Control-Allow-Origin": "*",
  //           "Access-Control-Allow-Methods": "GET,POST",
  //         },
  //       }
  //     );
  //     console.log(request);
  //   } catch (error) {
  //     console.error("Error checking session:", error);
  //   }
  // };
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
  // normalMode Signal
  const sendNormalProgress = () => {
    try {
      const request = axios.post(
        "/openvidu/api/signal",
        {
          session: mySessionId,
          to: [],
          type: "normalProgress",
          data: "",
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
    } catch (error) {}
  };
  const resetNormalProgress = () => {
    try {
      const request = axios.post(
        "/openvidu/api/signal",
        {
          session: mySessionId,
          to: [],
          type: "resetNormalProgress",
          data: "",
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
    } catch (error) {}
  };
  const sendOrders = () => {
    try {
      const request = axios.post(
        "/openvidu/api/signal",
        {
          session: mySessionId,
          to: [],
          type: "randomOrder",
          data: orders,
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
      console.log(error)
    }
  };
  const getRandomTags = () => {
    try {
      axios
        .get("/api/image/random/tag", {
          headers: {
            Authorization: `${accessToken}`,
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
          },
        })
        .then((response) => {
          console.log("대답:", response);
          // axios
          //   .post(
          //     "/openvidu/api/signal",
          //     {
          //       session: mySessionId,
          //       to: [],
          //       type: "randomTags",
          //       data: response.data,
          //     },
          //     {
          //       headers: {
          //         Authorization: `Basic ${btoa(
          //           `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
          //         )}`,
          //         "Content-Type": "application/json",
          //         "Access-Control-Allow-Origin": "*",
          //         "Access-Control-Allow-Methods": "GET,POST",
          //       },
          //     }
          //   )
          //   .then((response) => {
          //     console.log(response);
          //   })
          //   .catch((error) => {
          //     alert("게임에 필요한 요소를 전파하는데 실패했습니다", error);
          //     console.log(error);
          //   });
          // randomTags = response.data;
        })
        .catch((error) => {
          alert("게임에 필요한 요소를 불러오지 못했습니다", error);
          console.log(error);
        });
    } catch (error) {
      console.log(error);
    }
  };
  const getRandomOrder = (sessionId) => {
    try {
      axios
        .get(`/api/participant/random/${sessionId}`, {
          headers: {
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
            Authorization: `${accessToken}`,
            "Content-Type": "application/json;charset=UTF-8",
          },
        })
        .then((response) => {
          console.log(response);
          orders = response.data
          sendOrders()
          // setOrders([response.data])
        });
    } catch (error) {
      console.log(error);
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
              // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
              Authorization: `${accessToken}`,
              "Content-Type": "application/json;charset=UTF-8",
            },
          }
        )
        .then(async (response) => {
          // PRESIGNED URL에 이미지 보내는 코드
          // 아래 주석 절대 지우지 말기
          console.log("IMAGE");
          const binaryImageData = atob(resultURL.split(",")[1]);
          const arrayBufferData = new Uint8Array(binaryImageData.length);
          for (let i = 0; i < binaryImageData.length; i++) {
            arrayBufferData[i] = binaryImageData.charCodeAt(i);
          }
          const blob = new Blob([arrayBufferData], { type: "image/jpg" });
          // Blob 객체에서 File 객체 생성
          const imageFile = new File([blob], `${sessionId}.jpg`, {
            type: "image/jpg",
          });
          console.log(imageFile);
          // const image = new Image();
          // image.src = URL.createObjectURL(imageFile);
          // document.body.appendChild(image);
          let getFileName = response.data.fileName;
          await fetch(response.data.preSignUrl, {
            method: "PUT",
            body: imageFile,
            headers: {
              // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
              "Content-Type": "image/jpg",
            },
          }).then(async (response) => {
            console.log("DATA");
            console.log(response);
            // fileName, type(image), tags, roomCode to Back(Article)
            await axios
              .post(
                "/api/image",
                {
                  imageName: getFileName,
                  // imageName: "test.jpg",
                  fileType: "image",
                  // tags: tagResult,
                  tags: ["1", "2", "3", "4"],
                  roomCode: mySessionId,
                  // roomCode: "sessionB",
                },
                {
                  headers: {
                    // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
                    Authorization: `${accessToken}`,
                    "Content-Type": "application/json;charset=UTF-8",
                  },
                }
              )
              .then((response) => {
                console.log(response);
                roomImageID = response.data.imageId;
              });
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
            // Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjIifQ.OUP4gSxVM-CV0JeNYLxTtbwY9B0YGuS1PYjss9X0y5a9Q61g7Gjb43RsTVTK7L-EVhPvHS-DuUBN9Chy2SLgVg`,
            Authorization: `${accessToken}`,
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
    let range = document.querySelector("#video-frame");
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

  function arrayBufferToBase64(buffer) {
    var binary = "";
    var bytes = new Uint8Array(buffer);
    var len = bytes.byteLength;
    for (var i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
  }

  return (
    <div className="room">
      {roomStatus === 2 ? (
        <div className="room-active">
          <div className="room-top">
            {/* <RoomHeader status={roomStatus} /> */}
            <div className="room-header">
              <img id="logo" src={logo} alt="LOGO" />
              <br />
              <div>
                <p>방이 5분 후 종료됩니다</p>
              </div>
            </div>
            <RoomButtons
              onButton1={() => {
                axios
                  .delete(
                    `/openvidu/api/sessions/${mySessionId}/connection/${sessionConnectId}`,
                    {
                      headers: {
                        Authorization: `Basic ${btoa(
                          `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
                        )}`,
                      },
                    }
                  )
                  .then((response) => {
                    console.log(response);
                  })
                  .catch((error) => {
                    console.log(error);
                  });
                navigate("/");
              }}
              onButton2={() => {
                handleDownload();
              }}
              onButton3={() => {
                handleImageUpload(roomImageID);
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
            <div className="room-main">
              {Spec === "HORIZONTAL" && (
                <div
                  id="video-frame"
                  style={{
                    width: "589.6px",
                    height: "400.6px",
                    backgroundColor: "black",
                    backgroundImage: selectFrame,
                    backgroundSize: "contain",
                    backgroundRepeat: "no-repeat",
                    backgroundPosition: "center",
                  }}
                ></div>
              )}
              {Spec === "VERTICAL" && (
                <div
                  id="video-frame"
                  style={{
                    width: "207.8px",
                    height: "589.6px",
                    backgroundColor: "black",
                    backgroundImage: selectFrame,
                    backgroundSize: "contain",
                    backgroundRepeat: "no-repeat",
                    backgroundPosition: "center",
                  }}
                ></div>
              )}
            </div>
            {chatData && <ChatComponent user={chatData} />}
          </div>
          <div className="room-bot">
            <div className="video-container">
              <UserVideoComponent
                streamManager={publisher}
                myName={myUserName}
                selectedSpec={selectedSpec}
              />
              {subscribers.map((sub, i) => (
                <div
                  key={sub.id}
                  className="stream-container col-md-6 col-xs-6"
                >
                  <span>{sub.id}</span>
                  <UserVideoComponent
                    streamManager={sub}
                    selectedSpec={selectedSpec}
                  />
                </div>
              ))}
            </div>
            <Timer minutes="5" seconds="0" />
          </div>
        </div>
      ) : roomStatus === 1 ? (
        <div className="room-active">
          <div className="room-top">
            {/* <RoomHeader status={roomStatus} /> */}
            <div className="room-header">
              <img id="logo" src={logo} alt="LOGO" />
              <div>
                <p>컨셉 : {randomTag}</p>
              </div>
            </div>
            <RoomButtons
              onButton1={() => {}}
              onButton2={() => {
                sendNormalProgress();
              }}
              onButton3={() => {
                resetNormalProgress();
              }}
              buttonName1="나가기"
              buttonName2="촬영하기"
              buttonName3="다시 찍기"
              option1={true}
              option2={!isHost || selectedMode === "game"}
              option3={!isHost || normalCnt === 0 }
            />
          </div>
          <div className="room-mid">
            {/* <RoomPhoto /> */}
            <div className="room-main">
              {Spec === "HORIZONTAL" && (
                <div
                id="video-frame"
                style={{
                  width: "589.6px",
                  height: "400.6px",
                  backgroundColor: "black",
                  backgroundImage: selectFrame,
                  backgroundSize: "contain",
                  backgroundRepeat: "no-repeat",
                  backgroundPosition: "center",
                }}
                >
                {mainStreamManager && selectedMode === "game" && 
                (
                  <TargetVideoComponent
                    streamManager={mainStreamManager}
                    selectedSpec={Spec}
                    locationX={horizontalLeftLoc[gameCnt]}
                    locationY={horizontalTopLoc[gameCnt]}
                  />
                )}
                {mainStreamManager && selectedMode === "normal" &&  
                (
                  <TargetVideoComponent
                    streamManager={mainStreamManager}
                    selectedSpec={Spec}
                    locationX={horizontalLeftLoc[normalCnt]}
                    locationY={horizontalTopLoc[normalCnt]}
                  />
                )}
              </div>
              )}
              {Spec === "VERTICAL" && (
                <div
                id="video-frame"
                style={{
                  width: "207.8px",
                  height: "589.6px",
                  backgroundColor: "black",
                  backgroundImage: selectFrame,
                  backgroundSize: "contain",
                  backgroundRepeat: "no-repeat",
                  backgroundPosition: "center",
                }}
                >
                {mainStreamManager && selectedMode === "game" && 
                (
                  <TargetVideoComponent
                    streamManager={mainStreamManager}
                    selectedSpec={Spec}
                    locationX={verticalLeftLoc[gameCnt]}
                    locationY={verticalTopLoc[gameCnt]}
                  />
                )}
                {mainStreamManager && selectedMode === "normal" &&  
                (
                  <TargetVideoComponent
                    streamManager={mainStreamManager}
                    selectedSpec={Spec}
                    locationX={verticalLeftLoc[normalCnt]}
                    locationY={verticalTopLoc[normalCnt]}
                  />
                )}
              </div>
              )}
            </div>
            {chatData && <ChatComponent user={chatData} myName={myUserName} />}
          </div>
          <div className="room-bot">
            <div className="video-container">
              <UserVideoComponent
                streamManager={publisher}
                selectedSpec={selectedSpec}
                myName={myUserName}
              />
              {subscribers.map((sub, i) => (
                <div
                  key={sub.id}
                  className="stream-container col-md-6 col-xs-6"
                >
                  <span>{sub.id}</span>
                  <UserVideoComponent
                    streamManager={sub}
                    selectedSpec={selectedSpec}
                  />
                </div>
              ))}
            </div>
            <Timer minutes={Minutes} seconds={Seconds} />
          </div>
        </div>
      ) : (
        <div className="room-active">
          <div className="room-top">
            {/* <RoomHeader status={roomStatus} /> */}
            <div className="room-header">
              <img id="logo" src={logo} alt="LOGO" />
              <p>대기중</p>
            </div>
            <RoomButtons
              onButton1={() => {
                if (roomInfo) {
                  axios
                    .delete(`/openvidu/api/sessions/${mySessionId}`, {
                      headers: {
                        Authorization: `Basic ${btoa(
                          `OPENVIDUAPP:${APPLICATION_SERVER_SECRET}`
                        )}`,
                      },
                    })
                    .then((response) => {
                      console.log(response);
                    })
                    .catch((error) => {
                      console.log(error);
                    });
                }
                navigate("/");
              }}
              onButton2={() => {
                startRoom(mySessionId);
                if (selectedMode === "game") {
                  // getRandomTags();
                  getRandomOrder(mySessionId);
                  setTimeout(() => {
                    handleStartGameMode(mySessionId);
                  }, 100);
                } else {
                  if (selectedMode === "normal") {
                    getRandomOrder(mySessionId)
                    setTimeout(() => {
                      handleStartNormalMode(mySessionId);
                    }, 100);
                  } else {
                  }
                }
                // handleStartGameMode(mySessionId);
                // alert("Invalid Room")
              }}
              onButton3={() => {
                console.log("pub: ", publisher);
                console.log("sub: ", subscribers);
                console.log("roomInfo: ", roomInfo);
                console.log("connecionId: ", sessionConnectId);
                console.log("streamId: ", sessionStreamId);
                console.log("frame: ", selectFrame);
                console.log("frames: ", frameList);
                console.log("mode: ", selectedMode);
                console.log("spec: ", selectedSpec);
                console.log(chosenUrl);
                // getRandomOrder(mySessionId);
                // axios
                //   .get(
                //     "https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/38a46b4c-f74d-4b9b-bdd4-de6d379c16baSSSS.png",
                //     // "https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/2ba66a36-3d85-4645-9937-6f9f5e7b9543111.png",
                //     {
                //       responseType: "arraybuffer",
                //     },
                //     {
                //       headers: {
                //         "Access-Control-Allow-Origin": "*",
                //         "Access-Control-Allow-Methods": "GET,POST",
                //       },
                //     }
                //   )
                //   .then((response) => {
                //     console.log(response);
                //     // setSelectFrame(response.data);
                //     const b64 = arrayBufferToBase64(response.data);
                //     console.log(b64);
                //     const image = new Image();
                //     image.src = "data:image/png;base64," + b64;
                //     // document.body.appendChild(image);
                //     // setSelectFrame(`url(${response.data})`);
                //     setSelectFrame(`url(${image.src})`);
                //   })
                //   .catch((error) => {
                //     console.log(error);
                //   });
              }}
              buttonName1="나가기"
              buttonName2="시작하기"
              buttonName3="초대 링크"
              option2={!isHost}
              // option3={!isHost}
            />
          </div>
          <div className="room-mid">
            <div className="room-main">
            {Spec === "HORIZONTAL" && (
              <div
                id="video-frame"
                style={{
                  width: "589.6px",
                  height: "400.6px",
                  backgroundColor: "black",
                  backgroundImage: selectFrame,
                  backgroundSize: "contain",
                  backgroundRepeat: "no-repeat",
                  backgroundPosition: "center",
                }}
              >
                <UserVideoComponent
                  streamManager={publisher}
                  locationX={horizontalLeftLoc[0]}
                  locationY={horizontalTopLoc[0]}
                  selectedSpec={Spec}
                />
                {subscribers.map((sub, i) => (
                  <div key={i} className="stream-container col-md-6 col-xs-6">
                    <UserVideoComponent
                      streamManager={sub}
                      locationX={horizontalLeftLoc[i + 1]}
                      locationY={horizontalTopLoc[i + 1]}
                      selectedSpec={Spec}
                    />
                  </div>
                  ))}
                  </div>
            )} 
            {Spec === "VERTICAL" && (       
              <div
                id="video-frame"
                style={{
                  width: "207.8px",
                  height: "589.6px",
                  backgroundColor: "black",
                  backgroundImage: selectFrame,
                  backgroundSize: "contain",
                  backgroundRepeat: "no-repeat",
                  backgroundPosition: "center",
                }}
              >
                <UserVideoComponent
                  streamManager={publisher}
                  locationX={verticalLeftLoc[0]}
                  locationY={verticalTopLoc[0]}
                  selectedSpec={Spec}
                />
                {subscribers.map((sub, i) => (
                  <div key={i} className="stream-container col-md-6 col-xs-6">
                    <UserVideoComponent
                      streamManager={sub}
                      locationX={verticalLeftLoc[i + 1]}
                      locationY={verticalTopLoc[i + 1]}
                      selectedSpec={Spec}
                    />
                  </div>
                  ))}
                  </div>
                  )}
            </div>
            {chatData && <ChatComponent user={chatData} myName={myUserName} />}
          </div>
          <div className="room-bot">
            <div className="slide-container">
              {frameList && (
                <Slider {...carouselSettings}>
                  {frameList.map((item) => (
                    <div className="slide">
                      {/* <div key={item.id}> */}
                      <img
                        key={item.articleId}
                        src={item.frameLink}
                        alt={item.author}
                        // alt={item.subject}
                        style={{
                          width: "200px",
                          height: "125px",
                          borderRadius: "4px",
                          cursor: "pointer",
                        }}
                        onClick={() =>
                          {
                            if (isHost) {
                            handleSelectFrame(mySessionId, item.frameLink)
                            }
                          }
                        }
                      />
                      {/* </div> */}
                    </div>
                  ))}
                </Slider>
              )}
            </div>
            <div className="frame-settings">
              <div className="frame-search">
                {/* Frame name search */}
                <input
                  type="text"
                  placeholder="Search frame name..."
                  value={frameSearch}
                  onChange={(event) => {
                    setFrameSearch(event.target.value);
                  }}
                  // onChange={handleSearchTextChange}
                  onKeyDown={(event) => {
                    if (event.key === "Enter") {
                      getFrames(frameSortType, frameSearch);
                    }
                  }}
                  maxLength={10}
                />
              </div>
              <div className="frame-type-radio">
                {/* Frame type selection */}
                <form>
                  <label>
                    <input
                      type="radio"
                      name="frameType"
                      value="hot"
                      checked={frameSortType === "hot"}
                      onChange={() => setFrameSortType("hot")}
                    />
                    인기
                  </label>
                  <label>
                    <input
                      type="radio"
                      name="frameType"
                      value="recent"
                      checked={frameSortType === "recent"}
                      onChange={() => setFrameSortType("recent")}
                    />
                    최신
                  </label>
                  <label>
                    <input
                      type="radio"
                      name="frameType"
                      value="random"
                      checked={frameSortType === "random"}
                      onChange={() => setFrameSortType("random")}
                    />
                    무작위
                  </label>
                  {/* Add more type options as needed */}
                </form>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Room;

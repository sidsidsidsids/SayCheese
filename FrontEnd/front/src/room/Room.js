import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { OpenVidu } from "openvidu-browser";
import UserVideoComponent from "./UserVideoComponent";
import axios from "axios";
import "./Room.css";
import { Component } from "react";
import RoomButtons from "./RoomButtons";
import RoomChat from "./RoomChat";
import RoomFooter from "./RoomFooter";
import RoomHeader from "./RoomHeader";
import RoomPhoto from "./RoomPhoto";

import Timer from "./Timer";
// import ChatComponent from "./ChatComponent";

const APPLICATION_SERVER_URL = "http://localhost:5000/";
// const APPLICATION_SERVER_URL = "http://localhost:4443";
const APPLICATION_SERVER_SECRET = "MY_SECRET";
// const APPLICATION_SERVER_SECRET = 'SECRET';

class Room extends Component {
  constructor(props) {
    super(props);

    this.state = {
      mySessionId: undefined,
      myUserName: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      prevSubscribers: [],
      roomStatus: 0,
      chatData: undefined,
    };
    this.joinSession = this.joinSession.bind(this);
    this.leaveSession = this.leaveSession.bind(this);
    // this.switchCamera = this.switchCamera.bind(this);
    this.handleChangeSessionId = this.handleChangeSessionId.bind(this);
    this.handleChangeUserName = this.handleChangeUserName.bind(this);
    this.handleMainVideoStream = this.handleMainVideoStream.bind(this);
    this.onbeforeunload = this.onbeforeunload.bind(this);
  }
  componentDidMount() {
    window.addEventListener("beforeunload", this.onbeforeunload);
    this.setState({ mySessionId: "test" }); // mySessionId 이 방의 parameter 값으로 수정해야 함 !
    this.joinSession();
  }

  componentWillUnmount() {
    window.removeEventListener("beforeunload", this.onbeforeunload);
  }

  componentDidUpdate(prev) {}

  onbeforeunload(event) {
    this.leaveSession();
  }

  handleChangeSessionId(event) {
    this.setState({
      mySessionId: event.target.value,
    });
  }

  handleChangeUserName(event) {
    this.setState({
      myUserName: event.target.value,
    });
  }

  handleMainVideoStream(stream) {
    if (this.state.mainStreamManager !== stream) {
      this.setState({
        mainStreamManager: stream,
      });
    }
  }

  deleteSubscriber(streamManager) {
    let subscribers = this.state.subscribers;
    let index = subscribers.indexOf(streamManager, 0);
    if (index > -1) {
      subscribers.splice(index, 1);
      this.setState({
        subscribers: subscribers,
      });
    }
  }
  joinSession() {
    // --- 1) Get an OpenVidu object ---
    this.OV = new OpenVidu();

    // --- 2) Init a session ---

    this.setState(
      {
        session: this.OV.initSession(),
      },
      () => {
        var mySession = this.state.session;
        console.log(mySession);
        // --- 3) Specify the actions when events take place in the session ---

        // On every new Stream received...
        mySession.on("streamCreated", (event) => {
          // Subscribe to the Stream to receive it. Second parameter is undefined
          // so OpenVidu doesn't create an HTML video by its own
          var subscriber = mySession.subscribe(event.stream, undefined);
          var subscribers = this.state.subscribers;
          subscribers.push(subscriber);

          // Update the state with the new subscribers
          this.setState({
            subscribers: subscribers,
          });
        });

        // On every Stream destroyed...
        mySession.on("streamDestroyed", (event) => {
          // Remove the stream from 'subscribers' array
          this.deleteSubscriber(event.stream.streamManager);
        });

        // On every asynchronous exception...
        mySession.on("exception", (exception) => {
          console.warn(exception);
        });

        // --- 4) Connect to the session with a valid user token ---

        // Get a token from the OpenVidu deployment
        this.getToken().then((token) => {
          // First param is the token got from the OpenVidu deployment. Second param can be retrieved by every user on event
          // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
          mySession
            .connect(token)
            .then(async () => {
              // --- 5) Get your own camera stream ---
              console.log(mySession);
              console.log(mySession.connection.connectionId);
              this.setState({
                chatData: mySession,
              });
              console.log("occur");
              // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
              // element: we will manage it on our own) and with the desired properties
              let publisher = await this.OV.initPublisherAsync(undefined, {
                audioSource: undefined, // The source of audio. If undefined default microphone
                videoSource: undefined, // The source of video. If undefined default webcam
                publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
                publishVideo: true, // Whether you want to start publishing with your video enabled or not
                resolution: "320x240", // The resolution of your video
                frameRate: 30, // The frame rate of your video
                insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                mirror: false, // Whether to mirror your local video or not
              });

              // --- 6) Publish your stream ---

              mySession.publish(publisher);

              // Obtain the current video device in use
              var devices = await this.OV.getDevices();
              var videoDevices = devices.filter(
                (device) => device.kind === "videoinput"
              );
              var currentVideoDeviceId = publisher.stream
                .getMediaStream()
                .getVideoTracks()[0]
                .getSettings().deviceId;
              var currentVideoDevice = videoDevices.find(
                (device) => device.deviceId === currentVideoDeviceId
              );

              // Set the main video in the page to display our webcam and store our Publisher
              this.setState({
                currentVideoDevice: currentVideoDevice,
                mainStreamManager: publisher,
                publisher: publisher,
              });
            })
            .catch((error) => {
              console.log(
                "There was an error connecting to the session:",
                error.code,
                error.message
              );
            });
        });
      }
    );
  }
  leaveSession() {
    // --- 7) Leave the session by calling 'disconnect' method over the Session object ---

    const mySession = this.state.session;

    if (mySession) {
      mySession.disconnect();
    }

    // Empty all properties...
    this.OV = null;
    this.setState({
      session: undefined,
      subscribers: [],
      // mySessionId: "SessionA",
      // myUserName: "Participant" + Math.floor(Math.random() * 100),
      mainStreamManager: undefined,
      publisher: undefined,
    });
  }

  getToken() {
    return this.createSession(this.state.mySessionId).then((sessionId) =>
      this.createToken(sessionId)
    );
  }
  async createSession(sessionId) {
    console.log("state", this.state);
    console.log("sessionID", sessionId);
    const response = await axios.post(
      APPLICATION_SERVER_URL + "api/sessions",
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
    console.log("respons:", response);
    return response.data; // The sessionId
  }
  async createToken(session) {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "api/sessions/" + session + "/connections",
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
    console.log(response);
    return response.data; // The token
  }
  render() {
    return (
      // {roomState == "before" ? () : ()}
      <div className="room">
        {this.state.roomStatus === 2 ? (
          // <ResultRoom />
          <div className="room-active">
            <div className="room-top">
              <RoomHeader status={this.state.roomStatus} />
              <RoomButtons
                onConfirm={() => {
                  const navigate = useNavigate();
                  navigate("/main");
                }}
                onClose={() => {
                  this.setState({ roomStatus: 1 });
                }}
                buttonName1="나가기"
                buttonName2="사진 공유"
              />
            </div>
            <div className="room-mid">
              <RoomPhoto />
              {/* {subscribers.map((sub, i) => (
                <div key={sub.id} className="stream-container col-md-6 col-xs-6">
                <span>{sub.id}</span>
                <UserVideoComponent streamManager={sub} />
                </div>
              ))} */}
              <RoomChat />
            </div>
            <div className="room-bot">
              <RoomFooter status={this.state.roomStatus} />
              <Timer minutes="5" seconds="0" />
            </div>
          </div>
        ) : this.state.roomStatus === 1 ? (
          // <RunRoom />
          <div className="room-active">
            <div className="room-top">
              <RoomHeader status={this.state.roomStatus} />
              <RoomButtons
                onConfirm={() => {
                  this.setState({ roomStatus: 2 });
                }}
                onClose={() => {
                  this.setState({ roomStatus: 0 });
                }}
                buttonName1="촬영하기"
                buttonName2="다시 찍기"
              />
            </div>
            <div className="room-mid">
              <RoomPhoto />
              <RoomChat />
              {/* <UserVideoComponent streamManager={this.state.publisher} />
              {this.state.subscribers.map((sub, i) => (
                <div
                  key={sub.id}
                  className="stream-container col-md-6 col-xs-6"
                >
                  <span>{sub.id}</span>
                  <UserVideoComponent streamManager={sub} />
                </div>
              ))} */}
            </div>
            <div className="room-bot">
              <RoomFooter status={this.state.roomStatus} />
              <Timer minutes="0" seconds="30" />
            </div>
          </div>
        ) : (
          // <WaitRoom />
          <div className="room-active">
            <div className="room-top">
              <RoomHeader status={this.state.roomStatus} />
              <RoomButtons
                onConfirm={() => {
                  this.setState({ roomStatus: 1 });
                  console.log(this.state);
                }}
                onClose={() => {}}
                buttonName1="시작하기"
                buttonName2="초대 링크"
              />
            </div>
            <div className="room-mid">
              {/* <RoomPhoto /> */}
              <UserVideoComponent streamManager={this.state.publisher} />
              {this.state.subscribers.map((sub, i) => (
                <div
                  key={sub.id}
                  className="stream-container col-md-6 col-xs-6"
                >
                  <span>{sub.id}</span>
                  <UserVideoComponent streamManager={sub} />
                </div>
              ))}
              {/* <RoomChat /> */}
              {/* <ChatComponent user={this.chatData} /> */}
            </div>
            <div className="room-bot">
              <RoomFooter status={this.state.roomStatus} />
            </div>
          </div>
        )}
      </div>
    );
  }
}
export default Room;

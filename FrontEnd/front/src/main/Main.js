import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { getUserInfo } from "../redux/features/login/loginSlice";
import RoomCreateModal from "./RoomCreateModal";
import RoomJoinModal from "./RoomJoinModal";
import SetNameModal from "./SetNameModal";
let able;
function Main() {
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [joinModalOpen, setJoinModalOpen] = useState(false);
  const [nameModalOpen, setNameModalOpen] = useState(false);
  const { userInfo } = useSelector((store) => store.login);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getUserInfo());
    able = checkAvailable();
  }, []);

  const checkAvailable = async () => {
    try {
      const response = await axios.post(
        "/api/room/check",
        {},
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A`,
          },
        }
      );
      if (response.status === 200) {
        return true;
      } else {
        return false;
      }
    } catch (error) {
      console.log(error);
      return false;
    }
  };

  return (
    <div>
      Main
      <button
        onClick={() => {
          console.log(userInfo);
          if (!userInfo) {
            setNameModalOpen(true);
          } else {
            if (able) {
              setCreateModalOpen(true);
            } else {
              alert("이미 접속중입니다");
            }
          }
        }}
      >
        방 생성
      </button>
      <button
        onClick={() => {
          if (!userInfo) {
            setNameModalOpen(true);
          } else {
            if (able) {
              setJoinModalOpen(true);
            } else {
              alert("이미 접속중입니다");
            }
          }
        }}
      >
        방 입장
      </button>
      <br />
      <SetNameModal
        open={nameModalOpen}
        close={() => setNameModalOpen(false)}
        onConfirm={(inputNickname) => {
          // dispatch()
        }}
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

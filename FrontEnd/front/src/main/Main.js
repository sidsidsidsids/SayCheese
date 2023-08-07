import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

import RoomCreateModal from "./RoomCreateModal";
import RoomJoinModal from "./RoomJoinModal";
import SetNameModal from "./SetNameModal";
function Main() {
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [joinModalOpen, setJoinModalOpen] = useState(false);
  const [nameModalOpen, setNameModalOpen] = useState(false);
  // const nickname = useSelector((state) => state.nickname);
  const [nickname, setNickname] = useState(undefined)
  const dispatch = useDispatch();


  return (
    <div>
      Main
      <button
        onClick={() => {
          console.log(nickname)
          if (!nickname) {
            setNameModalOpen(true)
          } else {
          setCreateModalOpen(true);
          }
          console.log("방생성", createModalOpen);
        }}
      >
        방 생성
      </button>
      <button
        onClick={() => {
          console.log(nickname)
          if (!nickname) {
            setNameModalOpen(true)
          } else {
          setJoinModalOpen(true);
          }
          console.log("방입장", joinModalOpen);
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
          setNickname("GUEST_"+inputNickname)
          setNameModalOpen(false)
        }} />
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

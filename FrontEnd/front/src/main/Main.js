import { useState } from "react";
import RoomCreateModal from "./RoomCreateModal";
import RoomJoinModal from "./RoomJoinModal";

function Main() {
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [joinModalOpen, setJoinModalOpen] = useState(false);

  return (
    <div>
      Main
      <button
        onClick={() => {
          setCreateModalOpen(true);
          console.log("방생성", createModalOpen);
        }}
      >
        방 생성
      </button>
      <button
        onClick={() => {
          setJoinModalOpen(true);
          console.log("방입장", joinModalOpen);
        }}
      >
        방 입장
      </button>
      <br />
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

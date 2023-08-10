// FrameSearch 컴포넌트: 검색 인풋을 받는 컴포넌트입니다.
import React, { useState } from "react";
import axios from "axios";

import "../css/FrameSearch.css";
import SearchIcon from "../assets/search.png";

export default function FrameSearch() {
  const [searchWord, setSearchWord] = useState("");
  const [searchReturn, setSearchReturn] = useState("");
  function searchFrame() {
    axios
      .get(`/api/article/frame/list/recent?searchWord=${searchWord}`, {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
        },
      })
      .then((res) => {
        console.log("######", res.data);
        setSearchReturn(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  return (
    <div className="frameSearch">
      <form>
        <input onChange={(event) => setSearchWord(event.target.value)} />
        <button type="button" onClick={searchFrame}>
          <img src={SearchIcon} alt="서치 아이콘" />
        </button>
      </form>
    </div>
  );
}

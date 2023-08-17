// FrameSearch 컴포넌트: 검색 인풋을 받는 컴포넌트입니다.
import React, { useState } from "react";

import "../css/FrameSearch.css";
import SearchIcon from "../assets/search.png";

export default function FrameSearch({ searchWord, setSearchWord }) {
  const [inputValue, setInputValue] = useState("");
  function search() {
    setSearchWord(inputValue);
    setInputValue("");
  }
  // 사용자가 엔터키를 사용하면 검색하기를 원한다고 간주하여 실행
  const onCheckEnter = (e) => {
    if (e.key === "Enter") {
      search();
    }
  };
  return (
    <div className="frameSearch">
      <form onKeyDown={onCheckEnter}>
        <input
          value={inputValue}
          onChange={(event) => {
            setInputValue(event.target.value); // 엔터키가 아니면 검색어를 입력한다고 간주
          }}
        />
        {/* <input>이 하나면 엔터했을때 submit이 실행되서 리렌더링 되는 사이드 이펙트가 발생합니다. 이것을 방지하고자 보이지 않는 input 태그를 하나 더 만들었습니다 */}
        <input type="text" style={{ display: "none" }} />
        <button type="button" onClick={() => search()}>
          <img src={SearchIcon} alt="서치 아이콘" />
        </button>
      </form>
    </div>
  );
}

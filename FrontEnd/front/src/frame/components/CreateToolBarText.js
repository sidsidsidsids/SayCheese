import React, { useState, useRef } from "react";
// third party
import { useDispatch } from "react-redux";
import Select from "react-select";
import Swal from "sweetalert2";

// local
import { Undecorate } from "../../redux/features/frame/frameSlice";
import { AddText } from "../../redux/features/frame/frameSlice";
import "../css/CreateToolBarText.css";

export default function Text() {
  const [customText, setCustomText] = useState("");
  const [customTextColor, setCustomTextColor] = useState("#ffffff");
  const [customTextSize, setCustomTextSize] = useState("20");
  const [customTextFont, setCustomTextFont] = useState("Roboto");
  const textRef = useRef();
  const textColorRef = useRef();
  const textSizeRef = useRef();
  const dispatch = useDispatch();
  // 필요한 것 1. 텍스트 내용 2. 글꼴 3. 크기 4. 색깔
  const payload = {
    customText,
    customTextColor,
    customTextSize,
    customTextFont,
  };

  function deleteText() {
    dispatch(Undecorate());
  }

  function onRest() {
    // 제출 완료하면 인풋칸을 비웁니다
    if (!customText) {
      Swal.fire("내용을 입력하세요");
    }
    setCustomText("");
  }

  return (
    <>
      <label htmlFor="text">내용을 입력하세요</label>
      <input
        id="text"
        type="text"
        ref={textRef}
        value={customText}
        onChange={(e) => {
          setCustomText(e.target.value);
        }}
      />
      <label htmlFor="textColor">글자 색을 골라보세요</label>
      <input
        id="textColor"
        type="color"
        value={customTextColor}
        ref={textColorRef}
        onChange={(e) => {
          setCustomTextColor(e.target.value);
        }}
      />
      <label htmlFor="textSize">글자 크기를 선택하세요</label>
      <input
        id="textSize"
        type="number"
        ref={textSizeRef}
        defaultValue={22}
        max="60"
        min="1"
        onChange={(e) => {
          setCustomTextSize(e.target.value);
        }}
      ></input>

      <Select
        maxMenuHeight={300}
        placeholder="폰트를 골라보세요"
        menuPlacement="top"
        menuShouldScrollIntoView={true}
        styles={{
          control: (provided, state) => ({
            ...provided,
          }),
          option: (provided, state) => ({
            ...provided,
            fontFamily: state.value,
          }),
          menu: (provided) => ({
            ...provided,
            overflow: "hidden", // 옵션 백그라운드 오버플로우 해결
          }),
        }}
        theme={(theme) => ({
          ...theme,
          borderRadius: 30,
          colors: {
            ...theme.colors,
            primary25: "#F8C6D5",
            primary50: "#F8C6D5",
            primary: "black",
          },
        })}
        name="textFont"
        id="textFont"
        onChange={(e) => {
          setCustomTextFont(e.value);
        }}
        options={[
          { value: "Black Han Sans", label: "Black Han Sans" },
          { value: "Jua", label: "Jua" },
          { value: "Ramche", label: "Ramche" },
          { value: "TAEBAEKmilkyway", label: "태백은하수체" },
          { value: "KCC-Ganpan", label: "KCC 간판" },
          { value: "KCCMurukmuruk", label: "KCC 무럭무럭" },
          { value: "Cafe24ClassicType-Regular", label: "카페24 클래식타입" },
          { value: "ChosunCentennial", label: "조선100년체" },
          { value: "EarlyFontDiary", label: "다이어리체" },
          { value: "Cafe24Supermagic-Bold-v1.0", label: "카페24슈퍼매직" },
          { value: "iceJaram-Rg", label: "인천교육자람체" },
          { value: "PyeongChangPeace-Bold", label: "평창평화체" },
          { value: "GangwonEdu_OTFBoldA", label: "강원교육모두체" },
        ]}
      />
      <div className="alignTwoButtons">
        <button
          type="button"
          onClick={(event) => {
            event.stopPropagation();
            dispatch(AddText(payload));
            onRest();
          }}
          className="whtbtn"
        >
          추가하기
        </button>
        <br />
        <button
          className="btn"
          onClick={(event) => {
            event.stopPropagation();
            deleteText();
          }}
        >
          삭제하기
        </button>
      </div>
    </>
  );
}

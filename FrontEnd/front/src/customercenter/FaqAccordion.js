import React, { useState } from "react";
// local
import "./FaqAccordion.css";

function FaqAccordion({ sections }) {
  const [activeIndex, setActiveIndex] = useState(null);

  const getQnACard = (index) => {
    if (index === activeIndex) {
      setActiveIndex(null);
    } else {
      setActiveIndex(index);
    }
  };

  return (
    <div>
      {sections.map((section, index) => (
        <div className="FaqCard" key={index}>
          <div
            className={`section ${
              index === activeIndex ? "active" : ""
            } faq-card-title`}
            onClick={() => getQnACard(index)}
          >
            {/* index와 activeIndex가 일치할 경우 active 클래스가 추가됨, faq-card-title은 상관 없음*/}
            {section.question}
          </div>
          {index === activeIndex ? (
            <div className="faq-card-answer">{section.answer}</div>
          ) : (
            <div className="faq-card-none"></div>
          )}
          {/* index와 activeIndex가 일치할 경우 faq-card-answer 클래스 추가 및 answer 출력,
          일치하지 않을 경우 faq-card-none 클래스 추가 (답변 부분 감추는 동작) */}
        </div>
      ))}
    </div>
  );
}

export default FaqAccordion;

import React, { useState } from "react";
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
            {section.question}
          </div>
          {index === activeIndex ? (
            <div className="faq-card-answer">{section.answer}</div>
          ) : (
            <div className="faq-card-none"></div>
          )}
        </div>
      ))}
    </div>
  );
}

export default FaqAccordion;

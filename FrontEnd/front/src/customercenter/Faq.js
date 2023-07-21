import FaqAccordion from "./FaqAccordion";

// 임의로 faq 데이터 만듦. db연결할 경우 수정!
const qnaList = [
  {
    question: "자주 묻는 질문 1",
    answer: "그에 대한 답변 1",
  },
  {
    question: "자주 묻는 질문 2",
    answer:
      "그에 대한 답변 2 그에 대한 답변 2 그에 대한 답변 2 그에 대한 답변 2 그에 대한 답변 2",
  },
  {
    question: "자주 묻는 질문 3",
    answer: "그에 대한 답변 3",
  },
  {
    question: "자주 묻는 질문 4",
    answer: "그에 대한 답변 4",
  },
  {
    question: "자주 묻는 질문 5",
    answer: "그에 대한 답변 5",
  },
];

function Faq() {
  return (
    <div>
      <h1>자주 묻는 질문</h1>
      <br className="stop-dragging" />
      <FaqAccordion sections={qnaList} />
    </div>
  );
}

export default Faq;

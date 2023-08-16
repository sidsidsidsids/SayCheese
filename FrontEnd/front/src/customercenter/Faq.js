import { useEffect, useState } from "react";
// third party
import axios from "axios";
// local
import FaqAccordion from "./FaqAccordion";

function Faq() {
  const [faqs, setFaqs] = useState([]); // FAQ

  useEffect(() => {
    getFaq();
  }, []);

  async function getFaq() {
    try {
      console.log("try 시작");
      const response = await axios.get("/api/faq", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
        },
      });
      setFaqs(response.data.faqVoList);
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <div>
      <h1>자주 묻는 질문</h1>
      <br className="stop-dragging" />
      <FaqAccordion sections={faqs} />
    </div>
  );
}

export default Faq;

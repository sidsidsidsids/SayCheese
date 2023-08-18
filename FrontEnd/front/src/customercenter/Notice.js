import { useEffect, useState } from "react";
// third party
import { useParams } from "react-router-dom";
import axios from "axios";
// local
import "./Notice.css";
import NoticeDetail from "./NoticeDetail";

function Notice() {
  const { id } = useParams(); // 글 세부 정보 가져오기 위해 파라미터 값을 id로
  const [article, setArticle] = useState({});

  useEffect(() => {
    getArticle();
  }, []);

  async function getArticle() {
    try {
      const response = await axios.get(`/api/article/notice/${id}`, {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
        },
      });
      setArticle(response.data);
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <div>
      {article ? (
        <NoticeDetail
          id={article.id}
          subject={article.subject}
          createdDate={article.createdDate}
          hit={article.hit}
          content={article.content}
        />
      ) : (
        <p>확인</p>
      )}
    </div>
  );
}
export default Notice;

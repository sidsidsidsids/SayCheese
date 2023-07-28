import { useEffect, useState } from "react";
import "./Notice.css";
import { useParams } from "react-router-dom";
import axios from "axios";
import NoticeDetail from "./NoticeDetail";

function Notice() {
  const { id } = useParams();
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
      console.log(response.data);
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

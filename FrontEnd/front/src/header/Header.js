// 로고와 nav bar를 만들 페이지 입니다.

import { Link } from "react-router-dom";
import { AuthContext } from "../contexts/AuthContext";
import { useContext } from "react";

function Header() {
  const { isLogin, setIsLogin } = useContext(AuthContext);

  function handleLogout() {
    setIsLogin(false);
  }

  return (
    <div className="Header">
      <p>Nav bar</p>
      {isLogin ? (
        <button onClick={handleLogout} type="button">
          로그아웃
        </button>
      ) : (
        <Link to="/user/login">로그인</Link>
      )}
    </div>
  );
}

export default Header;

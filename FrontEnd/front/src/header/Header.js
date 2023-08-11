// 로고와 nav bar를 만들 페이지 입니다.
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  getUserInfo,
  loginSuccess,
  logintemporary,
  logoutSuccess,
} from "../redux/features/login/loginSlice";
import "./Header.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Header() {
  const { isLogin, userInfo } = useSelector((store) => store.login);

  const movePage = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (localStorage.getItem("accessToken")) {
      dispatch(loginSuccess());
      dispatch(getUserInfo());
    } else {
      dispatch(logoutSuccess());
    }
  }, []);

  function temporary() {
    localStorage.setItem(
      "accessToken",
      "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjIifQ.OUP4gSxVM-CV0JeNYLxTtbwY9B0YGuS1PYjss9X0y5a9Q61g7Gjb43RsTVTK7L-EVhPvHS-DuUBN9Chy2SLgVg"
    );
    dispatch(logintemporary("se6816@naver.com"));
  }

  function handleLogout() {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .get("/api/logout", {
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "69420",
          Authorization: `${accessToken}`,
        },
      })
      .then(() => {
        alert("로그아웃 되었습니다.");
        movePage("/");
        dispatch(logoutSuccess());
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
      })
      .catch((error) => {
        alert(
          "오류로 인해 로그아웃이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      });
  }

  return (
    <div className="Header">
      <ul>
        <li>
          <Link to="/">
            <p>Nav bar</p>
          </Link>
        </li>
      </ul>
      <button onClick={temporary}>액세스</button>
      <div className="HeaderSort">
        <ul>
          <li>
            <Link to="/photo" className="HoverEffect">
              네컷사진
            </Link>
          </li>
        </ul>
        <ul>
          <li style={{ margin: "auto 0 auto 10px" }}>
            <Link to="/frame/" className="HoverEffect">
              프레임
            </Link>
          </li>
        </ul>
        {isLogin ? (
          <ul className="DropDownMenu">
            {userInfo ? (
              <li className="MyPageHover">
                <p>{userInfo.nickname}님</p>
                <ul className="DropDownSubMenu">
                  <li>
                    <Link
                      to={`/user/mypage/${userInfo.email}`}
                      className="HoverEffect"
                    >
                      마이 페이지
                    </Link>
                  </li>
                  <li>
                    <p onClick={handleLogout} className="HoverEffect">
                      로그아웃
                    </p>
                  </li>
                </ul>
              </li>
            ) : (
              <p>로딩 중</p>
            )}
          </ul>
        ) : (
          <ul className="DropDownMenu">
            <li className="MyPageHover">
              <Link to="/user/login" className="HoverEffect">
                로그인
              </Link>
              <ul className="DropDownSubMenuTwo">
                <li>
                  <Link to="/user/signup" className="HoverEffect">
                    회원가입
                  </Link>
                </li>
              </ul>
            </li>
          </ul>
        )}
        <ul>
          <li>
            <Link to="/customercenter/notice" className="HoverEffect">
              고객센터
            </Link>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default Header;

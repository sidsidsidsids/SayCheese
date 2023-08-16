// 로고와 nav bar를 만들 페이지 입니다.
import { useEffect } from "react";
// third party
import { Link, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
// local
import {
  loginSuccess,
  logintemporary,
  logoutSuccess,
} from "../redux/features/login/loginSlice";
import "./Header.css";
<<<<<<< HEAD
import { useNavigate } from "react-router-dom";
import axios from "axios";
=======
import logo from "./assets/sign.png";
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723

function Header() {
  const { isLogin, userInfo } = useSelector((store) => store.login);

  const movePage = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (localStorage.getItem("accessToken")) {
      dispatch(loginSuccess());
      // dispatch(getUserInfo());
    } else {
      dispatch(logoutSuccess());
    }
  }, []);
<<<<<<< HEAD

  function temporary() {
    localStorage.setItem(
      "accessToken",
      "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXJJZCI6IjEifQ.sV341CXOobH8-xNyjrm-DnJ8nHE8HWS2WgM44EdIp6kwhU2vdmqKcSzKHPsEn_OrDPz6UpBN4hIY5TjTa42Z3A"
    );
    dispatch(logintemporary("se6816@naver.com"));
  }
=======
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723

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
<<<<<<< HEAD
        movePage("/");
=======
        movePage("/main");
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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
<<<<<<< HEAD
          <Link to="/">
            <p>Nav bar</p>
          </Link>
        </li>
      </ul>
      <button onClick={temporary}>액세스</button>
=======
          <Link to="/main">
            <img src={logo} className="SayCheeseLogo" alt="Say Cheese" />
          </Link>
        </li>
      </ul>

>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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
<<<<<<< HEAD
              <Link to="/user/login" className="HoverEffect">
=======
              <Link to="/user/login" className="HoverEffect LoginLink">
>>>>>>> 005bb6db321bd7c9af605eae98202b2907c6a723
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

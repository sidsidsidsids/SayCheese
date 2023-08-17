// 로고와 nav bar를 만들 페이지 입니다.
import { useEffect } from "react";
// third party
import { Link, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import Swal from "sweetalert2";
// local
import {
  loginSuccess,
  logintemporary,
  logoutSuccess,
} from "../redux/features/login/loginSlice";
import "./Header.css";
import logo from "./assets/sign.png";

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
        Swal.fire("로그아웃 되었습니다.");
        movePage("/main");
        dispatch(logoutSuccess());
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
      })
      .catch((error) => {
        Swal.fire(
          "오류로 인해 로그아웃이 불가능합니다.\n다시 시도해주시길 바랍니다."
        );
      });
  }

  return (
    <div className="Header">
      <ul>
        <li>
          <Link to="/main">
            <img src={logo} className="SayCheeseLogo" alt="Say Cheese" />
          </Link>
        </li>
      </ul>

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
              <Link to="/user/login" className="HoverEffect LoginLink">
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

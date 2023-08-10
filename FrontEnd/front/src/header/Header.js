// 로고와 nav bar를 만들 페이지 입니다.
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  getUserInfo,
  loginSuccess,
  logoutSuccess,
} from "../redux/features/login/loginSlice";
import "./Header.css";
import { useNavigate } from "react-router-dom";

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
  }, [dispatch]);
  // const [name, setName] = useState("");

  // const token = localStorage.getItem("accessToken");
  // const firstStep = () => {
  //   if (localStorage.getItem("accessToken")) {
  //     dispatch(loginSuccess());
  //     dispatch(setUserInfo());
  //     console.log("유즈이펙트밖에서 실행했음", userInfo);
  //   }
  // };
  // firstStep();

  // useEffect(() => {
  //   firstStep();
  // }, []);
  // useEffect(() => {
  // if (localStorage.getItem("accessToken")) {
  //   dispatch(loginSuccess());
  //   dispatch(setUserInfo());
  //   console.log("1 유즈이펙트");
  // } else {
  //   dispatch(logoutSuccess());
  // }
  // const firstStep = () => {
  //   return new Promise((resolve) => {
  //     if (localStorage.getItem("accessToken")) {
  //       dispatch(loginSuccess()); // Assuming you have defined the dispatch function for handling Redux actions
  //       dispatch(setUserInfo()); // Assuming you have defined the dispatch function for handling Redux actions
  //       resolve();
  //       console.log("디스패치 했음");
  //     }
  //   });
  // };

  // firstStep().then(() => {
  //   console.log("디스패치 저장함");
  //   setName(userInfo);
  // });
  // }, [isLogin]);

  function handleLogout() {
    movePage("/");
    dispatch(logoutSuccess());
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  }

  return (
    <div className="Header">
      <p>Nav bar</p>
      {isLogin ? (
        <ul className="DropDownMenu">
          {userInfo ? (
            <li className="MyPageHover">
              <p>닉네임 : {userInfo.nickname}</p>{" "}
              <Link
                to={`/user/mypage/${userInfo.email}`}
                className="HoverEffect"
              >
                마이 페이지
              </Link>
              <ul className="DropDownSubMenu">
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
        <Link to="/user/login">로그인</Link>
      )}
    </div>
  );
}

export default Header;

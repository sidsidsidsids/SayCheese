import { NavLink } from "react-router-dom";
// local
import "./CenterMenu.css";

function CenterMenu() {
  return (
    <div className="CenterMenu">
      <h1>고객센터</h1>
      <hr />
      <NavLink
        to="/customercenter/notice"
        className={({ isActive }) => {
          return isActive ? "ActiveMenu" : "NotActiveMenu";
        }}
      >
        {/* 링크가 활성화되어있는 경우 클래스 ActiveMenu, 비활성화되어있는 경우 클래스 NotActiveMenu 동작 */}
        <h3>공지사항</h3>
      </NavLink>
      <hr />
      <NavLink
        to="/customercenter/faq"
        className={({ isActive }) => {
          return isActive ? "ActiveMenu" : "NotActiveMenu";
        }}
      >
        <h3>자주 묻는 질문</h3>
      </NavLink>
    </div>
  );
}

export default CenterMenu;

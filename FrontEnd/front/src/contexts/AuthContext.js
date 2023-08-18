import { createContext, useEffect, useState } from "react";

// Context 생성 함수
function createAuthContext() {
  const AuthContext = createContext({ isLogin: false, setIsLogin: () => {} });
  return AuthContext;
}

export const AuthContext = createAuthContext();

export const AuthProvider = ({ children }) => {
  const [isLogin, setIsLogin] = useState(false);

  // useEffect(() => {
  //   const accessToken = localStorage.getItem("accessToken");
  //   if (accessToken) {
  //     loginCallBack(true);
  //   } else {
  //     loginCallBack(false);
  //   }
  // }, []);

  function loginCallBack() {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      setIsLogin(true);
    } else {
      setIsLogin(false);
    }
  }

  return (
    <AuthContext.Provider value={{ isLogin, setIsLogin, loginCallBack }}>
      {children}
    </AuthContext.Provider>
  );
};

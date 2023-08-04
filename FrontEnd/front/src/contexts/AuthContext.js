import { createContext, useState } from "react";

// Context 생성 함수
function createAuthContext() {
  const AuthContext = createContext();
  return AuthContext;
}

export const AuthContext = createAuthContext();

export const AuthProvider = ({ children }) => {
  const [isLogin, setIsLogin] = useState(false);

  return (
    <AuthContext.Provider value={{ isLogin, setIsLogin }}>
      {children}
    </AuthContext.Provider>
  );
};

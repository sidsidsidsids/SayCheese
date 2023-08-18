// third party
import { Outlet } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import { QueryClientProvider, QueryClient } from "@tanstack/react-query";
// local
import Header from "./header/Header";
import "./App.css";

function App() {
  // Create a client
  const queryClient = new QueryClient();

  return (
    <div className="App">
      <AuthProvider>
        <Header />
        <div className="contents">
          <QueryClientProvider client={queryClient}>
            <Outlet />
          </QueryClientProvider>
        </div>
      </AuthProvider>
    </div>
  );
}

export default App;

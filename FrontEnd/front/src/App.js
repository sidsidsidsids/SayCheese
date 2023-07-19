import { Outlet } from "react-router-dom";
import "./App.css";
import Header from "./header/Header";

function App() {
  return (
    <div className="App">
      <Header />
      <div className="contents">
        <Outlet />
      </div>
      <p>app.js</p>
    </div>
  );
}

export default App;

import { BrowserRouter, Route, Routes } from "react-router-dom";
import Main from "./main/Main";
import Room from "./room/Room";
import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/room/:id" element={<Room />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

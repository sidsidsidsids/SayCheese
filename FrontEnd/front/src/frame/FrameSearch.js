import "./FrameSearch.css";
import SearchIcon from "./assets/search.png";

export default function FrameSearch() {
  return (
    <div className="frameSearch">
      <form>
        <input />
        <button>
          <img src={SearchIcon} alt="서치 아이콘" />
        </button>
      </form>
    </div>
  );
}

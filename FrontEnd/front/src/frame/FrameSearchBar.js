import "./FrameSearchBar.css";
import SearchIcon from "./assets/search.png";

export default function FrameSearchBar() {
  return (
    <div className="frameSearch">
      <form>
        <input />
        <button>
          <img src={SearchIcon} />
        </button>
      </form>
    </div>
  );
}

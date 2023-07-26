import "./Photo.css";
import PhotoCategory from "./PhotoCategory";

function Photo() {
  return (
    <div className="PhotoBox">
      <div className="CategorySort">
        <PhotoCategory text={"가장 인기있는 사진"} />
      </div>
    </div>
  );
}

export default Photo;

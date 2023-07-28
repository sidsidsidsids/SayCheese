import "./Photo.css";
import PhotoCategory from "./PhotoCategory";
import PhotoSlider from "./PhotoSlider";

function Photo() {
  return (
    <div className="PhotoBox">
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"가장 인기있는 사진"} />
        </div>
        <PhotoSlider />
      </div>
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"최신 사진"} />
        </div>
        <PhotoSlider />
      </div>
      <div className="PhotoAllSort">
        <div className="CategorySort">
          <PhotoCategory text={"컨셉 1 사진"} />
        </div>
        <PhotoSlider />
      </div>
    </div>
  );
}

export default Photo;

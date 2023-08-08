import "./PhotoCategory.css";

function PhotoCategory({ text }) {
  return (
    <div>
      <h3 className="CategoryColor"># {text}</h3>
      <div></div>
    </div>
  );
}

export default PhotoCategory;

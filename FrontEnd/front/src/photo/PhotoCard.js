import "./PhotoCard.css";

function PhotoCard({ name, imgSrc, likes }) {
  return (
    <div className="PhotoCard">
      {name}
      <br />
      <img width="100px" src={imgSrc} />
      <br />
      likes:{likes}
    </div>
  );
}

export default PhotoCard;

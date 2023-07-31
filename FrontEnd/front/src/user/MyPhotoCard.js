import "./MyPhotoCard.css";

function MyPhotoCard({ name, imgSrc, likes }) {
  return (
    <div className="MyPhotoCard">
      {name}
      <br />
      <img width="100px" src={imgSrc} />
      <br />
      likes:{likes}
    </div>
  );
}

export default MyPhotoCard;

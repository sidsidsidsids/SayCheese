import "./MyPhotoCard.css";

function MyPhotoCard({ name, imgSrc, likes }) {
  return (
    <div className="MyPhotoCard">
      {name}
      <br />
      <img width="150px" src={imgSrc} />
      <br />
      likes : {likes}
    </div>
  );
}

export default MyPhotoCard;

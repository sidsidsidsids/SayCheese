import "./Button.css";

function Button({ className, text, onClick }) {
  return (
    <button className={`btn ${className}`} onClick={onClick}>
      {/* 클릭 이벤트 추가하기 위해 onClick 추가 */}
      {text}
    </button>
  );
}

export default Button;

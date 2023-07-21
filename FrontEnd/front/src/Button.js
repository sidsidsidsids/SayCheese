import "./Button.css";

function Button({ className, text }) {
  return <button className={`btn ${className}`}>{text}</button>;
}

export default Button;

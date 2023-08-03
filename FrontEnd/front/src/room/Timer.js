import React, { useEffect, useState } from "react";
import stopwatch from "./assets/timer.png";
import "./Timer.css";

const Timer = ({ minutes, seconds }) => {
  const [remainingTime, setRemainingTime] = useState({
    minutes: parseInt(minutes),
    seconds: parseInt(seconds),
  });

  useEffect(() => {
    let interval = null;

    // 새로운 props가 전달되면 remainingTime을 업데이트
    setRemainingTime({
      minutes: parseInt(minutes),
      seconds: parseInt(seconds),
    });

    interval = setInterval(() => {
      setRemainingTime((prevState) => {
        const { minutes, seconds } = prevState;
        if (minutes === 0 && seconds === 0) {
          // 카운트다운 종료
          clearInterval(interval);
          return prevState;
        } else if (seconds > 0) {
          return {
            ...prevState,
            seconds: seconds - 1,
          };
        } else {
          return {
            minutes: minutes - 1,
            seconds: 59,
          };
        }
      });
    }, 1000);

    return () => clearInterval(interval);
  }, [minutes, seconds]); // 새로운 props가 전달될 때마다 useEffect 실행

  return (
    <div className="timer">
      <img src={stopwatch} alt="watch" width={"10%"} />
      {remainingTime.minutes.toString().padStart(2, "0")}:
      {remainingTime.seconds.toString().padStart(2, "0")}
    </div>
  );
};

export default Timer;

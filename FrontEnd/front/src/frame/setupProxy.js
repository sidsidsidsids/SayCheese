const proxy = require("http-proxy-middleware");

// src/setupProxy.js
module.exports = function (app) {
  app.use(
    proxy("/api/article/frame", {
      target: "https://9177-211-192-210-179.ngrok-free.app", // 비즈니스 서버 URL 설정
      changeOrigin: true,
    })
  );
};

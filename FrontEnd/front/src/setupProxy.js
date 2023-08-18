const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    createProxyMiddleware("/openvidu", {
      target: "https://i9a401.p.ssafy.io",
      changeOrigin: true,
    })
  );
  app.use(
    createProxyMiddleware("/api", {
      target: "https://i9a401.p.ssafy.io",
      changeOrigin: true,
    })
  );
};
/* /api 로 시작하는 endpoint가 서버에 어떠한 통신을 요청한 경우 proxy 서버가 중계 역할을 하며
    target으로 지정한 주소로 통신하면서 서버를 우회함*/

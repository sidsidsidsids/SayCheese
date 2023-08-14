const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    createProxyMiddleware("/openvidu", {
      target: "https://i9a401.p.ssafy.io",
      changeOrigin: true,
    })
  );
  app.use(
    createProxyMiddleware("/api/room", {
      target: "https://17ad-175-197-19-209.ngrok-free.app",
      changeOrigin: true,
    })
  );
  app.use(
    createProxyMiddleware("/api/image", {
      target: "https://1c33-211-192-210-169.ngrok-free.app",
      changeOrigin: true,
    })
  );
  app.use(
    createProxyMiddleware("/api/amazon", {
      target: "https://c0d8-175-197-19-209.ngrok-free.app",
      changeOrigin: true,
    })
  );
  app.use(
    createProxyMiddleware("/api/article", {
      target: "https://b149-1-238-96-220.ngrok-free.app/",
      changeOrigin: true,
    })
  );
  app.use(
    createProxyMiddleware("/api", {
      target: "https://17ad-175-197-19-209.ngrok-free.app",
      changeOrigin: true,
    })
  );
};

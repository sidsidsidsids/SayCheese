const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    createProxyMiddleware("/api", {
      target: "https://b9b6-211-192-210-179.ngrok-free.app",
      changeOrigin: true,
    })
  );
};

const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    createProxyMiddleware("/api", {
      target: "https://4abe-211-192-210-189.ngrok-free.app",
      changeOrigin: true,
    })
  );
};

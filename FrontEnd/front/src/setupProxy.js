const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    createProxyMiddleware("/api", {
      target: "https://ca91-211-192-210-169.ngrok-free.app",
      changeOrigin: true,
    })
  );
};

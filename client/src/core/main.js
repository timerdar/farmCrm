import { router } from "./router.js";
import { showErrorPopup } from "./error.js";

window.addEventListener('DOMContentLoaded', router);
window.addEventListener('hashchange', router);

window.onerror = function(message, url, line, col, error) {
  let errorMsg = `Ошибка: ${message}`;
  if (url) errorMsg += `\nФайл: ${url}:${line}:${col}`;
  if (error && error.stack) errorMsg += `\nСтек: ${error.stack}`;
  showErrorPopup(errorMsg);
  return false;
};

window.onunhandledrejection = function(event) {
  showErrorPopup(`Ошибка: ${event.response ? event.response.message : event.reason}`);
};
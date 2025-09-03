function initErrorPopup() {
  if (document.getElementById('error-popup')) return;

  const popup = document.createElement('div');
  popup.id = 'error-popup';
  popup.className = 'error-popup';
  popup.innerHTML = `
    <button class="close-btn" title="Закрыть">&times;</button>
    <div class="error-text"></div>
  `;
  document.body.appendChild(popup);

  popup.querySelector('.close-btn').onclick = () => {
    popup.style.display = 'none';
  };
}

export function showErrorPopup(message) {
  initErrorPopup();
  const popup = document.getElementById('error-popup');
  popup.querySelector('.error-text').textContent = message;
  popup.style.display = 'block';
}
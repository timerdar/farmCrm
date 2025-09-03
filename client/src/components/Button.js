export function Button(text, onClick) {
    const btn = document.createElement('button');
    btn.textContent = text;
    btn.className = 'button';
    btn.addEventListener('click', onClick);
    return btn;
}
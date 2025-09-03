export function IconButton(icon, onClick){

    const btn = document.createElement('button');
    btn.className = 'icon-button';
    btn.addEventListener('click', onClick);
    const img = document.createElement('img');
    
    function setIcon(newIcon){
        img.src = newIcon;
    }

    setIcon(icon);
    btn.setIcon = setIcon;

    btn.appendChild(img);
    
    return btn;

}
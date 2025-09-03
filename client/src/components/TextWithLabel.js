export function TextWithLabel(labelText, text, iconPath){

    const column = document.createElement("div");
    column.className = 'text-with-label';
    
    const label_div = document.createElement("div");
    label_div.className = 'label-row';

    const label = document.createElement("label");
    label.textContent = labelText;
    label_div.appendChild(label);

    if (iconPath !== undefined){
        const icon = document.createElement("img");
        icon.className = 'icon-label';
        icon.alt = 'icon';
        icon.src = iconPath;
        label_div.appendChild(icon);
    }

    column.appendChild(label_div);

    const content = document.createElement("div");
    content.className = 'text-content';
    content.textContent = text;

    column.content = content;
    column.appendChild(content);

    return column;
}
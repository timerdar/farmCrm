import { TextWithLabel } from "./TextWithLabel.js";

export function EditableTextWithLabelForString(label, text, onAcceptClick) {

    let currentValue = text;
    const elem = document.createElement("div");
    elem.className = 'editable-text';

    function renderContent() {
        elem.innerHTML = '';

        const textWithLabel = TextWithLabel(label, `${currentValue}`, '/src/assets/edit.png');
        textWithLabel.addEventListener('click', () => renderInput());

        elem.appendChild(textWithLabel);
    }

    function renderInput() {
        elem.innerHTML = '';

        const input = document.createElement('input');
        input.type = 'text';
        input.placeholder = label;

        input.addEventListener('blur', () => {
            if (!input.value) {
                input.value = text;
                renderContent();
            } else {
                onAcceptClick(input.value).then(
                    success => {
                        if (success) {
                            currentValue = input.value;
                        }
                        renderContent();
                    }
                );

            }
        })

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                input.blur();
            }
        })

        elem.appendChild(input);
        input.focus();
    }

    renderContent();
    return elem;
}
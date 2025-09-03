export function ErrorMessage(text){

    const div = document.createElement("div");
    div.className = "error-message";
    div.textContent = text;

    return div;

}
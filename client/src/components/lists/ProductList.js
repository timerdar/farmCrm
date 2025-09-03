import { ProductCard } from "../cards/ProductCard.js";
import { EditableProductCard } from "../cards/editable/EditableProductCard.js";

export function ProductList(productList, isEditable){

    const list = document.createElement("ul");
    
    function render(data){
        list.innerHTML = '';
        data.forEach(product => {
            const li = document.createElement("li");
            if(isEditable){
                li.appendChild(EditableProductCard(product));
            }else{
                li.appendChild(ProductCard(product));
            }
            list.appendChild(li);
        })
    };

    render(productList);

    list.update = render;
    
    return list;

}
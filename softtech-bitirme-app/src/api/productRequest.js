import axios from 'axios';


export const getAllProduct=(pageNumber,pageSize)=>{
   
  
  const url = "/product" + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
  
  return axios.get(url)
    
}

export const NewProduct=(product)=>{
    
    const url = "/product";

    return axios.post(url,product)

}

export const deleteProduct = (productId) => {
    const url = "/product" + "?product_id=" + productId;

    return axios.delete(url);
}
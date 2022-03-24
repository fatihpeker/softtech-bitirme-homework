import React  from "react";
import { Button } from 'semantic-ui-react'
import { deleteProduct } from "../api/productRequest";

 const DelProduct= (props) => {
    const {productId, callback} = props;
    


    const onClick = () => {
        deleteProduct(productId)
        .then((r) => {
            if (r.ok) {
              return r;
            }
            if (r.status === 401 || r.status === 403 || r.status === 500) {
              return Promise.reject(new Error("Bir hata oluştu"));
            }
            return Promise.reject(new Error("Bilinmeyen bir hata oluştu"));
          })
          .catch((e) => {
            console.log(e.message)
          });
        callback();
    }

    return( 
        <Button negative onClick={onClick} >delete</Button>  
    );
     
}

export default DelProduct;
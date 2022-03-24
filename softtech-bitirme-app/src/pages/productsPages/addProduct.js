import React, {useState} from "react";
import Input from "../../component/Input";
import { NewProduct } from "../../api/productRequest";
import { useNavigate } from "react-router-dom";
import {
  Form,
  Button,
  Header,
  Grid,
  GridColumn,
  Segment,
} from "semantic-ui-react";
import TopMenuWithLogin from "../../component/topMenuWithLogin";

const AddProduct = (props) => {
  const [product,setProduct] = useState({id:null, name:"",priceWithoutKdv:null,categoryKdvType:"",barcode:"",stock:null,description:""});
  const [productError,setProductError] = useState({name:null,priceWithoutKdv:null,categoryKdvType:null,barcode:null})

  let navigate = useNavigate();
 
  const handleChange = (event)=>{
    const { name,value } = event.target;
    setProduct({...product, [name]:value});
  }

  
  const handleSubmit = (event) =>{
    event.preventDefault();
    console.log(product)
    if(product.name===""){
        setProductError({...productError,name:"name cannot be null"})
        return;
    }
    else{
        setProductError({...productError,name:""})
    }
    if(product.categoryKdvType===""){
        setProductError({...productError,categoryKdvType:"category cannot be null"})
        return;
    }
    else{
        setProductError({...productError,categoryKdvType:""})
    }
    if(product.priceWithoutKdv===null){
        setProductError({...productError,priceWithoutKdv:"price cannot be null"})
        return;
    }
    else{
        setProductError({...productError,priceWithoutKdv:null})
    }
    

    NewProduct(product)
    .then((r) => {
        if (r.data.success) {
          navigate("/pages/productsPages/product")
        }
        if (r.status === 401 || r.status === 403 || r.status === 500) {
          return Promise.reject(new Error("Bir hata oluştu"));
        }
        return Promise.reject(new Error("Bilinmeyen bir hata oluştu"));
      })
      .catch((e) => {
        console.log(e.message);
      });

  }

  return (
      <div>
          <TopMenuWithLogin></TopMenuWithLogin>
    <Grid textAlign='center' style={{ height: '100vh' }} verticalAlign='middle'>
      <GridColumn style={{ maxWidth: 450 }}>
        <Header as='h1' color="blue" textAlign='center'>
          Product
        </Header>
        <Form size="large" onSubmit={handleSubmit}>
          <Segment stacked  >
            <Input type="text" name="name" label="Name" placeholder="name" required="true" value={product.name} onChange={handleChange} error={productError.name} />
            <Input type="text" name="priceWithoutKdv" label="Price" placeholder="price" required="true" value={product.priceWithoutKdv} onChange={handleChange} error={productError.priceWithoutKdv} />
            <Input type="text" name="categoryKdvType" label="Category" placeholder="category" required="true" value={product.categoryKdvType} onChange={handleChange} error={productError.categoryKdvType} />
            <Input type="text" name="stock" label="Stock" placeholder="stock" required="false" value={product.stock} onChange={handleChange} />
            <Input type="text" name="description" label="Description" placeholder="description" required="false" value={product.description} onChange={handleChange}  />
            <Input type="text" name="barcode" label="Barcode" placeholder="barcode" required="true" value={product.barcode} onChange={handleChange} error={productError.barcode} />
            <Button type="submit" color="blue" size="large" >Submit</Button>
          </Segment>
        </Form>
      </GridColumn>
    </Grid>
    </div>
  );
  
}

export default AddProduct;

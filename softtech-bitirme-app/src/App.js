import './App.css';
import React, {useState} from "react";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import TopMenu from './component/topMenu';
import Login from './pages/authPages/login';
import Register from './pages/authPages/register';
import MainPage from './pages/mainPage';
import Product from './pages/productsPages/product';
import TopMenuWithLogin from './component/topMenuWithLogin';
import AddProduct from './pages/productsPages/addProduct';


function App() {
  
  return (
    <div className="App">
      <Router>
        <Routes>
        <Route path='/' element={<MainPage></MainPage>}></Route>
        <Route path='/pages/authPages/login' element={<Login></Login>}></Route>
        <Route path='/pages/authPages/register' element={<Register></Register>}></Route>
        <Route path='/pages/productsPages/product' element={<Product></Product>}></Route>
        <Route path='/pages/productsPages/addProduct' element={<AddProduct></AddProduct>}></Route>
        <Route path='*' element={<p>404 Not Found</p>}></Route>
        </Routes>
      </Router>
    </div>
  );
}

export default App;

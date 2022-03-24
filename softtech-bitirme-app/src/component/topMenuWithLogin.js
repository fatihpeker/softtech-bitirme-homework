import React from "react";
import { useNavigate } from "react-router-dom";
import { Menu,Button  } from "semantic-ui-react";

const TopMenuWithLogin = (props) => {

    let navigate = useNavigate();

    const goHome = () => {
        navigate("/")
    };

  const goProduct = () => {
    navigate("/pages/productsPages/product")
 };

 const goAccount = () => {
    //navigate
};

const addProduct = () => {
    navigate("pages/productsPages/addProduct")
};

const logout = () => {
    sessionStorage.clear();
    navigate("/")
};

  return (
    <Menu attached="top">
          <Menu.Menu>
          <Menu.Item>
                <Button active onClick={goHome} >What's in</Button>
              </Menu.Item>
          <Menu.Item >
            <Button primary onClick={goProduct}>
              Products
            </Button>
          </Menu.Item>
          <Menu.Item >
            <Button primary onClick={addProduct}>
              Add Product
            </Button>
          </Menu.Item>
          <Menu.Item >
            <Button primary onClick={goAccount}>
              Account
            </Button>
          </Menu.Item>
          </Menu.Menu>

          <Menu.Menu position="right">
              <Menu.Item>
                  <Button negative onClick={logout}>Logout</Button>
              </Menu.Item>
          </Menu.Menu>
        </Menu>
  );
};

export default TopMenuWithLogin;
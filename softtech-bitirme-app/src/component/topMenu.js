import React from "react";
import { Menu,Button } from "semantic-ui-react";
import { useNavigate } from "react-router-dom";

const TopMenu = (props) => {
  
    let navigate = useNavigate();

    const login = () => {
      navigate("/pages/authPages/login")
    };
    const singUp = () => {
        navigate("/pages/authPages/register")
    };
    const goHome = () => {
        navigate("/")
    };
    

    return (
        
      <Menu attached="top">
                <Menu.Item>
                <Menu.Item>
                <Button active onClick={goHome} >What's in</Button>
              </Menu.Item>
              <Button primary onClick={singUp}>
                Sign up
              </Button>
            </Menu.Item>
            
                <Menu.Item>
              <Button positive onClick={login}>Log-in</Button>
            </Menu.Item>
          
         
          </Menu>
    );
};

export default TopMenu;

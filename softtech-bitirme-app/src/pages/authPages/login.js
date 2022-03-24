import React, {useState} from "react";
import { Link } from "react-router-dom";
import Input from "../../component/Input"
import {login} from "../../api/authRequest"
import { useNavigate } from "react-router-dom";
import {
  Form,
  Button,
  Header,
  Grid,
  GridColumn,
  Divider,
  Segment,
} from "semantic-ui-react";
import TopMenu from "../../component/topMenu";

const Login = (props) => {
  const [usernamePassword,setUsernamePassword] = useState({username:"",password:""});
  const [usernamePasswordError, setUsernamePasswordError] = useState({username:null, password:null})

  let navigate = useNavigate();

  const handleChange = (event)=>{
    const { name,value } = event.target;
    setUsernamePassword({...usernamePassword, [name]:value});
  }

  
  const handleSubmit = (event) =>{
    event.preventDefault();
    const { username, password } = usernamePassword;
    if (username.length <3){
      setUsernamePasswordError({ ...usernamePasswordError ,username:"please enter a username witch characters more then 3"});
      return;
    }else{
      setUsernamePasswordError({ ...usernamePasswordError ,username:null});
    }
    if(password.length < 3){
      setUsernamePasswordError({ ...usernamePasswordError ,password:"please enter a username witch characters more then 3"});
      return;
    }
    else{
      setUsernamePasswordError({ ...usernamePasswordError ,password:null});
    }

    login(username,password)
    .then(response => handleResponse(response))
    .then(()=>{navigate("/pages/productsPages/product")})
    .catch(error => console.log(error.message))
    
  }

  const handleResponse = (response)=>{

    console.log(response);

    sessionStorage.setItem('token', response.data.data.token)
    sessionStorage.setItem('username', response.data.data.username);
    sessionStorage.setItem('userId', response.data.data.id);
}

    return (
      <div>
        <TopMenu></TopMenu>
        <Grid textAlign='center' style={{ height: '100vh' }} verticalAlign='middle'>
        <GridColumn style={{ maxWidth: 450 }}>
          <Header as='h1' color="blue" textAlign='center'>
            Login
          </Header>
          <Form size="large" onSubmit={handleSubmit}>
            <Segment stacked  >
              <Input type="text" name="username" label="Username" placeholder="Username" required="true" value={usernamePassword.username} onChange={handleChange} error={usernamePasswordError.username} />
              <Input type="password" name="password" label="Password" placeholder="Password" required="true" value={usernamePassword.password} onChange={handleChange} error={usernamePasswordError.password} />
              <Button type="submit" color="blue" size="large" >Submit</Button>
            </Segment>
          </Form>
          <Divider />
          <p>
            Do you have an account? <Link to="/pages/authPages/register">Register</Link>
          </p>
        </GridColumn>
      </Grid>
      </div>
    );
  
}

export default Login;

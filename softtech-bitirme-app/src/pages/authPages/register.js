import React, {useState} from "react";
import { Link } from "react-router-dom";
import Input from "../../component/Input";
import { singUp } from "../../api/authRequest";
import { useNavigate } from "react-router-dom";
import {
  Form,
  Button,
  Header,
  Grid,
  GridColumn,
  Divider,
  Segment
} from "semantic-ui-react";
import TopMenu from "../../component/topMenu";

const Register = (props) => {
  const [singupField,setSingupField] = useState({username:"",password:"",passwordRepeat:"",name:"",surname:""});
  const [singupFieldError, setsingupFieldError] = useState({username:null, password:null, passwordRepeat:null});

  let navigate = useNavigate();

  const handleChange = (event)=>{
    const { name,value } = event.target;
    setSingupField({...singupField, [name]:value});
  }


  
  const handleSubmit = (event) =>{
    event.preventDefault();
    const { username, password,passwordRepeat,name,surname} = singupField; 
    if (username.length <3){
      setsingupFieldError({ ...singupFieldError ,username:"please enter a username witch characters more then 3"});
      return;
    }else{
      setsingupFieldError({ ...singupFieldError ,username:null});
    }
    if(password.length < 3){
      setsingupFieldError({ ...singupFieldError ,password:"please enter a username witch characters more then 3"});
      return;
    }
    else{
      setsingupFieldError({ ...singupFieldError ,password:null});
    }
    if(password !== passwordRepeat){
      setsingupFieldError({...singupFieldError, password: "password missmatch", passwordRepeat: "password missmatch"})
      return;
    }
    else{
      setsingupFieldError({...singupFieldError, password:null , passwordRepeat:null })
    }
    
    singUp(username,password,name,surname)
    .then(()=>navigate("/pages/authPages/login"))
    .catch((error)=>console.log(error.message))
  }

  return (
    <div>
      <TopMenu></TopMenu>
    <Grid textAlign='center' style={{ height: '100vh' }} verticalAlign='middle'>
      <GridColumn style={{ maxWidth: 450 }}>
        <Header as='h1' color="blue" textAlign='center'>
          Sing Up
        </Header>
        <Form size="large" onSubmit={handleSubmit}>
          <Segment stacked  >
            <Input type="text" name="username" label="Username" placeholder="Username" required="true" value={singupField.username} onChange={handleChange} error={singupFieldError.username} />
            <Input type="password" name="password" label="Password" placeholder="Password" required="true" value={singupField.password} onChange={handleChange} error={singupFieldError.password} />
            <Input type="password" name="passwordRepeat" label="Password Repeat" placeholder="Password Repeat" required="true" value={singupField.passwordRepeat} onChange={handleChange} error={singupFieldError.passwordRepeat} />
            <Input type="text" name="name" label="Name" placeholder="Name" required="false" value={singupField.name} onChange={handleChange}/> 
            <Input type="text" name="surname" label="Surname" placeholder="Surname" required="false" value={singupField.surname} onChange={handleChange}/> 
            <Button type="submit" color="blue" size="large" >Submit</Button>
          </Segment>
        </Form>
        <Divider />
        <p>
          Do you have an account? <Link to="/pages/authPages/login">Login</Link>
        </p>
      </GridColumn>
    </Grid>
    </div>
  );
  
}

export default Register;

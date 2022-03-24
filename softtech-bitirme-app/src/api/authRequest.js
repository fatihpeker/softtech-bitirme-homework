import axios from 'axios';


export const singUp=(username,password,name,surname)=>{
    const data = {
      username: username,
      password: password,
      name: name,
      surname: surname
  }
  
  const url = "/auth/signup"
  
  return axios.post(url, data);
    
}

export const login = (username,password) =>{
  const data = {
    username: username,
    password: password
}

const url = "/auth/signin"

return axios.post(url, data);
}
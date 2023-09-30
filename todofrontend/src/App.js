
import './App.css';
import Login from './components/Login';
import Home from './components/Home';
import { useEffect, useState } from 'react';


function App() {

  const [loggedIn, setLoggedIn] = useState(false);



  const login = () => {
    setLoggedIn(true)
  }

  const checkLogin = async () => {
    if (localStorage.getItem('todo_token')) {
      console.log("token found");
      const data = await fetch('http://localhost:8080/validatetoken', {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",

        },
        method: 'POST',
        body: JSON.stringify({
          token: localStorage.getItem('todo_token')
        })
      });
      const resp = await data.json();
      console.log(resp);
      if (resp.valid == "1") {
        setLoggedIn(true)
        return;
      } 

    }

    localStorage.removeItem('todo_token');
    setLoggedIn(false);
  }


  useEffect(() => {
    checkLogin();
  }, [])

  return (
    <div className="App">
  
  { loggedIn ? <Home />:<Login login={login}/>}
    </div>
  );
}

export default App;

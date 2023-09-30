import React, { useState } from "react";

export default function Login(props) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            
            const res = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*",

                },
                body: JSON.stringify({
                    username,
                    password,

                }),
             

            });


            const data = await res.json();
            
            if (data.sessiontoken) {
                localStorage.setItem("todo_token", data.sessiontoken);
                props.login();
            } else {
                    console.log(data);
                }

        } catch (error) {
            console.log(error);
        }
    };

    const handleRegistrer = async (e) => {
        e.preventDefault();
        try {
            
            const res = await fetch("http://localhost:8080/registrer", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*",

                },
                body: JSON.stringify({
                    username,
                    password,

                }),
             

            });

            const data = await res.json();
           
            if (data.sessiontoken) {
                localStorage.setItem("todo_token", data.sessiontoken);
                props.login();
            } else {
                    console.log(data);
                }
    
        } catch (error) {
            console.log(error);
        }
    }


    return (
        <div className="login">
            <h1>Login</h1>
            <form onSubmit={handleLogin}>
                <input
                    type="name"
                    placeholder="Username"
                    onChange={(e) => setUsername(e.target.value)}
                    value={username}
                    

                />
                <input
                    type="password"
                    placeholder="Password"
                    onChange={(e) => setPassword(e.target.value)}
                    value={password}
                />
                <button type="submit">Login</button>
            </form>
            <h1>Registrer</h1>
            <form onSubmit={handleRegistrer}>
                <input
                    type="name"
                    placeholder="Username"
                    onChange={(e) => setUsername(e.target.value)}
                    value={username}
                    

                />
                <input
                    type="password"
                    placeholder="Password"
                    onChange={(e) => setPassword(e.target.value)}
                    value={password}
                />
                <button type="submit">Registrer</button>
            </form>
        </div>
    );
}
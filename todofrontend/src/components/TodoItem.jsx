import  React, {useState} from "react";


export default function TodoItem(props) {


    const handleChange = async (e) => {
        const res = await fetch("http://localhost:8080/updatetodo", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Access-Control-Allow-Origin": "*",
            },
            body: JSON.stringify({
                id: props.data.id,
                finished: e.target.checked ? "1" : "0",
                token: localStorage.getItem("todo_token"),
                textcontent: props.data.textcontent,
            }),
        });
        const data = await res.json();
      
        if (data.error) {
            console.log(data);
        } else {
        
            props.setTodos(data);
        }
    }
            
        
    return (
        <div 
        style={
            {
                border: ".5px solid black",
                margin: "10px",
                borderRadius: "5px",
                margin: "20px auto",
                
             
                height: "50px",
                display: "flex",
                textAlign: "center",
                justifyContent: "center",
                width: "700px",
            }
        }
        
        >
            
            <input type="checkbox" checked={props.data.finished}  onChange={(e) => {
                handleChange(e);
            }} />
            <p
        
            style={
                {
                    textDecoration: props.data.finished ? "line-through" : "none",
                    margin: "auto",
                    width: "80%",
                    textAlign: "center",
                }
            }
            >{props.data.textcontent}</p>
        </div>
    );
}
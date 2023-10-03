import { useEffect } from "react";
import { useState } from "react";
import TodoItem from "./TodoItem";
import CreateTodo from "./CreateTodo";

export default function TodoComponent() { 
    const [todos, setTodos] = useState([]);


    const getTodos = async() => {
        const token = localStorage.getItem("todo_token");
        const res = await fetch("http://localhost:8080/gettodos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Access-Control-Allow-Origin": "*",

            },
            body: JSON.stringify({
                token: token
            }),
        });

        const data = await res.json();
        console.log(data);
        if (data.length > 0) {
            
            setTodos(data);
        } else {
            setTodos([]);
        }
    }
    useEffect(() => {
        getTodos();
    }, []);
    return (
        <div 
        style={{
      width: "100%",
        }}>
            <CreateTodo refetch={getTodos}/>
            {todos.map((todo, index) => 
        
                    <TodoItem setTodos={setTodos} key={index} data={todo}/>
                
        )}
        </div>
    );


};
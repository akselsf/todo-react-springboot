import { useState } from "react";

export default function CreateTodo(props) {

    const [text, setText] = useState("");

    const handleAdd = async () => {
        if (text != "") {
            setText("");
            const res = await fetch("http://localhost:8080/addtodo", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*",
                },
                body: JSON.stringify({
                    textcontent: text,
                    token: localStorage.getItem("todo_token"),
                }),
            });
            const data = await res.json();
            console.log(data);
            if (data.error) {
                console.log(data);
            } else if (data.success && data.success == "1") {
                props.refetch();
            }
        }
    }

    return (
        <div>
            <input type="text" value={text} onChange={
                (e) => setText(e.target.value)
            } />
            <button onClick={() => handleAdd()}>Add</button>
        </div>
    );
}
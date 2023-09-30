import  React, {useState} from "react";


export default function TodoItem(props) {

    const [striked, setStriked] = useState(false);

    const handleChange = () => {
        setStriked(!striked);
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
            
            <input type="checkbox" onChange={() => {
                handleChange();
            }} />
            <p
        
            style={
                {
                    textDecoration: striked ? "line-through" : "none",
                    margin: "auto",
                    width: "80%",
                    textAlign: "center",
                }
            }
            >{props.data.textcontent}</p>
        </div>
    );
}
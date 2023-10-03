package com.todo.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class TodoController {

    @PostMapping("/gettodos")
    public List<TodoItem> getTodos(@RequestBody Map<String, String> body) {
        String sessiontoken = body.getOrDefault("token", null);

        if (sessiontoken == null || sessiontoken.length() == 0) {
             
            return new ArrayList<TodoItem>();
        }
        if (!Database.checkSessionToken(sessiontoken)) {
            
            return new ArrayList<TodoItem>();
        }

        UserAccount user = Database.getUser(sessiontoken);
       
        if (user == null) {
            return new ArrayList<TodoItem>();
        }
        return Database.getTodos(user.id());

    }

    @PostMapping("/addtodo")
    public Map<String, String> addTodo(@RequestBody Map<String, String> body) {
        String textcontent = body.getOrDefault("textcontent", null);
        String sessiontoken = body.getOrDefault("token", null);

        Map<String, String> result = new HashMap<String, String>();
        if (textcontent == null || textcontent.length() == 0) {
            result.put("error", "textcontent is empty");
            return result;
        }
        if (sessiontoken == null || sessiontoken.length() == 0) {
            result.put("error", "sessiontoken is empty");
            return result;
        }

        if (!Database.checkSessionToken(sessiontoken)) {
            result.put("error", "sessiontoken is invalid");
            return result;
        }
        UserAccount user = Database.getUser(sessiontoken);
        if (user == null) {
            result.put("error", "sessiontoken is invalid");
            return result;
        }

        Database.createTodo(textcontent, user.id());
        result.put("success", "1");
        return result;
    }

    @GetMapping("/gettoken")
    public void gettoken(int userid) {
        Database.createSessionToken(userid);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Map<String, String> result = new HashMap<String, String>();
        if (username == null || username.length() == 0) {
            result.put("error", "username is empty");
            return result;
        }
        if (password == null || password.length() == 0) {
            result.put("error", "password is empty");
            return result;
        }

        int loginSuccess = Database.login(username, password);
        if (loginSuccess == -1) {
            result.put("error", "couldnt log in");
            return result;
        }

        String sessionToken = Database.createSessionToken(loginSuccess);
        result.put("sessiontoken", sessionToken);
        return result;

    }

    @PostMapping("/registrer")
    public Map<String, String> register(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");
        Map<String, String> result = new HashMap<String, String>();
        if (username == null || username.length() == 0) {
            result.put("error", "username is empty");
            return result;
        }
        if (password == null || password.length() == 0) {
            result.put("error", "password is empty");
            return result;
        }

        int registerSuccess = Database.register(username, password);
        if (registerSuccess == -1) {
            result.put("error", "couldnt register");
            return result;
        }

        String sessionToken = Database.createSessionToken(registerSuccess);
        result.put("sessiontoken", sessionToken);
        return result;

    }

    @PostMapping("/validatetoken")
    public Map<String, String> validatetoken(@RequestBody Map<String, String> body) {
        String sessiontoken = body.get("token");

        Map<String, String> result = new HashMap<String, String>();
        if (sessiontoken == null || sessiontoken.length() == 0) {
            result.put("error", "sessiontoken is empty");
            return result;
        }
        if (!Database.checkSessionToken(sessiontoken)) {
            result.put("error", "sessiontoken is invalid");
            return result;
        }

        result.put("valid", "1");
        return result;
    }

    @PostMapping("/updatetodo") 
    public List<TodoItem> updatetodo(@RequestBody Map<String, String> body) {
        String sessiontoken = body.get("token");
        String textcontent = body.get("textcontent");
        String id = body.get("id");
        String finished = body.get("finished");

        if (sessiontoken == null || sessiontoken.length() == 0) {
            return new ArrayList<TodoItem>();
        }

         if (!Database.checkSessionToken(sessiontoken)) {
            
            return new ArrayList<TodoItem>();
        }
        
        UserAccount user = Database.getUser(sessiontoken);
        if (user == null) {
            return new ArrayList<TodoItem>();
    
        }


        Database.updateTodo(
            user.id(),
            Integer.parseInt(id),
            textcontent,
            Integer.parseInt(finished)
        );

        List<TodoItem> todos = Database.getTodos(user.id());
        return todos;
    }

}

package com.todo.todo;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class EncryptionHandler {
    public static String encrypt(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
    
}


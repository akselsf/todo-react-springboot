package com.todo.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

  private static Connection getRemoteConnection() {
    try {
      SetUpEnv.setProperties();
    } catch (Exception e) {
      System.out.println("Couldnt set properties");
    }

    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

      String dbName = System.getProperty("AZURE_DBNAME");
      String userName = System.getProperty("AZURE_USERNAME");
      String password = System.getProperty("AZURE_PASSWORD");
      String hostname = System.getProperty("AZURE_HOSTNAME");
      String port = System.getProperty("AZURE_PORT");
      String emaildomain = System.getProperty("AZURE_EMAILDOMAIN");

      String jdbcUrl = "jdbc:sqlserver://" + hostname + ":" + port + ";database=" + dbName + ";user=" + userName + "@"
          + emaildomain + ";password=" + password
          + ";encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";
      System.out.println("Getting remote connection with connection string from environment variables.");
      Connection con = DriverManager.getConnection(jdbcUrl);
      System.out.println("Remote connection successful.");
      return con;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static List<TodoItem> getTodos(int useraccount_id) {
    List<TodoItem> todos = new ArrayList<TodoItem>();

    Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return new ArrayList<TodoItem>();
    }

    String query = "select id, textcontent, finished from todoitem where useraccount_id = " + useraccount_id + "";
    try (Statement stmt = con.createStatement()) {
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        String textcontent = rs.getString("textcontent");
        int id = rs.getInt("id");
       
        int finished = rs.getInt("finished");
       

        TodoItem todo = new TodoItem(id, textcontent, finished == 0 ? false : true);
        todos.add(todo);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return todos;

  }

  public static void deleteTodo(int id, int useraccount_id) {
    Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return;
    }

    String query = "delete from todoitem where id = " + id + " and useraccount_id = " + useraccount_id + "";
    try (Statement stmt = con.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void finishTodo(int id, int useraccount_id) {
    Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return;
    }

    String query = "update todoitem set finished = 1 where id = " + id + " and useraccount_id = " + useraccount_id + "";
    try (Statement stmt = con.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void unFinishTodo(int id, int useraccount_id) {
    Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return;
    }

    String query = "update todoitem set finished = 0 where id = " + id + " and useraccount_id = " + useraccount_id + "";
    try (Statement stmt = con.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void createTodo(String textcontent, int useraccount_id) {
    Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return;
    }

    String query = "insert into todoitem (textcontent, finished, useraccount_id) values ('" + textcontent + "', 0, "
        + useraccount_id + ")";
    try (Statement stmt = con.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static boolean checkSessionToken(String sessionToken) {

    Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return false;
    }

    String query = "select useraccount_id from sessiontoken where token = '" + sessionToken + "'";
    try (Statement stmt = con.createStatement()) {
      ResultSet rs = stmt.executeQuery(query);

      if (rs.next()) {
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return false;
  }

  public static UserAccount getUser(String sessiontoken) {
    Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return null;
    }

    String query = "select sessiontoken.useraccount_id as useraccount_id, username, passwordhash from sessiontoken INNER JOIN useraccount ON sessiontoken.useraccount_id = useraccount.id where token = '" + sessiontoken + "'";
    System.out.println(query);
    try (Statement stmt = con.createStatement()) {
      ResultSet rs = stmt.executeQuery(query);

      if (rs.next()) {
        int useraccount_id = rs.getInt("useraccount_id");
        String username = rs.getString("username");
        String passwordhash = rs.getString("passwordhash");
        return new UserAccount(useraccount_id, username, passwordhash);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;

  }

  public static String createSessionToken(int useraccount_id) {

      Connection con = getRemoteConnection();
      if (con == null) {
        System.out.println("Connection is null");
        return null;
      }

      String query = "delete from sessiontoken where useraccount_id = " + useraccount_id + "";
      try (Statement stmt = con.createStatement()) {
        stmt.executeUpdate(query);
      } catch (SQLException e) {
        e.printStackTrace();
        return null;
      }

      String sessiontoken = java.util.UUID.randomUUID().toString() +"__"+ useraccount_id;
      
      query = "insert into sessiontoken (token, useraccount_id, expire) values ('" + sessiontoken + "', " + useraccount_id + ", DATEADD(day, 1, GETDATE()))";
      try (Statement stmt = con.createStatement()) {
        stmt.executeUpdate(query);
      } catch (SQLException e) {
        e.printStackTrace();
        return null;
      }

      return sessiontoken;

  }

public static int login(String username, String password) {
  String pass = EncryptionHandler.encrypt(password);
  Connection con = getRemoteConnection();
  if (con == null) {
    System.out.println("Connection is null");
    return -1;
  }

  String query = "select id, passwordhash from useraccount where username = '" + username + "'";
  try (Statement stmt = con.createStatement()) {
    ResultSet rs = stmt.executeQuery(query);

    if (rs.next()) {
      int useraccount_id = rs.getInt("id");
      String passwordhash = rs.getString("passwordhash");
      if (EncryptionHandler.checkPassword(password, passwordhash)) {
        return useraccount_id;
      }
   
    }

  } catch (SQLException e) {
    e.printStackTrace();
  }
  return -1;

}

public static int register(String username, String password) {
  

  Connection con = getRemoteConnection();
  if (con == null) {
    System.out.println("Connection is null");
    return -1;
  }

  // check if username in use

  String query = "select id from useraccount where username = '" + username + "'";
 
  try (Statement stmt = con.createStatement()) {
    ResultSet rs = stmt.executeQuery(query);

    if (rs.next()) {
      return -1;
    }

  } catch (SQLException e) {
    e.printStackTrace();
  }
String pass = EncryptionHandler.encrypt(password);
  query = "insert into useraccount (username, passwordhash) values ('" + username + "', '" + pass + "')";

  try (Statement stmt = con.createStatement()) {
    stmt.executeUpdate(query);
  } catch (SQLException e) {
    e.printStackTrace();
    return -1;
  }

  
  query = "select id from useraccount where username = '" + username + "' and passwordhash = '" + pass + "'";
 
  try (Statement stmt = con.createStatement()) {
    ResultSet rs = stmt.executeQuery(query);

    if (rs.next()) {
      int useraccount_id = rs.getInt("id");
      return useraccount_id;
    }

  } catch (SQLException e) {
    e.printStackTrace();
  }
  return -1;

}

public static void updateTodo(int userid, int todoid, String textcontent, int finished) {
   Connection con = getRemoteConnection();
    if (con == null) {
      System.out.println("Connection is null");
      return;
    }

    String query = "update todoitem set textcontent = '" + textcontent + "', finished = " + finished + " where id = " + todoid + " and useraccount_id = " + userid + "";
    try (Statement stmt = con.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      con.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
}
}

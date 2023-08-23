package src;

import java.sql.*;
 
public class GFG {
    public static void main(String arg[])
    {
        Connection connection = null;
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydatabase",
                "root", "Bdvej746Js$2jd");
 
            // mydb is database
            // mydbuser is name of database
            // mydbuser is password of database
 
            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(
                "select * from audit_id");
            int id;
            String audit_name;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                audit_name = resultSet.getString("audit_name").trim();
                System.out.println("ID : " + id
                                   + " Audit Name : " + audit_name);
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
    } // function ends
} // class ends
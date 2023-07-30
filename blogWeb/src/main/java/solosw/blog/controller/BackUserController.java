package solosw.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solosw.blog.service.MysqlService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
public class BackUserController {
   @RequestMapping("/searchuser")
    public User searchUser(String data) throws Exception
    {
        Connection connection = MysqlService.getConnection();
        String sql = "select * FROM users WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,data);
        ResultSet resultSet= statement.executeQuery();
        if(resultSet.next())
        {
            String username= resultSet.getString("name");
            String password= resultSet.getString("password");
            String email= resultSet.getString("email");
            int dif=resultSet.getInt("difference");
            return new User(username,email,password,dif);
        }
        return null;
    }
    @RequestMapping("/deleteuser")
    public ResponseEntity<Boolean> deleteUser(String data)
    {
        try {
            Connection connection = MysqlService.getConnection();
            String sql = "DELETE FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, data);
            int rowsAffected = statement.executeUpdate();
            return ResponseEntity.ok(rowsAffected>0);
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @RequestMapping("/changestatus")
    public ResponseEntity<Boolean> changeStatus(String data)
    {
        try {
            Connection connection = MysqlService.getConnection();
            String sql = "update users set difference = !difference WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, data);
            int rowsAffected = statement.executeUpdate();
            return ResponseEntity.ok(rowsAffected>0);
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @RequestMapping("/getuser")
    public ArrayList<User> getUser() throws  Exception
    {
        Connection connection = MysqlService.getConnection();
        String sql = "select * FROM users";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet= statement.executeQuery();
        ArrayList<User> all=new ArrayList<>();
        int maxnum=50;
        int temp=0;
        while (resultSet.next())
        {
            if(temp>maxnum) break;;
            String username= resultSet.getString("name");
            String password= resultSet.getString("password");
            String email= resultSet.getString("email");
            int dif=resultSet.getInt("difference");
            all.add( new User(username,email,password,dif));
            temp++;
        }
        return all;
    }
}
class User{
    private String username,email,password;
    int status;
    public User(String username,String email,String password,int status)
    {
        this.username=username;
        this.email=email;
        this.password=password;
        this.status=status;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
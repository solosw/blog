package solosw.blog.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solosw.blog.service.MysqlService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
public class LoginController {

    @RequestMapping("/login.action")
    public String login(@RequestBody LoginForm loginForm,HttpServletRequest request) throws  Exception{
        // 处理登录逻辑，比如校验账号密码等
        ObjectMapper objectMapper = new ObjectMapper();
        if(loginCheck(loginForm.email,loginForm.password))
        {
                String ip=   getClientIp(request);

            try{
                String sql = "INSERT INTO login (email,ip) VALUES (?, ?)";
                PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
                statement.setString(1,loginForm.email);
                statement.setString(2, ip);
                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " rows affected");
            }catch (Exception e)
            {
                System.out.println(e);
                return objectMapper.writeValueAsString(new RegisterResponse("登陆异常",false));
            }
        }
        // 返回响应
        return objectMapper.writeValueAsString(new RegisterResponse("登陆成功",true));
    }
    boolean loginCheck(String em,String pwd) throws Exception
    {
        String sql = "SELECT * FROM users where email = ? and password = ?";
        PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setString(1,em);
        statement.setString(2,pwd);
        // 执行查询操作，获得结果集
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println("matching records found.");
            resultSet.close();
            statement.close();
            return true;
        } else {
            System.out.println("No matching records found.");
            resultSet.close();
            statement.close();
            return false;
        }


    }
    public static  String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
 class LoginForm {
    public String email;
    public String password;

    // 省略getter和setter方法
}
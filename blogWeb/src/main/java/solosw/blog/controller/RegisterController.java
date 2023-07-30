package solosw.blog.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solosw.blog.service.MysqlService;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Random;

@RestController
public class RegisterController {
    public static HashMap<String, String> codeDir = new HashMap<String, String>();
    @Autowired
    private JavaMailSender javaMailSender;

    @RequestMapping("/register")
    public String registerUser(@RequestBody UserDTO user) throws Exception {
        // 处理用户注册逻辑，可以将用户信息存入数据库等
        // System.out.println(userDTO.checkpwd);
        ObjectMapper objectMapper = new ObjectMapper();
        // 将对象转换为JSON字符串
        if (codeDir.get(user.email).equals(user.checkpwd)) {
            System.out.println("验证成功");
            try {
                String sql = "INSERT INTO users (name, email,password) VALUES (?, ?,?)";
                PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
                statement.setString(1, user.username);
                statement.setString(2, user.email);
                statement.setString(3, user.password);
                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " rows affected");
                codeDir.remove(user.email);
                if (rowsAffected == 0)
                    return objectMapper.writeValueAsString(new RegisterResponse("用户已存在", false));
            } catch (Exception e) {
                System.out.println(e);
                codeDir.remove(user.email);
                return objectMapper.writeValueAsString(new RegisterResponse("注册失败", false));
            }

        } else {
            return objectMapper.writeValueAsString(new RegisterResponse("验证码错误", false));
        }
        codeDir.remove(user.email);
        return objectMapper.writeValueAsString(new RegisterResponse("注册成功", true));
    }

    @RequestMapping("/checkpwd")
    public ResponseEntity<Boolean> sendCode(String email)  {
        String randomCode = generateRandomCode(4);
        System.out.println(email + ":" + randomCode);
        if (codeDir.containsKey(email)) {
            codeDir.remove(email);
            codeDir.put(email, randomCode);
        } else {
            codeDir.put(email, randomCode);
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1595704581@qq.com");
            message.setTo(email);
            message.setSubject("欢迎注册Solosw的博客");
            message.setText("验证码:" + randomCode);
            javaMailSender.send(message);
            System.out.println(email + "发送成功");
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            System.out.println("Exception:"+e.toString());
            return ResponseEntity.ok(false);
        }

    }

    private String generateRandomCode(int length) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }

}

class UserDTO {
    public String password, email, checkpwd, username;
}

class RegisterResponse {
    private String msy;
    private boolean status;

    public RegisterResponse() {
        // 无参构造函数
    }

    public RegisterResponse(String msy, boolean status) {
        this.msy = msy;
        this.status = status;
    }

    public String getMsy() {
        return msy;
    }

    public void setMsy(String msy) {
        this.msy = msy;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
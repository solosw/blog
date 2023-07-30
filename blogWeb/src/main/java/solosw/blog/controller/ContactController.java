package solosw.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ContactController {
    @Autowired
    private JavaMailSender javaMailSender;
    @RequestMapping("/contact.action")
    public ResponseEntity<Boolean> SendContent(@RequestBody SendUser user )throws  Exception
    {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1595704581@qq.com");
            message.setTo("qq318210803@outlook.com");
            message.setSubject("留言");
            message.setText(user.name+":\n"+user.msy+"\n"+user.email);
            //发送邮件
            javaMailSender.send(message);
            return ResponseEntity.ok(true);
    }
}
class SendUser{
    public  String name,email,msy;
}
package solosw.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solosw.blog.service.MysqlService;

import javax.xml.stream.events.Comment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
public class ArticleShow {
    @RequestMapping("/sendmark")
    public String sendMark(HttpServletRequest request,MarkBody markBody) throws Exception
    {
        String ip =LoginController.getClientIp(request);
        Connection connection = MysqlService.getConnection();
        String sql = "select users.name from login,users WHERE login.ip = ? and users.email =login.email";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,ip);
        ResultSet resultSet = statement.executeQuery();
        String uname="";
        if(resultSet.next()) {
            uname=resultSet.getString("name");

        }
        if(uname==null||uname.length()==0||uname.isEmpty()) return uname;
        sql ="insert into comment(name,time,content,title,kind) values(?,?,?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1,uname);
        statement.setString(2,markBody.getDate());
        statement.setString(3,markBody.getComment());
        statement.setString(4,markBody.getTitle());
        statement.setString(5,markBody.getKind());
        int rows =statement.executeUpdate();
        return  uname;
    }

    @RequestMapping("/getmark")
    public ArrayList<CommentResponse> getMark(Article data) throws Exception
    {

        Connection connection = MysqlService.getConnection();
        String sql = "select name,time,content from comment WHERE title = ? and kind = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,data.getTitle());
        statement.setString(2, data.getKind());
        ResultSet resultSet = statement.executeQuery();
        ArrayList<CommentResponse> responses=new ArrayList<>();
        while(resultSet.next()) {
            String name =resultSet.getString("name");
            String time =resultSet.getString("time");
            String content =resultSet.getString("content");
            responses.add(new CommentResponse(time,name,content));

        }



        return  responses;
    }
}
class CommentResponse{
    private String date,name,comment;
    public CommentResponse(String date,String name ,String Comment){
        this.comment=Comment;
        this.date=date;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
class MarkBody
{
    private String date,kind,title,comment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
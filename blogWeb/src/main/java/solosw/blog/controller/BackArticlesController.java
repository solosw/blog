package solosw.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import solosw.blog.service.MysqlService;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class BackArticlesController {
   @GetMapping ("/manager")
   public ModelAndView getManager(HttpServletRequest request) throws Exception
   {
       String ip=LoginController.getClientIp(request);
       String sql ="select  users.difference from login,users where login.ip = ? and users.email = login.email";
       PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
       statement.setString(1,ip);
       ResultSet resultSet=statement.executeQuery();
       int status=-1;
       if(resultSet.next())
       {
           status=resultSet.getInt("difference");
       }
       System.out.println(status);
       if (status <= 0) {
           return new ModelAndView(new RedirectView("/index.html")); // 使用重定向
       }
       return new ModelAndView(new RedirectView("/backarticles.html")); // 使用重定向
   }
    @PostMapping("/uploadArticle")
    public ResponseEntity<String> uploadArticle(@RequestBody ArticleData data) throws Exception{
       String kind = data.kind;
       String image=data.imagebase;
       String title=data.title;
       String content=data.content;
        String sql = "INSERT INTO articlekind (kind, image) VALUES (?, ?)";
        PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
        // 在这里处理接收到的文章数据
        // 可以进行保存到数据库、存储图片等操作
        if(image!=null)
        {
            statement.setString(1, kind);
            statement.setString(2, image);
            // 执行SQL语句
            statement.executeUpdate();
        }
        // 设置参数
        // 关闭资源
       // statement.close();
         sql = "INSERT INTO article (content,kind,title) VALUES (?, ?,?)";
        statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setString(1,content);
        statement.setString(2,kind);
        statement.setString(3,title);
        statement.executeUpdate();
        sql ="update bloginf set articlenum = articlenum + ? where htmlname = ?";
        statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setInt(1,1);
        statement.setString(2,"index");
        statement.executeUpdate();
        // 关闭资源
        statement.close();
        return ResponseEntity.ok("文章上传成功！");
    }
    @PostMapping("/uploadArticlewithoutkind")
    public ResponseEntity<String> uploadArticlewithoutkind(@RequestParam("kind") String kind,
                                                @RequestParam("title") String title,
                                                @RequestParam("content") String content) throws Exception{
        String   sql = "INSERT INTO article (content,kind,title) VALUES (?, ?,?)";
        PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
        statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setString(1,content);
        statement.setString(2,kind);
        statement.setString(3,title);
        statement.executeUpdate();
        sql ="update bloginf set articlenum = articlenum + ? where htmlname = ?";
        statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setInt(1,1);
        statement.setString(2,"index");
        statement.executeUpdate();
        // 关闭资源
        statement.close();
        return ResponseEntity.ok("文章上传成功！");
    }
    @RequestMapping("/deletelist")
    public ArrayList<Article> deleteList() throws Exception
    {
        Connection connection = MysqlService.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT kind,title FROM article");
        ResultSet resultSet = statement.executeQuery();
        int maxCount=10;
        int temp=0;
        ArrayList<Article> all=new ArrayList<>();
       while (resultSet.next()) {
           if(temp>maxCount)break;
           String title=resultSet.getString("title");
           String kind=resultSet.getString("kind");
           temp++;
           all.add(new Article(title,kind));
        }
        return  all;
    }
    @RequestMapping("/deletearticle")
    public ResponseEntity<Boolean> deleteArticle(Article data) throws Exception
    {
        try {
            Connection connection = MysqlService.getConnection();
            String sql = "DELETE FROM article WHERE kind = ? AND title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, data.getKind());
            statement.setString(2, data.getTitle());
            int rowsAffected = statement.executeUpdate();
            sql ="update bloginf set articlenum = articlenum - ? where htmlname = ?";
             statement = connection.prepareStatement(sql);
             statement.setInt(1,rowsAffected);
             statement.setString(2,"index");
             statement.executeUpdate();
            return ResponseEntity.ok(rowsAffected>0);
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @RequestMapping("/searchdelete")
    public ArrayList<Article> searchDelete(String data) throws Exception
    {

            Connection connection = MysqlService.getConnection();
            String sql = "select kind,title from article WHERE kind like ? or title like ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%"+data+"%");
            statement.setString(2, "%"+data+"%");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Article> all=new ArrayList<>();
        while (resultSet.next()) {
            String title=resultSet.getString("title");
            String kind=resultSet.getString("kind");
            all.add(new Article(title,kind));
        }
        return all;
            // 处理异常情况


    }


}
class ArticleData{
    public String kind,title,content,imagebase;
}
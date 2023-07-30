package solosw.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import solosw.blog.service.MysqlService;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class IndexController {
    @GetMapping("/")
    public ModelAndView getIndexPage() throws  Exception
    {
        System.out.println("访问");
       String sql ="update bloginf set looknum =looknum + ? where htmlname = ?";
        PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setInt(1,1);
        statement.setString(2,"index");
        statement.executeUpdate();
        return new ModelAndView("/index.html");
    }
    @PostMapping("/sign")
    public ResponseEntity<String> getSignPage() {
        // 处理Ajax请求的业务逻辑
        // request 包含Ajax请求发送的数据
        // 构造响应的DTO，包含模型数据和视图信息
        return ResponseEntity.ok("sign.html");
    }
    @PostMapping("/login")
    public ResponseEntity<String> getLoginPage() {
        // 处理Ajax请求的业务逻辑
        // request 包含Ajax请求发送的数据

        // 构造响应的DTO，包含模型数据和视图信息

        return ResponseEntity.ok("login.html");
    }
    @PostMapping("/getindex")
    public ResponseEntity<String> getIndexPage1() {
        // 处理Ajax请求的业务逻辑
        // request 包含Ajax请求发送的数据

        // 构造响应的DTO，包含模型数据和视图信息

        return ResponseEntity.ok("index.html");
    }
    @RequestMapping("/islogin")
    public boolean checkIsLogin(HttpServletRequest request) throws Exception
    {

        String ip=LoginController.getClientIp(request);
        String sql = "SELECT * FROM login where ip = ?";
        PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setString(1,ip);
        // 执行查询操作，获得结果集
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
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
    @RequestMapping("/getname")
    public String getLoginName(HttpServletRequest request) throws Exception
    {
        String ip=LoginController.getClientIp((request));
        String sql = "SELECT users.name FROM login,users where ip = ? and login.email= users.email";
        PreparedStatement statement = MysqlService.getConnection().prepareStatement(sql);
        statement.setString(1,ip);
        String name="";
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println("matching records found.");
            name=resultSet.getString(1);
            resultSet.close();
            statement.close();
            return name;
        } else {
            System.out.println("No matching records found.");
            resultSet.close();
            statement.close();
            return "已登录";
        }
    }

    @RequestMapping("/getindexarticle")
    public ArrayList<Article> getIndexArticle() throws Exception
    {
        Connection connection = MysqlService.getConnection();
        ArrayList<Article> all=new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT kind,title FROM article");
        ResultSet resultSet = statement.executeQuery();
        int MaxCount =20;
        int temp= 0;
        while (resultSet.next()) {
            if(temp>MaxCount) break;
            String kindName = resultSet.getString("kind");
            String title = resultSet.getString("title");
            all.add(new Article(title,kindName));
            temp++;
        }
        resultSet.close();
        statement.close();
        return all;
    }

    @RequestMapping("/getIndexArticle")
    public String getIndexArticle(Article data) throws Exception
    {
        String url="/articleshow.html?kind="+data.getKind()+"&"+"title="+data.getTitle();
        return url;

    }

    @RequestMapping("/getIndexArticleBySearch")
    public ArrayList<Article> getIndexArticleBySearch(String data) throws Exception
    {
        Connection connection = MysqlService.getConnection();
        ArrayList<Article> all=new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT kind,title FROM article where kind like ? or title like ?");
        statement.setString(1, "%"+data+"%");
        statement.setString(2, "%"+data+"%");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {

            String kindName = resultSet.getString("kind");
            String title = resultSet.getString("title");
            all.add(new Article(title,kindName));
        }
        resultSet.close();
        statement.close();
        return all;
    }

    @RequestMapping("/getbogInf")
    public BlogInformation getBogInf() throws Exception
    {
        Connection connection = MysqlService.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT looknum,articlenum FROM bloginf where htmlname =?");
        statement.setString(1, "index");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {


            return new BlogInformation(resultSet.getInt("looknum"),resultSet.getInt("articlenum"));

        }
        resultSet.close();
        statement.close();
        return null;
    }


}
class BlogInformation{
    public  int looknum,articlenum;
    public  BlogInformation(int looknum,int articlenum)
    {
        this.looknum=looknum;
        this.articlenum=articlenum;
    }
}

package solosw.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solosw.blog.service.MysqlService;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class ArticlesController {
    @ RequestMapping("/getcontent")
    public String getArticleContent(Article data) throws Exception
    {

        Connection connection = MysqlService.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT content FROM article where kind = ? and title = ?");
        statement.setString(1,data.getKind());
        statement.setString(2,data.getTitle());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("content");
        }
        return "";
    }
   @ RequestMapping("/articleshow")
    public String getArticleShow(Article data) throws Exception
   {
       String url="/articleshow.html?kind="+data.getKind()+"&"+"title="+data.getTitle();
       return url;
   }
    @GetMapping("/getkind")
    public ResponseEntity<ArrayList<KindInf>> getArticleKind() throws Exception {
        ArrayList<KindInf> all = getAllKind();
        return ResponseEntity.ok(all);
    }
    @RequestMapping("/getlist")
    public String getArticles( String data)
    {
        String url="/articlelist.html?data=" + data;
        return url;
    }
    @RequestMapping("/getarticlelist")
    public ResponseEntity<ArrayList<Article>> getArticleList(String data) throws Exception
    {
        Connection connection = MysqlService.getConnection();
        ArrayList<Article> all=new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT kind,title FROM article where kind like ? or title like ?");
        statement.setString(1,"%"+data+"%");
        statement.setString(2,"%"+data+"%");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String kindName = resultSet.getString("kind");
            String title = resultSet.getString("title");
           all.add(new Article(title,kindName));

        }

        resultSet.close();
        statement.close();
        return ResponseEntity.ok(all);
    }
    @RequestMapping("/listshow")
    public String listShow( String data)
    {
        return data;
    }
    private ArrayList<KindInf> getAllKind() throws Exception {
        ArrayList<KindInf> kindInfs = new ArrayList<>();
        Connection connection = MysqlService.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM articlekind");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String kindName = resultSet.getString("kind");
            String imageStream = resultSet.getString("image");
            // 将图片数据转换为 Base64 编码
            KindInf kindInf = new KindInf(kindName, imageStream);
            kindInfs.add(kindInf);
        }

        resultSet.close();
        statement.close();

        return kindInfs;
    }

    class KindInf {
        private String name;
        private String imageData;

        public KindInf() {
        }

        public KindInf(String name, String imageData) {
            this.name = name;
            this.imageData = imageData;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageData() {
            return imageData;
        }

        public void setImageData(String imageData) {
            this.imageData = imageData;
        }
    }
}
class Article{
    private String title;
    private  String kind;
    public Article(String title,String kind)
    {
        this.kind=kind;
        this.title=title;
    }
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
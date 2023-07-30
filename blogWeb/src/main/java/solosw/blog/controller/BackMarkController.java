package solosw.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solosw.blog.service.MysqlService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
public class BackMarkController {

    @RequestMapping("/getcommentlist")
    public ArrayList<CommentList> getCommentList() throws Exception
    {
        ArrayList<CommentList> commentListArrayList=new ArrayList<>();
        Connection connection = MysqlService.getConnection();
        String sql = "select name,title,kind from comment";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            String name =resultSet.getString("name");
            String title =resultSet.getString("title");
            String kind=resultSet.getString("kind");
            commentListArrayList.add(new CommentList(title,name,kind));

        }
        return  commentListArrayList;
    }

    @RequestMapping("/getMarkContent")
    public String getMarkContent(CommentList list) throws Exception
    {

        Connection connection = MysqlService.getConnection();
        String sql = "select content from comment where name = ? and title = ? and kind =?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,list.name);
        statement.setString(2,list.title);
        statement.setString(3,list.kind);
        ResultSet resultSet = statement.executeQuery();
         if(resultSet.next()) {

            return resultSet.getString(1);
        }
        return  "error :"+"不存在改评论";
    }

    @RequestMapping("/deleteMarkContent")
    public ResponseEntity<Boolean> deleteMarkContent(CommentList list) throws Exception
    {

        Connection connection = MysqlService.getConnection();
        String sql = "delete  from comment where name = ? and title = ? and kind =?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,list.name);
        statement.setString(2,list.title);
        statement.setString(3,list.kind);
          int rows =  statement.executeUpdate();

        return ResponseEntity.ok(rows>0);

    }

    @RequestMapping("/getcommentlistbytitle")
    public ArrayList<CommentList> getCommentListByTitle(String data) throws Exception
    {
        ArrayList<CommentList> commentListArrayList=new ArrayList<>();
        Connection connection = MysqlService.getConnection();
        String sql = "select name,title,kind from comment where kind = ? or title = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,data);
        statement.setString(2,data);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            String name =resultSet.getString("name");
            String title =resultSet.getString("title");
            String kind=resultSet.getString("kind");
            commentListArrayList.add(new CommentList(title,name,kind));

        }
        return  commentListArrayList;
    }

}
class CommentList{
    public String title,name,kind;
    public CommentList(String title,String name,String kind){
        this.kind=kind;
        this.name=name;
        this.title=title;
    }
}
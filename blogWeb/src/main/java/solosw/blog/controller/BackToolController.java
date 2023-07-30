package solosw.blog.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;
import solosw.blog.service.MysqlService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
public class BackToolController {
    @RequestMapping ("/uploadtool")
    public String handleFileUpload(@RequestBody ToolData2 data)  throws  Exception{
        //读取htmlfile的内容
        String sql = "INSERT INTO tool (toolname, toolimage,toolhtml) VALUES (?, ?,?)";
        PreparedStatement preparedStatement= MysqlService.getConnection().prepareStatement(sql);
        System.out.println(data.getName());
        System.out.println(data.getImagebase());
        System.out.println(data.getHtmlfile());
        preparedStatement.setString(1, data.getName());
       preparedStatement.setString(2,data.getImagebase());
       preparedStatement.setString(3, data.getHtmlfile());
       preparedStatement.executeUpdate();
        return "Files uploaded successfully";
    }
    @PostMapping("/gettoollist")
    public ArrayList<String>  getToolList()  throws  Exception{

        String sql = "select toolname from tool";
        PreparedStatement preparedStatement= MysqlService.getConnection().prepareStatement(sql);
        ArrayList<String> all=new ArrayList<>();
        ResultSet resultSet=preparedStatement.executeQuery();
        while (resultSet.next())
        {
            all.add(resultSet.getString(1));
        }

        return all;
    }

    @PostMapping("/deletetool")
    public ResponseEntity<Boolean> deleteTool(String data)  throws  Exception{

        String sql = "delete from tool where toolname = ?";
        PreparedStatement preparedStatement= MysqlService.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,data);
        int rows=preparedStatement.executeUpdate();
        return ResponseEntity.ok(rows>0);
    }
}

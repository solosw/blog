package solosw.blog.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solosw.blog.service.MysqlService;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

@RestController
public class ToolsController {

    @RequestMapping("/gettoollist.action")
    public ArrayList<ToolData> gettoollistAction() throws Exception {
        ArrayList<ToolData> all = new ArrayList<>();
        Connection connection = MysqlService.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT toolname,toolimage FROM tool");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String Name = resultSet.getString("toolname");
            String imageStream = resultSet.getString("toolimage");
            all.add(new ToolData(imageStream, Name));
        }
        resultSet.close();
        statement.close();
        return all;
    }


    @RequestMapping("/gettool")
    public String gettool(String data) throws Exception {

        return "/toolshow.html?name=" + data;

    }

    @RequestMapping("/gettoolhtml")
    public String gettoolhtml(String data) throws Exception {

        Connection connection = MysqlService.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT toolhtml FROM tool where toolname = ?");
        statement.setString(1,data);
        ResultSet resultSet = statement.executeQuery();
        String res="";
        if (resultSet.next()) {
           res =resultSet.getString(1);
        }
        resultSet.close();
        statement.close();
        return res;
    }
}
class ToolData{
    public String image,name;
    public ToolData(String image,String name)
    {
        this.image=image;
        this.name=name;
    }
}
class ToolData2{
    private String imagebase;
    private String name;
    private String htmlfile;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHtmlfile() {
        return htmlfile;
    }

    public String getImagebase() {
        return imagebase;
    }

    public void setHtmlfile(String htmlfile) {
        this.htmlfile = htmlfile;
    }

    public void setImagebase(String imagebase) {
        this.imagebase = imagebase;
    }
}
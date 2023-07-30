package solosw.blog.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import solosw.blog.controller.RegisterController;

import  java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MysqlService {
    @Value("${spring.datasource.url}")
    private  String url;
    private static Connection connection;
    public  static  Connection getConnection(){return connection;}
    @Value("${spring.datasource.username}")
    private  String username;
    @Value("${spring.datasource.password}")
    private  String password;
    @PostConstruct
    public void MysqlConnectionInit() {
        // 在应用程序启动时自动运行的逻辑
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功");
            TimerTask deleteTask = new TimerTask() {
                @Override
                public void run() {
                    deleteRecords(connection);
                    System.out.println("运行中");
                }
            };

            Timer timer = new Timer();
            timer.schedule(deleteTask, 0,   1000*60*60);

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            // 设置定时任务，在每天晚上12点执行清空操作
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    // 清空HashMap
                    RegisterController.codeDir.clear();
                    System.out.println("HashMap cleared at 12:00 AM");
                }
            }, getTimeUntilNextMidnight(), 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);

        } catch (SQLException e) {
           System.out.println("数据库连接失败:"+e);
        }

    }

    public static void deleteRecords(Connection connection) {
        try {
            // 获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -7); // 往前推7天

            Date deleteDate = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String deleteQuery = "DELETE FROM login WHERE logintime < ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, dateFormat.format(deleteDate));

            int deletedRows = preparedStatement.executeUpdate();
            System.out.println("Deleted " + deletedRows + " rows.");

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static long getTimeUntilNextMidnight() {
        long currentTime = System.currentTimeMillis();
        long oneDayMillis = 24 * 60 * 60 * 1000; // 一天的毫秒数

        long midnightMillis = (currentTime / oneDayMillis + 1) * oneDayMillis; // 距离下一个午夜12点的毫秒数
        return midnightMillis - currentTime;
    }
}

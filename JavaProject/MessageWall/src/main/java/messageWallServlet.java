import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/27
 * Time: 17:15
 */


@WebServlet("/messageWall")
public class messageWallServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    //进行保存操作
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 读取 body ,使 JSON字符串 变为 Java的对象
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);
        try {
            DBManager dbManager = new DBManager();
            dbManager.insert(message.from, message.to, message.msg);
            System.out.println(message.toString());
            dbManager.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //设置 响应的状态
        resp.setStatus(200);
    }

    // 获取旧数据
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Message> list = new LinkedList<>();//用于记录
        try {
            //读取旧数据
            DBManager dbManager = new DBManager();
            list = dbManager.selectAll();

            //写成 json 字符串
            String jsonStr = objectMapper.writeValueAsString(list);

            //设置响应格式
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF8");
            resp.getWriter().write(jsonStr);
            dbManager.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

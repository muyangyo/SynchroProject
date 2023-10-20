package API;

import DAO.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/18
 * Time: 22:30
 */
@WebServlet("/blogList")
public class BlogList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //再次强制验证登录,此时不会自动跳转
        if (req.getSession(false) == null) {
            return;
        } else {
            //正常逻辑
            try {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF8");
                BlogDAO blogDAO = new BlogDAO();
                List<Blog> blogs = blogDAO.getBlogs();
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(blogs);
                System.out.println(json);
                resp.getWriter().println(json);
                resp.getWriter().flush();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

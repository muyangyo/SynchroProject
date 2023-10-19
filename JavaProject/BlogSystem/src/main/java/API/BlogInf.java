package API;

import DAO.Blog;
import DAO.BlogDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/19
 * Time: 16:10
 */
@WebServlet("/blogInf")
public class BlogInf extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF8");
        String blogId = req.getParameter("blogId");
        if (blogId == null) {
            return;//用户手动删掉blogId时
        } else {
            try {
                BlogDAO blogDAO = new BlogDAO();
                Blog blog = blogDAO.getBlogById(new Integer(blogId));
                if (blog == null) {
                    return;
                } else {
                    resp.setContentType("application/json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(blog);
                    System.out.println(json);
                    resp.getWriter().println(json);
                    resp.getWriter().flush();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}

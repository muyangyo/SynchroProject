package API;

import DAO.BlogDAO;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/27
 * Time: 20:48
 */
@WebServlet("/delBlog")
public class DelBlog extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");

        HttpSession session = req.getSession(false);
        if (session == null) {
            return;
        }
        String editorName = req.getParameter("editor");
        if (!session.getAttribute("user").equals(editorName)) {
            return;
        }

        try {
            BlogDAO blogDAO = new BlogDAO();
            String[] strings = req.getParameter("blogId").split("=");
            System.out.println(Arrays.toString(strings));

            blogDAO.delBlog(Integer.parseInt(strings[1]));
            System.out.println("已删除一篇文章");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}

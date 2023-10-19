package API;

import DAO.User;
import DAO.UserDAO;

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
 * Time: 17:00
 */
@WebServlet("/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        resp.setContentType("text/html; charset=utf8");
        //验证是否有该账号
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUsersByName(username);

            if (user.userName != null) {
                //账号存在
                if (user.userName.equals(username) && user.password.equals(password)) {
                    resp.sendRedirect("blog_list.html");
                    return;
                } else {
                    System.out.println("用户密码错误!");
                }
            } else {
                System.out.println("用户账号错误!");
            }
            System.out.println(user);
            resp.sendRedirect("login.html");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

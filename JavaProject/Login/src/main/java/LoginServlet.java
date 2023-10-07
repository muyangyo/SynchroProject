import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/7
 * Time: 21:38
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        PrintWriter printWriter = resp.getWriter();
        if (username.equals("admin") && password.equals("123")) {
            printWriter.println("登录成功!\n正在跳转至首页!");
            //printWriter.flush();
            //建立会话
            HttpSession session = req.getSession(true);
            session.setAttribute("username", "admin");
            session.setAttribute("password", "123");
            session.setAttribute("count", 0);
            //跳转至首页
            resp.sendRedirect("index");
        } else {
            //printWriter.println("密码或者账号错误!");
            //return;
            resp.sendRedirect("login.html");
        }
    }
}

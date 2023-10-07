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
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF8");
        PrintWriter printWriter = resp.getWriter();
        HttpSession session = req.getSession(false);
        //未登录状态
        if (session == null) {
            /*printWriter.println("未登录,请先登录!");
            printWriter.flush();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            resp.sendRedirect("login.html");
            return;
        }
        //已经登录的状态
        int count = (int) session.getAttribute("count");
        count++;
        session.setAttribute("count", count);
        printWriter.println("登录总次数" + count);
        printWriter.flush();
    }
}

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/16
 * Time: 16:26
 */
@WebServlet("/hello-world")
public class ServletDemo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理请求部分 (servlet已经帮我们处理了 1.接受请求和解析请求部分 3.返回请求部分
        System.out.println("Hello-World");//这个输出到日志的
        PrintWriter respWriter = resp.getWriter();
        respWriter.println("Hello World");//输出到页面上的
    }
}



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/20
 * Time: 11:57
 */
@WebServlet("/Get")
public class GetDemo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");//设置 resp 的形式,告诉浏览器这个按照 Html 的形式进行显示

        StringBuilder stringBuilder = new StringBuilder();
        //首行数据获取
        stringBuilder.append("getProtocol: " + req.getProtocol() + "<br>");
        stringBuilder.append("getMethod: " + req.getMethod() + "<br>");
        stringBuilder.append("getRequestURI: " + req.getRequestURI() + "<br>");
        stringBuilder.append("getContextPath: " + req.getContextPath() + "<br>");
        stringBuilder.append("getQueryString: " + req.getQueryString() + "<br>");
        stringBuilder.append("====================================<br>");

        //表单参数获取(无论在 QueryString中还是 Body 中)
        stringBuilder.append("getParameterNames : getParameter<br>");
        Enumeration<String> stringEnumeration = req.getParameterNames();
        while (stringEnumeration.hasMoreElements()) {
            String key = stringEnumeration.nextElement();
            stringBuilder.append(key + " : " + req.getParameter(key) + "<br>");
        }
        stringBuilder.append("getParameterValues: " + Arrays.toString(req.getParameterValues("a")) + "<br>");
        stringBuilder.append("====================================<br>");

        //报头
        stringBuilder.append("getHeaderNames : getHeader<br>");
        Enumeration<String> stringEnumeration1 = req.getHeaderNames();
        while (stringEnumeration1.hasMoreElements()) {
            String key = stringEnumeration1.nextElement();
            stringBuilder.append(key + " : " + req.getHeader(key) + "<br>");
        }
        stringBuilder.append("getCharacterEncoding: " + req.getCharacterEncoding() + "<br>");
        stringBuilder.append("getContentType: " + req.getContentType() + "<br>");
        stringBuilder.append("getContentLength: " + req.getContentLength() + "<br>");

        resp.getWriter().print(stringBuilder.toString());
    }
}

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReadCookie extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String[] names = {"firstName", "LastName", "birthDay", "Sex"};
        Cookie[] cookies = request.getCookies();

        response.getWriter().println(
            "<!DOCTYPE html><html><head><title>Read Cookies</title></head><body>"
            + "<h2>Cookie values</h2>"
        );

        for (String name : names) {
            String value = "Cookie not found";

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(name)) {
                        value = URLDecoder.decode(
                            cookie.getValue(), StandardCharsets.UTF_8
                        );
                        break;
                    }
                }
            }

            response.getWriter().println(
                "<p>" + name + " = " + escape(value) + "</p>"
            );
        }

        response.getWriter().println(
            "<p><a href='" + request.getContextPath()
            + "/index.html'>Back to form</a></p></body></html>"
        );
    }

    private String escape(String value) {
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}